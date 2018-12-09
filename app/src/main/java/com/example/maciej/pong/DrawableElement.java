package com.example.maciej.pong;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class DrawableElement {
    public int width;
    public int height;
    public Point tl;
    int color;


    public DrawableElement(int width, int height, Point tl, int color) {
        this.width = width;
        this.height = height;
        this.tl = tl;
        this.color = color;
    }
    public void draw(Canvas canvas, Paint paint){
        paint.setColor(color);
        canvas.drawRect(tl.x,tl.y,tl.x+width,tl.y+height,paint);
    }
    public void move(float x, float y){
        tl.x+=x;
        tl.y+=y;
    }
}
