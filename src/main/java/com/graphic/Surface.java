package com.graphic;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Surface extends JPanel {
    BufferedImage buffer;
    public BufferedImage canvas;
    Graphics2D graphic;


    public int width, height;

    public Surface(int width, int height){
        this.width = width; this.height = height;
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void paint(Graphics g){
        ((Graphics2D) g).drawImage(buffer, null, 0, 0);
    }

    public void begin(){
        graphic = canvas.createGraphics();
        drawRect(Color.black, 0, 0, width, height, false);
    }

    public void end(){
        buffer.createGraphics().drawImage(canvas, null, 0, 0);
        repaint();
    }

    public void drawLine(Color color, int x0, int y0, int x1, int y1){
        graphic.setColor(color);
        graphic.drawLine(x0, y0, x1, y1);
    }

    public void drawRect(Color color, int x0, int y0, int w, int h, boolean border){
        graphic.setColor(color);
        if (border) graphic.drawRect(x0, y0, w, h);
        else graphic.fillRect(x0, y0, w, h);
    }
}
