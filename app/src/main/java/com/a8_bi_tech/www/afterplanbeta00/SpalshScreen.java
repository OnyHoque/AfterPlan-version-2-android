package com.a8_bi_tech.www.afterplanbeta00;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SpalshScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);

        startSplashScreen(200);
    }

    private void startSplashScreen(final int time) {
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(time);
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                }
            }
        };
        myThread.start();
    }
}
