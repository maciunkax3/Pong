package com.example.maciej.pong;

import android.graphics.Point;

public class Ball extends DrawableElement{
    public int directionX = 1;
    public int directionY = 1;
    public float acc = (float) 1.001;
    public float v = 1;

    public Ball(int width, int height, Point tl, int color) {

        super(width, height, tl, color);
    }
    @Override
    public void move(float x, float y){
        super.move(x*directionX*v, y*directionY*v);
        v *=acc;
    }
}
