package com;

import com.graphic.Drawer;
import com.graphic.Windows;
import com.simulation.World;
import com.utils.RU;
import com.video.Encoder;
import org.jcodec.api.awt.AWTSequenceEncoder;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String... args) throws InterruptedException, IOException {
        int width = 1800, height = 960, tile = 4;
        RU.setSeed(2);

        Windows.init(width, height, tile);
        World.init(width / tile, height / tile);
        World.start();
        Encoder.init("C:\\Progects\\Java\\LongBiology\\records\\test", 30);

        while (true){
            Drawer.draw();
            for(int i = 0; i < 3; i++) World.step();
        }
    }
}
