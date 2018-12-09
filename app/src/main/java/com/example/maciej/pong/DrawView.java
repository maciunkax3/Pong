package com.example.maciej.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
    int width, height;
    int pointTop, pointBottom;

    DrawableElement plateTop, plateBottom ;
    DrawableElement backGroud = null;
    Ball ball;
    Paint paint;

    private SparseArray<PointF> mActivePointers;


    Context context;
    GestureDetector gestureDetector;


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();


            return true;
        }

    }

    public DrawView(Context context) {
        super(context);
        gestureDetector = new GestureDetector(context, new GestureListener());
        this.context = context;
        paint = new Paint();

        mActivePointers = new SparseArray<PointF>();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if(backGroud == null){
            initElements(canvas);
        }
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();
        // Narysowanie czarnego prostokąta jako tło aplikacji.
        backGroud.draw(canvas, paint);
        plateTop.draw(canvas, paint);
        plateBottom.draw(canvas, paint);
        ball.draw(canvas, paint);
        ball.move(4,4);
        checkEdge(ball);
        this.invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // We have a new pointer. Lets add it to the list of pointers

                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                mActivePointers.put(pointerId, f);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    PointF point = mActivePointers.get(event.getPointerId(i));
                    if (point != null) {
                        float x=event.getX(i);
                        float y=event.getY(i);
                        float move = x-point.x;
                        if(x > plateBottom.tl.x && x < plateBottom.tl.x+plateBottom.width &&
                                y > plateBottom.tl.y-100 && y < plateBottom.tl.y+plateBottom.height+50 &&
                                plateBottom.tl.x +move>=0 && plateBottom.tl.x+plateBottom.width+move < width){
                            plateBottom.move((int)move, 0);
                        }
                        else if(x > plateTop.tl.x && x < plateTop.tl.x+plateTop.width &&
                                y > plateTop.tl.y-100 && y < plateTop.tl.y+plateTop.height+50 &&
                                plateTop.tl.x +move>=0 && plateTop.tl.x+plateTop.width+move < width){
                            plateTop.move((int)move, 0);
                        }
                        point.x = x;
                        point.y = y;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointers.remove(pointerId);
                break;

        }
        // Schedules a repaint.
        invalidate();
        return true;
    }


    void initElements(Canvas canvas){
        width = canvas.getWidth();
        height = canvas.getHeight();
        backGroud = new DrawableElement(width, height, new Point(0,0), Color.BLACK);
        plateTop = new DrawableElement(width/5, height/50, new Point((int)(width*0.4),(int)(height*0.02)), Color.WHITE);
        plateBottom = new DrawableElement(width/5, height/50, new Point((int)(width*0.4),(int)(height*0.96)), Color.WHITE);
        ball = new Ball(width/50, height/75, new Point((int)(width*0.49),(int)(height*0.49)), Color.WHITE);

    }
    public void checkEdge(Ball ball){
        if(ball.tl.x < 0){
            ball.tl.x = 0;
            ball.directionX=1;
        }
        if(ball.tl.x >width-10){
            ball.tl.x = width-10;
            ball.directionX=-1;
        }
        if(ball.tl.y < 0){
            loss();
        }
        if(ball.tl.y >height-10){
            loss();
        }
        if(ball.tl.y+ball.height > plateBottom.tl.y && ball.tl.x > plateBottom.tl.x && ball.tl.x < plateBottom.tl.x+plateBottom.width){
            ball.tl.y = plateBottom.tl.y-ball.height;
            ball.directionY = -1;
        }
        if(ball.tl.y < plateTop.tl.y +plateTop.height && ball.tl.x > plateTop.tl.x && ball.tl.x < plateTop.tl.x+plateTop.width){
            ball.tl.y = plateTop.tl.y+plateTop.height;
            ball.directionY = 1;
        }

    }
    public void loss(){
        ball = new Ball(width/50, height/75, new Point((int)(width*0.49),(int)(height*0.49)), Color.WHITE);
    }
}
