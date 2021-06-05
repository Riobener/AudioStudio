package com.riobener.audiostudio.Instruments.Views;

import android.graphics.Color;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Random;

public class DrumMachine {
    LinearLayout.LayoutParams fullWRAP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    int instrumentColor;
    //Drum pads
    Button[] pads;
    public DrumMachine(){
        Random rnd = new Random();
        instrumentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
