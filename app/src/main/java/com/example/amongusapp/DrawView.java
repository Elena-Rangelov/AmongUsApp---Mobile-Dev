package com.example.amongusapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DrawView extends SurfaceView {
    SurfaceHolder surface;
    Paint paint=new Paint();
    Sprite sprite = new Sprite();
    BadSprite b1, b2, b3, b4 = new BadSprite();

    Canvas canvas;
    boolean isRunning=true;
    int frames=0;
    private static final int MAX_STREAMS=100;
    private int soundIdExplosion;
    private int soundIdBackground;
    private boolean soundPoolLoaded;
    private SoundPool soundPool;
    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        surface=getHolder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long lastTime = System.nanoTime(); // get current time to the nanosecond
                double amountOfTicks = 60; // set the number of updates per second
                double ns = 1000000000 / amountOfTicks; // this determines how many times we can devide 60 into 1e9 of nano seconds or about 1 second
                long timer = System.currentTimeMillis(); // get current time
                int updates = 0; // set frame variable
                while(true){
                    long now = System.nanoTime(); // get current time in nonoseconds durring current loop
                    if(now - lastTime<ns){//if running fast
                        try{
                            Thread.sleep((long)((ns - (now-lastTime)))/1000000);//pause until time for next update
                        }catch(Exception e){}
                    }
                    lastTime = System.nanoTime();  // set lastTime to current time to mark beginning of next loop
                    if(isRunning){
                        if(!surface.getSurface().isValid())continue;
                        canvas = surface.lockCanvas();//lock canvas
                        synchronized (getHolder()){
                            update(canvas);
                            onDraw(canvas);
                        }
                        surface.unlockCanvasAndPost(canvas);//unlock the canvas
                    }
                    updates++; // note that a frame has passed
                    if(System.currentTimeMillis() - timer > 1000 ){ // if one second has passed
                        timer+= 1000; // add a thousand to our timer for next time
                        System.out.println("UPS: " +updates +" FPS: "+frames); // print out how many frames have happend in the last second
                        updates = 0; // reset the update count for the next second
                        frames = 0;// reset the frame count for the next second
                    }
                }
            }
        }).start();
        sprite.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.amongus));
        initSoundPool();//pre-load sounds
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        b1 = generateBadSprite1();
        b2 = generateBadSprite2();
        b3 = generateBadSprite3();
        b4 = generateBadSprite4();
        sprite = generateSprite();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.GRAY);
        canvas.drawRect(getLeft(),0,getRight(),getBottom(),paint);
        paint.setColor(Color.RED);
        sprite.draw(canvas);
        b1.draw(canvas);
        b2.draw(canvas);
        b3.draw(canvas);
        b4.draw(canvas);

        frames++;
    }
    public void update(Canvas canvas){
        if(canvas==null)return;
        sprite.update(canvas);
        b1.update(canvas);
        b2.update(canvas);
        b3.update(canvas);
        b4.update(canvas);
        if(RectF.intersects(sprite, b1) || RectF.intersects(sprite, b2) || RectF.intersects(sprite, b3) || RectF.intersects(sprite, b4)){
            sprite.kill();
            pause();
        }
    }
    public Sprite generateSprite(){
        float x = (float)(Math.random()*(getWidth()-.1*getWidth()));
        Sprite sprite = new Sprite(x,0,x+.1f*getWidth(),.1f*getWidth());
        sprite.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sprites));
        //sprite.show(canvas);
        return sprite;
    }

    public BadSprite generateBadSprite1(){
        float x = (float)(Math.random()*(getWidth()-.1*getWidth()));
        int dX = (int)(Math.random()*(15)+5);
        float y = (float)(getHeight()/3);
        BadSprite b = new BadSprite(x,y,x+.1f*getWidth(),y+.1f*getWidth(), dX);
        b.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sprites));
        return b;

    }
    public BadSprite generateBadSprite2(){
        float x = (float)(Math.random()*(getWidth()-.1*getWidth()));
        int dX = (int)(Math.random()*(15)+5);
        float y = (float)(getHeight()/2);
        BadSprite b = new BadSprite(x,y,x+.1f*getWidth(),y+.1f*getWidth(), dX);
        b.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sprites));
        return b;
    }
    public BadSprite generateBadSprite3(){
        float x = (float)(Math.random ()*(getWidth()-.1*getWidth()));
        int dX = (int)(Math.random()*(15)+5);
        float y = (float)(getHeight()*2/3);
        BadSprite b = new BadSprite(x,y,x+.1f*getWidth(),y+.1f*getWidth(), dX);
        b.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sprites));
        return b;
    }
    public BadSprite generateBadSprite4(){
        float x = (float)(Math.random()*(getWidth()-.1*getWidth()));
        float y = (float)(getHeight());
        return new BadSprite(x,y,x+.1f*getWidth(),y+.1f*getWidth(), 0);
    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if(event.getAction()==MotionEvent.ACTION_DOWN){
//            if(badSprite.contains(event.getX(),event.getY())){
//                badSprite=generateSprite();
//                badSprite.setColor(Color.GREEN);
//            }
//        }
//        return true;
//    }

    public void pause() {//pause-resume
        isRunning=!isRunning;
    }
    private void initSoundPool()  {
        // With Android API >= 21.
        if (Build.VERSION.SDK_INT >= 21 ) {
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            SoundPool.Builder builder= new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);
            this.soundPool = builder.build();
        }
        // With Android API < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }
        // When SoundPool load complete.
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPoolLoaded = true;
                // Playing background sound.
                playSoundBackground();
            }
        });
    }

    public void playSoundBackground()  {
        if(soundPoolLoaded) {
            float leftVolumn = 0.8f;
            float rightVolumn =  0.8f;
            // Play sound background.mp3
            int streamId = this.soundPool.play(this.soundIdBackground,leftVolumn, rightVolumn, 1, -1, 1f);
        }
    }
}

