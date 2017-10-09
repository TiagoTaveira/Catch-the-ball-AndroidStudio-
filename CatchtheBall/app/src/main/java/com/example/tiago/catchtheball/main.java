package com.example.tiago.catchtheball;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class main extends AppCompatActivity {


    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView green_ball;
    private ImageView red_ball;
    private ImageView bomb100;

    private int screenWidth;
    private int screenHeight;



    private int frameHeight;
    private int boxSize;
    //position
    private int boxY;
    private int green_ballX;
    private int green_ballY;
    private int red_ballX;
    private int red_ballY;
    private int bomb100X;
    private int bomb100Y;


    //final ImageView backgroundOne = (ImageView) findViewById(R.id.background_one);
    //final ImageView backgroundTwo = (ImageView) findViewById(R.id.background_two);
    //final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);

    private int score = 0;

    private int startTime = (int) System.currentTimeMillis();
    private int initTime = (int)System.currentTimeMillis();



    private Handler handler = new Handler();
    private Timer timer = new Timer();

    private boolean action_flag = false;
    private boolean start_flag = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //createBackground();

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        box = (ImageView) findViewById(R.id.box);
        green_ball = (ImageView) findViewById(R.id.green_ball);
        red_ball = (ImageView) findViewById(R.id.red_ball);
        bomb100 = (ImageView) findViewById(R.id.bomb100);

        WindowManager windowManager = getWindowManager();

        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);


        screenWidth = point.x;
        screenHeight = point.y;


        green_ball.setX(-80);
        green_ball.setY(-80);
        red_ball.setX(-80);
        red_ball.setY(-80);
        bomb100.setX(-80);
        bomb100.setY(-80);

    }

    /*
    public void createBackground() {

        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX - width);
            }
        });

        animator.start();
    } */

    public void hitCheck() {

        int greenCenterX = green_ballX + green_ball.getWidth()/2;
        int greenCenterY = green_ballY + green_ball.getHeight()/2;

        if (0 <= greenCenterX && greenCenterX <= boxSize &&
                boxY <= greenCenterY && greenCenterY <= boxY + boxSize) {
            score += 30;
            green_ballX = -10;
        }

        int redCenterX = red_ballX + red_ball.getWidth()/2;
        int redCenterY = red_ballY + red_ball.getHeight()/2;

        if (0 <= redCenterX && redCenterX <= boxSize &&
                boxY <= redCenterY && redCenterY <= boxY + boxSize) {
            score += 10;
            red_ballX = -10;
        }

        int bombCenterX = bomb100X + bomb100.getWidth()/2;
        int bombCenterY = bomb100Y + bomb100.getHeight()/2;

        if (0 <= bombCenterX && bombCenterX <= boxSize &&
                boxY <= bombCenterY && bombCenterY <= boxY + boxSize) {

            timer.cancel();
            timer = null;

            Intent intent = new Intent(getApplicationContext(), result.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);

        }
    }

    public void changePos() {

        hitCheck();

        float speed = (float)(Math.sqrt(1 + (startTime - initTime)/1000.0))*screenWidth/3000.0f;

        green_ballX -= 18 * (speed);
        if (green_ballX < 0) {
            green_ballX = screenWidth + 5000;
            green_ballY = (int) Math.floor(Math.random() * (frameHeight - green_ball.getHeight()));
        }

        green_ball.setX(green_ballX);
        green_ball.setY(green_ballY);



        bomb100X -= 13 * (speed);
        if (bomb100X < 0) {
            bomb100X = screenWidth + 10;
            bomb100Y = (int) Math.floor(Math.random() * (frameHeight - bomb100.getHeight()));
        }

        bomb100.setX(bomb100X);
        bomb100.setY(bomb100Y);



        red_ballX -= 12 * (speed);
        if (red_ballX < 0) {
            red_ballX = screenWidth + 20;
            red_ballY = (int) Math.floor(Math.random() * (frameHeight - red_ball.getHeight()));
        }

        red_ball.setX(red_ballX);
        red_ball.setY(red_ballY);



        if(action_flag) {
            boxY -= 18;
        } else {
            boxY += 18;
        }

        if (boxY < 0) {
            boxY = 0;
        }

        if (boxY > (frameHeight - boxSize)) {
            boxY = frameHeight - boxSize;
        }


        box.setY(boxY);

        scoreLabel.setText("Score: " + score);
    }


    public boolean onTouchEvent (MotionEvent event) {

        if(!start_flag) {

            start_flag = true;

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();
            boxY = (int) box.getY();
            boxSize = box.getHeight();


            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                            startTime = (int) System.currentTimeMillis();


                        }
                    });
                }
            }, 0, 20);

        } else{
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                action_flag = true;

            } else if (event.getAction() == MotionEvent.ACTION_UP){
                action_flag = false;
            }
        }
        return true;
    }
}
