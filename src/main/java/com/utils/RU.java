package com.utils;

import com.Main;
import com.simulation.Cell;

import java.util.Random;

public class RU {
    static Random random = new Random();

    public static void setSeed(int seed){
        random.setSeed(seed);
    }

    public static int getRandint(int min, int max){
        return (random.nextInt() % (max - min + 1) + max - min + 1) % (max - min + 1) + min;
    }

    public static int[][] getRandmind(){
        int[][] mind = new int[Cell.MIND_SIZE][Cell.GENE_SIZE];
        for (int i = 0; i < Cell.MIND_SIZE; i++)
            for (int j = 0; j < Cell.GENE_SIZE; j++){
                mind[i][j] = getRandint(0, 255);
            }
        return mind;
    }
}
