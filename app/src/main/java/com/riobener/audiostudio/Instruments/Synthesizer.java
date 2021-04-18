package com.riobener.audiostudio.Instruments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.riobener.audiostudio.R;


import org.apmem.tools.layouts.FlowLayout;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import me.tankery.lib.circularseekbar.CircularSeekBar;
import nl.igorski.mwengine.core.ADSR;
import nl.igorski.mwengine.core.LFO;
import nl.igorski.mwengine.core.OscillatorProperties;
import nl.igorski.mwengine.core.RouteableOscillator;
import nl.igorski.mwengine.core.SynthInstrument;
import nl.igorski.mwengine.core.WaveForms;

public class Synthesizer {
    final ViewGroup.LayoutParams fullWRAP = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    final ViewGroup.LayoutParams fullMATCH = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
    SynthInstrument synth;

    ScrollView synthView;
    FlowLayout flowLayout;

    //ADSR View

    CircularSeekBar attack;
    CircularSeekBar  decay;
    CircularSeekBar  release;
    TextView attackText;
    TextView decayText;
    TextView releaseText;
    TextView labelADSR;


    public Synthesizer(){
        synth = new SynthInstrument();
        synth.setOscillatorAmount(3);
        synth.getOscillatorProperties(0).setWaveform(2);
        synth.getOscillatorProperties(1).setWaveform(2);
        synth.getOscillatorProperties(2).setWaveform(5);
    }
    public View createView(Context context){
        synthView = new ScrollView(context);

        int backgroundColor = ContextCompat.getColor(context, R.color.backgroundColor);
        synthView.setBackgroundColor(backgroundColor);
        synthView.setLayoutParams(fullMATCH);
        flowLayout = new FlowLayout(context);
        flowLayout.setOrientation(FlowLayout.HORIZONTAL);
        flowLayout.setGravity(Gravity.FILL);
        flowLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        flowLayout.addView(createADSR(context));
        synthView.addView(flowLayout);
        return synthView;
    }

    public View createADSR(Context context){
        LinearLayout adsrLayout;
        LinearLayout adsrLay;
        LinearLayout controllerLayout;
        adsrLayout = new LinearLayout(context);

        adsrLayout.setLayoutParams(fullWRAP);
        adsrLayout.setOrientation(LinearLayout.VERTICAL);
        adsrLayout.setPadding(7,7,7,7);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            adsrLayout.setBackground(context.getDrawable(R.drawable.instrument_attributes_back));
        }
        //label
        labelADSR = new TextView(adsrLayout.getContext());
        labelADSR.setTextSize(20);
        labelADSR.setText("ADSR");
        labelADSR.setGravity(Gravity.CENTER);
        labelADSR.setTextColor(Color.WHITE);
        adsrLayout.addView(labelADSR);

        //controllers Layout
        adsrLay = new LinearLayout(context);
        adsrLay.setLayoutParams(fullWRAP);
        adsrLay.setOrientation(LinearLayout.HORIZONTAL);


        //controllers
        controllerLayout = new LinearLayout(context);
        controllerLayout.setLayoutParams(fullWRAP);
        controllerLayout.setOrientation(LinearLayout.VERTICAL);

        attack = new CircularSeekBar(context);
        attack.setLayoutParams(new ViewGroup.LayoutParams(200,200));
        attack.setRotation(180);
        attack.setCircleStyle(Paint.Cap.ROUND);
        attack.setCircleStrokeWidth(40);


        attackText = new TextView(context);
        attackText.setTextSize(20);
        attackText.setText("Attack");
        attackText.setAllCaps(false);
        attackText.setGravity(Gravity.CENTER);
        attackText.setTextColor(Color.WHITE);

        controllerLayout.setPadding(20,20,20,20);
        controllerLayout.addView(attack);
        controllerLayout.addView(attackText);

        adsrLay.addView(controllerLayout);
        if(labelADSR.getParent() !=null){
            ((ViewGroup)labelADSR.getParent()).removeView(labelADSR); // <- fix
        }
        adsrLayout.addView(labelADSR);
        adsrLayout.addView(adsrLay);
        return adsrLayout;

    }
/*
 <com.triggertrap.seekarc.SeekArc
    android:id="@+id/seekArc"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    android:padding="30dp"
    seekarc:rotation="180"
    seekarc:startAngle="30"
    seekarc:sweepAngle="300"
    seekarc:touchInside="true" />*/



}
