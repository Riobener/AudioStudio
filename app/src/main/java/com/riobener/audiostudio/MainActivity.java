package com.riobener.audiostudio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    RadioGroup radioGroup;
    RadioButton sin;
    RadioButton square;
    SeekBar bpmChanger;
    TextView bpmText;
    int bpm = 120;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWaveType(1);
        radioGroup = findViewById(R.id.waveChoosing);
        sin = new RadioButton(this);
        sin.setText("SIN");
        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWaveType(1);
                square.setChecked(false);
            }
        });
        square = new RadioButton(this);
        square.setText("SQUARE");
        square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWaveType(2);
                sin.setChecked(false);
            }
        });
        radioGroup.addView(square);
        radioGroup.addView(sin);


        bpmChanger = (SeekBar)findViewById(R.id.bpmChanger);
        bpmChanger.setProgress(bpm);
        bpmChanger.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setBpm(progress);
                bpmText.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                bpmText.setText(""+bpmChanger.getProgress());
            }
        });

        bpmText = (TextView)findViewById(R.id.bpmText);
        bpmText.setText(""+bpm);



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
    public native void setWaveType(int i);//1-sine, 2 - square
    public native void setBpm(int bpm);
}