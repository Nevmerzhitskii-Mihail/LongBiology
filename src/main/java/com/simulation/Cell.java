package com.simulation;

import com.utils.MU;
import com.utils.RU;
import com.utils.WU;

public class Cell {
    public static class CellProperty{
        public int prev, next; // Ссылки на предыдущую и следующую клетки

        public int x, y; // Позиция клетки
        public int health; // Энергия клетки
        public byte parent; // Направление к родительской клетке (=-1, если родительской клетки нет)
        public byte[] energyTo = new byte[4]; // Массив направлений передачи энергии
        public CellType type = CellType.Null;

        public int[][] mind = new int[MIND_SIZE][GENE_SIZE]; // Геном
        public int adr; // Текущий ген

        public CellProperty setPrev(int prev){
            this.prev = prev;
            return this;
        }
        public CellProperty setNext(int next){
            this.next = next;
            return this;
        }
        public CellProperty setPos(int x, int y){
            this.x = x;
            this.y = y;
            return this;
        }
        public CellProperty setHealth(int health){
            this.health = health;
            return this;
        }
        public CellProperty setParent(byte parent){
            this.parent = parent;
            return this;
        }
        public CellProperty setEnergyTo(byte[] energyTo){
            System.arraycopy(energyTo, 0, this.energyTo, 0, 4);
            return this;
        }
        public CellProperty setMind(int[][] mind){
            for (int i = 0; i < MIND_SIZE; i++) System.arraycopy(mind[i], 0, this.mind[i], 0, GENE_SIZE);
            return this;
        }
        public CellProperty setAdr(int adr){
            this.adr = adr;
            return this;
        }
        public CellProperty setType(CellType type){
            this.type = type;
            return this;
        }
    }
    public enum CellType {
        Null, Sprout, Leave, Pipe, Root, Antenna, Seed
    }
    public static int MIND_SIZE = 32;
    public static int GENE_SIZE = 15; // Размер генома

    public int prev, next; // Ссылки на предыдущую и следующую клетки
    public int index;
    public boolean is_alive;

    public int x, y; // Позиция клетки
    public int health; // Энергия клетки
    public byte parent; // Направление к родительской клетке (=-1, если родительской клетки нет)
    public byte[] energyTo = new byte[4]; // Массив направлений передачи энергии
    public CellType type = CellType.Null;
    int healthA, healthB;
    int dir = 0;

    int[][] mind = new int[MIND_SIZE][GENE_SIZE]; // Геном
    int adr; // Текущий ген

    int seedSteps = 0;

    public Cell(int index){
        this.index = index;
    }

    public void setFromProperty(CellProperty p){
        adr = p.adr;
        energyTo = p.energyTo;
        mind = p.mind;
        x = p.x;
        y = p.y;
        health = p.health;
        type = p.type;
        parent = p.parent;
        prev = p.prev;
        next = p.next;
    }

    public void cellStep(){
        swapBuffer();
        if (parent != -1 && WU.getAtDir(x, y, parent).cell.index == 0) parent = -1;
        if (parent != -1) dir = WU.normD(parent + 2);
        switch (type){
            case Leave:
                leaveStep();
                break;
            case Root:
                rootStep();
                break;
            case Antenna:
                antennaStep();
                break;
            case Seed:
                seedStep();
                break;
            case Pipe:
                pipeStep();
                break;
            case Sprout:
                sproutStep();
                break;
        }
        health--;
        if (health <= 0 || WU.getAtPos(x, y).energy >= 10000 || WU.getAtPos(x, y).organic >= 10000) {
            killCell();
        }
    }

    void leaveStep(){
        testEnergyTo();
        if (parent == -1){killCell(); return;}
        int delta = (int) MU.lerp(0, 80, (double) WU.getAtPos(x, y).organic / 10000);
        health += delta;
        distributeEnergy();
    }

    void rootStep(){
        testEnergyTo();
        if (parent == -1){killCell(); return;}
        int delta = Math.min(100, WU.getAtPos(x, y).organic);
        health += Math.max(0, delta - 3);
        WU.setOrganicAtPos(WU.getAtPos(x, y).organic - delta, x, y);
        distributeEnergy();
    }

    void antennaStep(){
        testEnergyTo();
        if (parent == -1){killCell(); return;}
        int delta = Math.min(100, WU.getAtPos(x, y).energy);
        health += Math.max(0, delta - 3);
        WU.setEnergyAtPos(WU.getAtPos(x, y).energy - delta, x, y);
        distributeEnergy();
    }

