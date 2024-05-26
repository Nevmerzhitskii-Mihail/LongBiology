package com.simulation;

import com.utils.WU;
import com.utils.RU;

public class World {
    public static int width, height;
    public static int[][] cells_map;
    public static int[][] energy_map;
    public static int[][] organic_map;
    public static Cell[] list;

    public static int iteration = 0;

    public static void init(int width, int height){
        World.width = width;
        World.height = height;
        cells_map = new int[width][height];
        energy_map = new int[width][height];
        organic_map = new int[width][height];
        list = new Cell[width * height + 1];
        for (int i = 0; i < width * height + 1; i++){
            list[i] = new Cell(i);
        }
    }

    public static void start(){
        clear();
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++){
                organic_map[x][y] = 4000;
                energy_map[x][y] = 0;
            }
        for (int x = 0; x < width; x+=20)
            for (int y = 0; y < height; y+=20){
                WU.addCellWithParent(new Cell.CellProperty().setParent((byte) -1).setPos(x, y).setAdr(0).setHealth(4000).setEnergyTo(new byte[]{0, 0, 0, 0}).setType(Cell.CellType.Sprout).setMind(RU.getRandmind()), 0);
            }
    }

    static void clear(){
        for (int i = 1; i < width * height + 1; i++){
            WU.delCell(i);
        }
    }

    public static void step(){
        int cur = list[0].next;
        while (cur != 0){
            int next = list[cur].next;
            list[cur].cellStep();
            cur = next;
        }
        iteration++;
    }
}
