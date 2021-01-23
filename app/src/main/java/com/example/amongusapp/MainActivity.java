package com.example.amongusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {
    DrawView drawView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set No Title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        drawView=findViewById(R.id.drawView);
        /**
         * Option way of getting fullscreen and no title
         * //Set fullscreen
         * this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
         *        WindowManager.LayoutParams.FLAG_FULLSCREEN);
         *
         * //Set No Title
         * this.requestWindowFeature(Window.FEATURE_NO_TITLE);
         **/
    }

    public void moveLeft(View view) {
        drawView.sprite.setdX(-3);//set horizontal speed to move left
    }

    public void moveRight(View view) {
        drawView.sprite.setdX(3);//set horizontal speed to move right
    }

    public void moveUp(View view) {
        drawView.sprite.setdY(-3);//set vertical speed to move up
    }

    public void moveDown(View view) {

        drawView.sprite.setdY(6);//set vertical speed to move down

    }

    public void restart(View view){
        drawView.sprite = drawView.generateSprite();
        drawView.b1 = drawView.generateBadSprite1();
        drawView.b2 = drawView.generateBadSprite2();
        drawView.b3 = drawView.generateBadSprite3();
        drawView.b4 = drawView.generateBadSprite4();
        drawView.paint.setColor(Color.GRAY);
        drawView.canvas.drawRect(drawView.getLeft(),0,drawView.getRight(),drawView.getBottom(),drawView.paint);
        drawView.pause();

    }


}
