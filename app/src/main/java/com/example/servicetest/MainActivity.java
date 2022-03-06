package com.example.servicetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    boolean serviceRunning = false;
    private BroadcastReceiver pong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pong = new BroadcastReceiver(){
            public void onReceive (Context context, Intent intent) {
                serviceRunning = true;
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(pong, new IntentFilter("pong"));
        LocalBroadcastManager.getInstance(this).sendBroadcastSync(new Intent("ping"));



        Button start = this.findViewById(R.id.start);
        Button stop = this.findViewById(R.id.stop);

        start.setOnClickListener(this::startClicked);
        stop.setOnClickListener(this::stopClicked);


        if(!serviceRunning){
            start.setVisibility(View.VISIBLE);
            stop.setVisibility(View.GONE);
        } else {
            start.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
        }
    }



    public void startClicked(View v) {
        Button start = this.findViewById(R.id.start);
        Button stop = this.findViewById(R.id.stop);

        start.setVisibility(View.GONE);
        stop.setVisibility(View.VISIBLE);

        Intent serviceIntent = new Intent(this, MyService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }


    }

    public void stopClicked(View v) {
        Button start = this.findViewById(R.id.start);
        Button stop = this.findViewById(R.id.stop);

        start.setVisibility(View.VISIBLE);
        stop.setVisibility(View.GONE);

        Intent serviceIntent = new Intent(this, MyService.class);
        stopService(serviceIntent);

    }
}