package com.example.amongusapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

class Sprite extends RectF {
    private static final int BMP_COLUMNS = 7;
    private static final int BMP_ROWS = 10;
    private static final int DOWN=0, LEFT=1, RIGHT=2, UP=3;
    private int dX, dY, color;
    private boolean flip;
    private Bitmap bitmap;
    private int currentFrame=0, iconWidth, iconHeight, animationDelay=20;
    public Sprite() {
        this(1,2, Color.RED);
    }
    public Sprite(int dX, int dY, int color) {
        this(1,1,11,11,dX,dY,color);
    }

    public Sprite(float left, float top, float right, float bottom) {
        this(left, top, right, bottom,0,4,Color.BLUE);
    }
    public Sprite(RectF r){
        super(r);
    }
    public Sprite(float left, float top, float right, float bottom, int dX, int dY, int color) {
        super(left, top, right, bottom);
        this.dX = dX;
        this.dY = dY;
        this.color = color;
    }
    public void update(Canvas canvas){
        if(left+dX<0||right+dX>canvas.getWidth())//if next step hits boundary
            dX*=-1; //bounce off left and right boundaries
        if(top+dY>canvas.getHeight())//if next step puts off bottom of screen
            offsetTo(left,-height());//teleport to top of screen
        if(bottom+dY<0)
            offsetTo(left,canvas.getHeight());
        offset(dX,dY);//moves dX to the right and dY downwards
        if(animationDelay--<0) {//increment to next sprite image after delay
            if(currentFrame == 4)
                currentFrame = 1;//cycles current image with boundary proteciton
            else
                currentFrame = ++currentFrame;
            animationDelay=20;//arbitrary delay before cycling to next image
        }
    }

    public void draw(Canvas canvas){
        if(bitmap==null) {//if no bitmap exists draw a red circle
            Paint paint = new Paint();
            paint.setColor(color);//sets its color
            canvas.drawCircle(centerX(), centerY(), width() / 2, paint);//draws circle
        }else {
            iconWidth = bitmap.getWidth() / BMP_COLUMNS;//calculate width of 1 image
            iconHeight = bitmap.getHeight() / BMP_ROWS; //calculate height of 1 image
            if((dX < 0) != flip)
                bitmap = flipBitmap();
            flip = dX < 0;
            int srcY = currentFrame * iconHeight;
            Rect src = new Rect(3*iconWidth, srcY, 3*iconWidth + iconWidth, srcY + iconHeight);  //defines the rectangle inside of heroBmp to displayed
            canvas.drawBitmap(bitmap,src, this,null); //draw an image
        }
    }

    private boolean getAnimationFlip() {
        return Math.abs(dX) == dX;
    }

    private Bitmap flipBitmap() {
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1, bitmap.getWidth()/2f, bitmap.getHeight()/2f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    }

    public void kill(){
        currentFrame = 7;
    }


    public int getdX() {
        return dX;
    }

    public void setdX(int dX) {
        this.dX = dX;
    }

    public int getdY() {
        return dY;
    }

    public void setdY(int dY) {
        this.dY = dY;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void hide(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);//sets its color
        canvas.drawCircle(centerX(), centerY(), width() / 2, paint);//draws circle
    }

    public void show(Canvas canvas){
        draw(canvas);
    }

}

