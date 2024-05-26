package com.graphic;

import com.simulation.World;
import com.utils.MU;
import com.utils.WU;
import com.video.Encoder;

import java.awt.*;

public class Drawer {
    public static void draw(){
        Surface canvas = Windows.main_window.canvas;
        canvas.begin();
        for (int x = 0; x < World.width; x++)
        for (int y = 0; y < World.height; y++){
            drawBack(x, y, canvas);
            drawCell(x, y, canvas);
        }
        canvas.end();
        Encoder.frame(canvas.canvas);
    }

    static void drawBack(int x, int y, Surface canvas){
        Color c;
        if (WU.getAtPos(x, y).energy >= 10000){
            if (WU.getAtPos(x, y).organic >= 10000) c = new Color(190, 190, 190);
            else c = new Color(115, 209, 231);
        }
        else {
            if (WU.getAtPos(x, y).organic >= 10000) c = new Color(215, 151, 142);
            else c = new Color(0, 0, 0);
        }
        canvas.drawRect(c, x * Windows.tile, y * Windows.tile, Windows.tile, Windows.tile, false);
    }

    static void drawCell(int x, int y, Surface canvas){
        if (WU.getAtPos(x, y).cell.index == 0) return;
        switch (WU.getAtPos(x, y).cell.type){
            case Leave:
                canvas.drawRect(new Color(70, 147, 50),x * Windows.tile, y * Windows.tile, Windows.tile, Windows.tile, false);
                break;
            case Root:
                canvas.drawRect(new Color(119, 54, 54),x * Windows.tile, y * Windows.tile, Windows.tile, Windows.tile, false);
                break;
            case Antenna:
                canvas.drawRect(new Color(82, 125, 217),x * Windows.tile, y * Windows.tile, Windows.tile, Windows.tile, false);
                break;
            case Sprout:
                canvas.drawRect(new Color(225, 214, 176),x * Windows.tile, y * Windows.tile, Windows.tile, Windows.tile, false);
                break;
            case Seed:
                canvas.drawRect(new Color(189, 169, 90),x * Windows.tile, y * Windows.tile, Windows.tile, Windows.tile, false);
                break;
            case Pipe:
                canvas.drawRect(new Color(33, 33, 33),x * Windows.tile, y * Windows.tile, Windows.tile, Windows.tile, false);
                break;
        }
//        canvas.drawRect(MU.lerp(new Color(77, 33, 33), new Color(206, 206, 206), (float) WU.getAtPos(x, y).cell.health / 1000),
//                x * Windows.tile + Windows.tile / 4, y * Windows.tile + Windows.tile / 4, Windows.tile / 2, Windows.tile / 2, false);
//        int dx = WU.getDx(WU.getAtPos(x, y).cell.parent);
//        int dy = WU.getDy(WU.getAtPos(x, y).cell.parent);
//        canvas.drawLine(new Color(171, 21, 1), x * Windows.tile + Windows.tile / 2, y * Windows.tile + Windows.tile / 2, x * Windows.tile + Windows.tile / 2 + Windows.tile / 4 * dx, y * Windows.tile + Windows.tile / 2 + Windows.tile / 4 * dy);
//        for (int i = 0; i < 4; i++){
//            dx = WU.getDx(i);
//            dy = WU.getDy(i);
//            if (WU.getAtPos(x, y).cell.energyTo[i] == 1)
//            canvas.drawLine(new Color(139, 1, 171), x * Windows.tile + Windows.tile / 2, y * Windows.tile + Windows.tile / 2, x * Windows.tile + Windows.tile / 2 + Windows.tile / 4 * dx, y * Windows.tile + Windows.tile / 2 + Windows.tile / 4 * dy);
//        }

    }
}
