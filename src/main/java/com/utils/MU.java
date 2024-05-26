package com.utils;

import java.awt.*;

public class MU {
    public static double clamp(double v, double min, double max){
        return Math.max(min, Math.min(max, v));
    }
    public static double lerp(double a, double b, double t){
        return (b - a) * clamp(t, 0, 1) + a;
    }

    public static Color lerp(Color a, Color b, double t){
        int red = (int) lerp(a.getRed(), b.getRed(), t);
        int green = (int) lerp(a.getGreen(), b.getGreen(), t);
        int blue = (int) lerp(a.getBlue(), b.getBlue(), t);
        return new Color(red, green, blue);
    }
}
