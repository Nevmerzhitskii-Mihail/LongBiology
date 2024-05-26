package com.video;

import org.jcodec.api.awt.AWTSequenceEncoder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Encoder {
    static AWTSequenceEncoder encoder;
    static String directory;
    static int record;
    static int fps;
    public static boolean is_recording = false;

    public static void init(String directory, int fps) {
        Encoder.directory = directory;
        record = 0;
        Encoder.fps = fps;
    }
    
    public static void start(){
        if (!is_recording) {
            try {
                encoder = AWTSequenceEncoder.createSequenceEncoder(new File(directory + "\\" + record + ".mp4"), fps);
                is_recording = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void frame(BufferedImage frame){
        if (is_recording) {
            try {
                try {
                    encoder.encodeImage(frame);
                }
                catch (IllegalStateException e){

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void end() {
        if (is_recording) {
            try {
                is_recording = false;
                encoder.finish();
                record++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