    void seedStep(){
        seedSteps--;
        if (seedSteps <= 0) {
            type = CellType.Sprout;
            seedSteps = 0;
        }
    }

    void pipeStep(){
        testEnergyTo();
        distributeEnergy();
    }

    void sproutStep(){
        grow();
    }

    void grow(){
        if (health < energyForGrow()) return;
        for (int i = 0; i < 3; i++){
            int ndir = WU.normD(dir + i - 1);
            CellType ntype = getTypeFromGene(mind[adr][i]);
            if (ntype != CellType.Null && !WU.isBlockedCellAtDir(x, y, ndir)){
                CellProperty p = new CellProperty().setType(ntype).setParent((byte) WU.normD(ndir + 2)).setAdr(mind[adr][i] % MIND_SIZE).setPos(WU.getNx(x, ndir), WU.getNy(y, ndir)).setMind(getMutatedMind()).setEnergyTo(new byte[]{0, 0, 0, 0}).setHealth(0);
                if (ntype == CellType.Sprout) energyTo[ndir] = 1;
                else p.energyTo[p.parent] = 1;
                WU.addCellWithParent(p, index);
                health -= 30;
            }
        }
        type = CellType.Pipe;
        distributeEnergy();
    }

    int energyForGrow(){
        int energy = 0;
        for (int i = 0; i < 3; i++){
            int ndir = WU.normD(dir + i - 1);
            CellType ntype = getTypeFromGene(mind[adr][i]);
            if (ntype != CellType.Null && !WU.isBlockedCellAtDir(x, y, ndir)) energy += 30;
        }
        return energy;
    }

    CellType getTypeFromGene(int gene){
        if (gene < 2 * MIND_SIZE) return CellType.Sprout;
        if (gene < 2 * MIND_SIZE + 20) return CellType.Leave;
        if (gene < 2 * MIND_SIZE + 40) return CellType.Root;
        if (gene < 2 * MIND_SIZE + 60) return CellType.Antenna;
        return CellType.Null;
    }

    public int[][] getMutatedMind(){
        int[][] nmind = new int[MIND_SIZE][GENE_SIZE];
        for (int i = 0; i < MIND_SIZE; i++) System.arraycopy(mind[i], 0, nmind[i], 0, GENE_SIZE);
        if (RU.getRandint(0, 100) <= 20) {
            nmind[RU.getRandint(0, MIND_SIZE - 1)][RU.getRandint(0, GENE_SIZE - 1)] = RU.getRandint(0, 255);
        }
        return nmind;
    }

    public void addEnergyToBuffer(int e){
        if (World.iteration % 2 == 0) healthA += e;
        else healthB += e;
    }

    void swapBuffer(){
        if (World.iteration % 2 == 0) {
            health += healthB;
            healthB = 0;
        }
        else {
            health += healthA;
            healthA = 0;
        }
    }

    public void killCell(){
        int organic = 0;
        int energy = 0;
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                int xt = WU.normX(x + dx), yt = WU.normY(y + dy);
                organic += WU.getAtPos(xt, yt).organic;
                energy += WU.getAtPos(xt, yt).energy;
            }
        }
        organic = (organic + 30) / 9;
        energy = (energy + health) / 9;
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                int xt = WU.normX(x + dx), yt = WU.normY(y + dy);
                WU.setEnergyAtPos(energy, xt, yt);
                WU.setOrganicAtPos(organic, xt, yt);
            }
        }
        for(int i = 0; i < 4; i++){
            if (WU.getAtDir(x, y, i).cell.index == 0) continue;
            WU.getAtDir(x, y, i).cell.energyTo[WU.normD(i + 2)] = 0;
        }
        WU.delCell(index);
    }

    public void testEnergyTo(){
        for (int i = 0; i < 4; i++){
            if (WU.getAtDir(x, y, i).cell.index == 0) {
                energyTo[i] = 0;
            }
        }
        if (energyTo[0] + energyTo[1] + energyTo[2] + energyTo[3] == 0) {
            if (parent == -1) {killCell(); return;}
            WU.getAtDir(x, y, parent).cell.energyTo[WU.normD(parent + 2)] = 0;
            energyTo[parent] = 1;
        }
    }

    public void distributeEnergy(){
        int count = 1 + energyTo[0] + energyTo[1] + energyTo[2] + energyTo[3];
        int delta = health / count;
        for (int i = 0; i < 4; i++){
            if (energyTo[i] == 1) WU.getAtDir(x, y, i).cell.addEnergyToBuffer(delta);
        }
        health -= delta * (count - 1);
    }
}
