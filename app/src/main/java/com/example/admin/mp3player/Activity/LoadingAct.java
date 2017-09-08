package com.example.admin.mp3player.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.admin.mp3player.R;

/**
 * Created by VuMinhDuc on 17/05/14.
 */

public class LoadingAct extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullAct();
        setContentView(R.layout.loading_act);
        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(1);  //Delay of 10 seconds
                } catch (Exception e) {

                } finally {

                    Intent i = new Intent(LoadingAct.this,
                            MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }

    private void fullAct() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
