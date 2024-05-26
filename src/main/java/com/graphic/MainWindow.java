package com.graphic;

import com.video.Encoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class MainWindow extends JFrame {
    public Surface canvas;
    public MainWindow(String title){
        super(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        Container container = getContentPane();

        canvas = new Surface(Windows.width, Windows.height);
        container.add(canvas, BorderLayout.CENTER);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 's'){
                    System.out.println("Encode start");
                    Encoder.start();
                }
                if (e.getKeyChar() == 'e'){
                    System.out.println("Encode end");
                    Encoder.end();
                    System.out.println(Encoder.is_recording);
                }
            }
        });

        pack();
        setSize(Windows.width, Windows.height + 50);
    }
}
