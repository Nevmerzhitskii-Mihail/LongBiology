package com.utils;

import com.simulation.Cell;
import com.simulation.World;

public class WU {
    public static class CellData{
        public Cell cell;
        public int organic;
        public int energy;
        public CellData(Cell cell, int organic, int energy){
            this.cell = cell;
            this.organic = organic;
            this.energy = energy;
        }
    }
    public static int normD(int dir){
        return (dir % 4 + 4) % 4;
    }
    public static int normX(int x){
        return (x % World.width + World.width) % World.width;
    }
    public static int normY(int y){
        return (y % World.height + World.height) % World.height;
    }

    public static int getDx(int dir){
        if (dir == 1) return 1;
        if (dir == 3) return -1;
        return 0;
    }
    public static int getDy(int dir){
        if (dir == 0) return 1;
        if (dir == 2) return -1;
        return 0;
    }

    public static int getNx(int x, int dir){
        return normX(x + getDx(normD(dir)));
    }
    public static int getNy(int y, int dir) {
        return normY(y + getDy(normD(dir)));
    }

    public static CellData getAtPos(int x, int y){
        return new CellData(World.list[World.cells_map[x][y]], World.organic_map[x][y], World.energy_map[x][y]);
    }
    public static CellData getAtDir(int x, int y, int dir){
        return getAtPos(getNx(x, dir), getNy(y, dir));
    }

    public static boolean isBlockedCellAtPos(int x, int y){
        return getAtPos(x, y).cell.index != 0 || getAtPos(x, y).energy >= 10000 || getAtPos(x, y).organic >= 10000;
    }
    public static boolean isBlockedCellAtDir(int x, int y, int dir){
        return isBlockedCellAtPos(getNx(x, dir), getNy(y, dir));
    }

    public static void setCellAtPos(int cell, int x, int y){
        World.cells_map[x][y] = cell;
    }
    public static void setOrganicAtPos(int organic, int x, int y){
        World.organic_map[x][y] = organic;
    }
    public static void setEnergyAtPos(int energy, int x, int y){
        World.energy_map[x][y] = energy;
    }

    public static void addCellWithParent(Cell.CellProperty property, int parent){
        property.setNext(parent);
        property.setPrev(World.list[parent].prev);
        addCellToList(property);
    }

    public static void addCellToList(Cell.CellProperty property) {
        int index = findEmptyCell();
        World.list[index].setFromProperty(property);
        World.list[index].is_alive = true;
        World.list[property.next].prev = index;
        World.list[property.prev].next = index;
        setCellAtPos(index, World.list[index].x, World.list[index].y);
    }

    public static void delCell(int index) {
        World.list[index].is_alive = false;
        World.list[World.list[index].prev].next = World.list[index].next;
        World.list[World.list[index].next].prev = World.list[index].prev;
        setCellAtPos(0, World.list[index].x, World.list[index].y);
    }

    public static int findEmptyCell() {
        for (int i = 1; i < World.width * World.height + 1; i++){
            if (!World.list[i].is_alive) return i;
        }
        return -1;
    }
}
