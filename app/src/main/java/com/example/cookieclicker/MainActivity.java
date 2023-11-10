package com.example.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    ImageView ship;
    ImageView worker;
    ImageView factory;
    TextView t;
    TextView textViewInCode;
    ImageView workerRoboInCode;
    ImageView factoryRoboInCode;
    ConstraintLayout myLayout;
    TextView activeWorkers;
    TextView activeFactories;
    public static int activeWorkerCount = 0;
    public static int activeFactoryCount = 0;
    public static int WORKER_COST = 20;
    public static int FACTORY_COST = 120;
    Animation fadeIn;
    Animation fadeOut;
    public static AtomicInteger ai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myLayout = findViewById(R.id.id_layout);
        ai=new AtomicInteger(0);
        t = findViewById(R.id.textView);
        ship = findViewById(R.id.imageView);
        ship.setImageResource(R.drawable.boat);
        worker=findViewById(R.id.worker);
        worker.setImageResource(R.drawable.workers);
        factory=findViewById(R.id.industry);
        factory.setImageResource(R.drawable.factory);
        activeWorkers=findViewById(R.id.activeWrokers);
        activeFactories=findViewById(R.id.activeFactories);

        AnimationDrawable animDrawable= (AnimationDrawable)myLayout.getBackground();
        animDrawable.setEnterFadeDuration(2500);
        animDrawable.setExitFadeDuration(5000);
        animDrawable.start();






        //toX, fromY, toY, pivotXType, pivotXValue,pivotYType, pivotYValue
        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(30);//1000=1 sec
        //fadeIN and out Animation
        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000); // set duration for the animation in milliseconds

        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(1000); // set duration for the animation in milliseconds

        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeOut.setInterpolator(new DecelerateInterpolator());

        // anaimation for +1

        ship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(scaleAnimation);

                ai.getAndAdd(1);
                resetStates(fadeIn, fadeOut);
                generateIncrementalPlusOne();
                t.setText("Ships: " + ai.get());
            }
        });
        worker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ai.get() >= WORKER_COST) {
                    activeWorkerCount++;
                    activeWorkers.setText("Active Workers:" + activeWorkerCount);
                    ai.getAndAdd(-WORKER_COST);
                    generateWorkerRobo();
                    t.setText("Ships: " + ai.get());
                    if(ai.get() < WORKER_COST){
                        if(worker.getVisibility() == View.VISIBLE) {
                            worker.startAnimation(fadeOut);
                            worker.setVisibility(View.INVISIBLE);
                        }

                    }
                    if(ai.get() < FACTORY_COST){
                        if(factory.getVisibility() == View.VISIBLE) {
                            factory.startAnimation(fadeOut);
                            factory.setVisibility(View.INVISIBLE);
                        }

                    }
                }
            }
        });

        factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ai.get() >= FACTORY_COST) {
                    activeFactoryCount++;
                    activeFactories.setText("Active Factories:" + activeFactoryCount);
                    ai.getAndAdd(-FACTORY_COST);
                    generateFactoryRObo();
                    t.setText("Ships: " + ai.get());
                    if(ai.get() < FACTORY_COST){
                        if(factory.getVisibility() == View.VISIBLE) {
                            factory.startAnimation(fadeOut);
                            factory.setVisibility(View.INVISIBLE);
                        }

                    }
                    if(ai.get() < WORKER_COST){
                        if(worker.getVisibility() == View.VISIBLE) {
                            worker.startAnimation(fadeOut);
                            worker.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });


    }

    private void generateIncrementalPlusOne() {
        textViewInCode = new TextView(this);
        textViewInCode.setId(View.generateViewId());
        textViewInCode.setText("+1");
        textViewInCode.setTextSize(30.0f);
        textViewInCode.setTextColor(Color.WHITE);

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        textViewInCode.setLayoutParams(lp);

        //connections and bias
        myLayout.addView(textViewInCode);
        ConstraintSet cs = new ConstraintSet();
        cs.clone(myLayout);

        //Top
        cs.connect(textViewInCode.getId(), ConstraintSet.TOP, ship.getId(), ConstraintSet.TOP);
        //bottom
        cs.connect(textViewInCode.getId(), ConstraintSet.BOTTOM, ship.getId(), ConstraintSet.BOTTOM);
        //sides
        cs.connect(textViewInCode.getId(), ConstraintSet.LEFT, ship.getId(), ConstraintSet.LEFT);
        cs.connect(textViewInCode.getId(), ConstraintSet.RIGHT, ship.getId(), ConstraintSet.RIGHT);

        cs.setHorizontalBias(textViewInCode.getId(), generateRandomfloat());
        cs.setVerticalBias(textViewInCode.getId(), generateRandomfloat());
        cs.applyTo(myLayout);
        Animation textAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, -1.0f
        );
        textAnimation.setDuration(2500); // 1 second
        textAnimation.setRepeatCount(Animation.ABSOLUTE); //
        textAnimation.setFillAfter(true); // persist after animation is finished
        textAnimation.setFillEnabled(true);
        textViewInCode.startAnimation(textAnimation);

    }
    public float generateRandomfloat() {
        Random random = new Random();
        return  0.01f + random.nextFloat() * 0.98f;
    }


    private void generateWorkerRobo() {
        workerRoboInCode = new ImageView(this);
        workerRoboInCode.setId(View.generateViewId());
        workerRoboInCode.setImageResource(R.drawable.workers);


        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(90, 90);
        workerRoboInCode.setLayoutParams(lp);

        //connections and bias
        myLayout.addView(workerRoboInCode);
        ConstraintSet cs = new ConstraintSet();
        cs.clone(myLayout);

        //Top
        cs.connect(workerRoboInCode.getId(), ConstraintSet.TOP, myLayout.getId(), ConstraintSet.TOP);
        //bottom
        cs.connect(workerRoboInCode.getId(), ConstraintSet.BOTTOM, myLayout.getId(), ConstraintSet.BOTTOM);
        //sides
        cs.connect(workerRoboInCode.getId(), ConstraintSet.LEFT, myLayout.getId(), ConstraintSet.LEFT);
        cs.connect(workerRoboInCode.getId(), ConstraintSet.RIGHT, myLayout.getId(), ConstraintSet.RIGHT);

        cs.setHorizontalBias(workerRoboInCode.getId(), generateRandomfloat());
        cs.setVerticalBias(workerRoboInCode.getId(), 0.89f);
        cs.applyTo(myLayout);
        Animation rotateAnimation = new RotateAnimation(-10, 10, Animation.RELATIVE_TO_SELF, 0.3f, Animation.RELATIVE_TO_SELF, 0.3f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        workerRoboInCode.startAnimation(rotateAnimation);

        // Also start the thread
        Thread workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ai.getAndAdd(1);
                            t.setText("Ships: " + ai.get());
                            resetStates(fadeIn, fadeOut);
                        }
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Start the background thread
        workerThread.start();

        //WorkerRoboThread workerThread =  new WorkerRoboThread();
        //workerThread.start();
    }

    private void generateFactoryRObo() {
        factoryRoboInCode = new ImageView(this);
        factoryRoboInCode.setId(View.generateViewId());
        factoryRoboInCode.setImageResource(R.drawable.factory);


        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(90, 90);
        factoryRoboInCode.setLayoutParams(lp);

        //connections and bias
        myLayout.addView(factoryRoboInCode);
        ConstraintSet cs = new ConstraintSet();
        cs.clone(myLayout);

        //Top
        cs.connect(factoryRoboInCode.getId(), ConstraintSet.TOP, myLayout.getId(), ConstraintSet.TOP);
        //bottom
        cs.connect(factoryRoboInCode.getId(), ConstraintSet.BOTTOM, myLayout.getId(), ConstraintSet.BOTTOM);
        //sides
        cs.connect(factoryRoboInCode.getId(), ConstraintSet.LEFT, myLayout.getId(), ConstraintSet.LEFT);
        cs.connect(factoryRoboInCode.getId(), ConstraintSet.RIGHT, myLayout.getId(), ConstraintSet.RIGHT);

        cs.setHorizontalBias(factoryRoboInCode.getId(), generateRandomfloat());
        cs.setVerticalBias(factoryRoboInCode.getId(),0.99f);
        cs.applyTo(myLayout);
        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.4f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        factoryRoboInCode.setAnimation(alphaAnimation);
        Thread factoryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ai.getAndAdd(1);
                            t.setText("Ships: " + ai.get());
                            resetStates(fadeIn, fadeOut);
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        factoryThread.start();
    }


    private void resetStates(Animation fadeIn, Animation fadeOut){

        if(ai.get() >=  WORKER_COST){
            if(worker.getVisibility() == View.INVISIBLE) {
                worker.setAnimation(fadeIn);
                worker.setVisibility(View.VISIBLE);
            }
        } else {
            if(worker.getVisibility() == View.VISIBLE) {
                worker.startAnimation(fadeOut);
                worker.setVisibility(View.INVISIBLE);
            }
       }
        if(ai.get() >=  FACTORY_COST){
            if(factory.getVisibility() == View.INVISIBLE) {
                factory.startAnimation(fadeIn);
                factory.setVisibility(View.VISIBLE);
            }
        } else {
            if(factory.getVisibility() == View.VISIBLE) {
                factory.startAnimation(fadeOut);
                factory.setVisibility(View.INVISIBLE);
            }

        }
    }

}