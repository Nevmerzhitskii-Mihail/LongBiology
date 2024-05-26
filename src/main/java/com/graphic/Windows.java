package com.graphic;

public class Windows {
    public static MainWindow main_window;
    public static int width, height, tile;

    public static void init(int width, int height, int tile){
        Windows.width = width; Windows.height = height; Windows.tile = tile;
        main_window = new MainWindow("Living System 0.1");
    }
}
