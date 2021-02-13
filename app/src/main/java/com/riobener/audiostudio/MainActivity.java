package com.riobener.audiostudio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        Button playStop = findViewById(R.id.playStop);
        playStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundState){
                    soundState=false;
                    playStop.setText("Stop");
                    tap(true);
                }else{
                    soundState = true;
                    playStop.setText("Start");
                    tap(false);
                }
            }
        });
    }

    boolean soundState = true;
    public native void initialize();
    public native void tap(boolean i);
}