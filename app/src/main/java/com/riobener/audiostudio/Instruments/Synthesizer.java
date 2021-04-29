package com.riobener.audiostudio.Instruments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.nex3z.flowlayout.FlowLayout;
import com.riobener.audiostudio.R;


// org.apmem.tools.layouts.FlowLayout;

import androidx.core.content.ContextCompat;


import java.util.Random;

import me.tankery.lib.circularseekbar.CircularSeekBar;

import nl.igorski.mwengine.core.SynthInstrument;


public class Synthesizer {
    LinearLayout.LayoutParams fullWRAP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams fullMATCH = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT);
    SynthInstrument synth;

    ScrollView synthView;
    FlowLayout flowLayout;

    int instrumentColor;

    //ADSR View
    CircularSeekBar attack;
    CircularSeekBar decay;
    CircularSeekBar release;
    CircularSeekBar sustain;
    TextView attackText;
    TextView decayText;
    TextView releaseText;
    TextView sustainText;
    TextView labelADSR;


    public Synthesizer() {
        Random rnd = new Random();
        instrumentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        fullWRAP.setMargins(20,20,20,20);
        synth = new SynthInstrument();
        synth.setOscillatorAmount(3);
        synth.getOscillatorProperties(0).setWaveform(2);
        synth.getOscillatorProperties(1).setWaveform(2);
        synth.getOscillatorProperties(2).setWaveform(5);
    }

    public View createView(Context context) {
        synthView = new ScrollView(context);

        int backgroundColor = ContextCompat.getColor(context, R.color.backgroundColor);
        synthView.setBackgroundColor(backgroundColor);
        synthView.setLayoutParams(fullWRAP);
        flowLayout = new FlowLayout(context);
        flowLayout.setRowSpacing(4);
        flowLayout.setChildSpacing(4);
        flowLayout.setRtl(false);
        flowLayout.addView(createADSR(context));
        flowLayout.addView(createADSR(context));
        flowLayout.addView(createADSR(context));
        synthView.addView(flowLayout);
        return synthView;
    }
    final float PARAM_PADDING = 80;
    public View createADSR(Context context) {

        LinearLayout adsrLayout;
        LinearLayout adsrLay;
        FlowLayout controllerLayout;
        adsrLayout = new LinearLayout(context);

        adsrLayout.setLayoutParams(fullWRAP);

        adsrLayout.setOrientation(LinearLayout.VERTICAL);
        adsrLayout.setPadding(7, 7, 7, 7);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(new float[] { PARAM_PADDING,PARAM_PADDING,PARAM_PADDING,PARAM_PADDING,
                    PARAM_PADDING,PARAM_PADDING,PARAM_PADDING,PARAM_PADDING});
            shape.setColor(Color.BLACK);

            shape.setStroke(5, instrumentColor);
            adsrLayout.setBackground(shape);
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
        adsrLay.setOrientation(LinearLayout.VERTICAL);


        //controllers
        controllerLayout = new FlowLayout(context);
        controllerLayout.setGravity(Gravity.FILL);
        controllerLayout.setChildSpacing(30);

        //attack
        LinearLayout atck = new LinearLayout(context);
        atck.setLayoutParams(fullWRAP);
        atck.setOrientation(LinearLayout.VERTICAL);
        attack = new CircularSeekBar(context);
        attack.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        attack.setRotation(180);
        attack.setCircleStyle(Paint.Cap.ROUND);
        attack.setCircleStrokeWidth(40);
        attackText = new TextView(context);
        attackText.setTextSize(20);
        attackText.setText("Attack");
        attackText.setAllCaps(false);
        attackText.setGravity(Gravity.CENTER);
        attackText.setTextColor(Color.WHITE);
        atck.addView(attack);
        atck.addView(attackText);
        controllerLayout.addView(atck);

        //decay
        LinearLayout dec = new LinearLayout(context);
        dec.setLayoutParams(fullWRAP);
        dec.setOrientation(LinearLayout.VERTICAL);
        decay = new CircularSeekBar(context);
        decay.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        decay.setRotation(180);
        decay.setCircleStyle(Paint.Cap.ROUND);
        decay.setCircleStrokeWidth(40);
        decayText = new TextView(context);
        decayText.setTextSize(20);
        decayText.setText("Decay");
        decayText.setAllCaps(false);
        decayText.setGravity(Gravity.CENTER);
        decayText.setTextColor(Color.WHITE);
        dec.addView(decay);
        dec.addView(decayText);
        controllerLayout.addView(dec);

        //sustain
        LinearLayout sust = new LinearLayout(context);
        sust.setLayoutParams(fullWRAP);
        sust.setOrientation(LinearLayout.VERTICAL);
        sustain = new CircularSeekBar(context);
        sustain.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        sustain.setRotation(180);
        sustain.setCircleStyle(Paint.Cap.ROUND);
        sustain.setCircleStrokeWidth(40);
        sustainText = new TextView(context);
        sustainText.setTextSize(20);
        sustainText.setText("Sustain");
        sustainText.setAllCaps(false);
        sustainText.setGravity(Gravity.CENTER);
        sustainText.setTextColor(Color.WHITE);
        sust.addView(sustain);
        sust.addView(sustainText);
        controllerLayout.addView(sust);

        //release
        LinearLayout rel = new LinearLayout(context);
        rel.setLayoutParams(fullWRAP);
        rel.setOrientation(LinearLayout.VERTICAL);
        release = new CircularSeekBar(context);
        release.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        release.setRotation(180);
        release.setCircleStyle(Paint.Cap.ROUND);
        release.setCircleStrokeWidth(40);
        releaseText = new TextView(context);
        releaseText.setTextSize(20);
        releaseText.setText("Release");
        releaseText.setAllCaps(false);
        releaseText.setGravity(Gravity.CENTER);
        releaseText.setTextColor(Color.WHITE);
        rel.addView(release);
        rel.addView(releaseText);
        controllerLayout.addView(rel);

        controllerLayout.setPadding(10, 10, 10, 10);

        adsrLay.addView(controllerLayout);
        if (labelADSR.getParent() != null) {
            ((ViewGroup) labelADSR.getParent()).removeView(labelADSR); // <- fix
        }
        adsrLayout.addView(labelADSR);
        adsrLayout.addView(adsrLay);
        return adsrLayout;

    }
}
