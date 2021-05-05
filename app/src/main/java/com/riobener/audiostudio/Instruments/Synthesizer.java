package com.riobener.audiostudio.Instruments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.nex3z.flowlayout.FlowLayout;
import com.riobener.audiostudio.R;


// org.apmem.tools.layouts.FlowLayout;

import androidx.core.content.ContextCompat;


import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import me.tankery.lib.circularseekbar.CircularSeekBar;

import nl.igorski.mwengine.core.SynthInstrument;

//The class which creating view for synthesizer programmatically
public class Synthesizer {
    LinearLayout.LayoutParams fullWRAP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams fullMATCH = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams wrapMatch = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT);
    SynthInstrument synth;

    ScrollView synthView;
    FlowLayout flowLayout;
    final int LABEL_TEXTSIZE = 30;

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

    //OSC1 View
    TextView oscLabel;
    CircularSeekBar octaveBar;
    TextView octaveText;
    CircularSeekBar detuneBar;
    TextView detuneText;
    ImageButton sin;
    ImageButton square;
    ImageButton sawtooth;
    ImageButton triangular;

    public Synthesizer() {

        Random rnd = new Random();
        instrumentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        fullWRAP.setMargins(20,20,20,20);
        synth = new SynthInstrument();
    }
    private Drawable getImageForButton(Context context, String name, int width, int height){
        try {
            // get input stream
            InputStream ims = context.getAssets().open(name+".png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            return resize(d,context, width, height);
        }
        catch(IOException ex) {

        }
        return null;
    }
    private Drawable resize(Drawable image, Context context, int width, int height) {
            Bitmap b = ((BitmapDrawable)image).getBitmap();
            Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width,height, false);
            return new BitmapDrawable(context.getResources(), bitmapResized);
    }
    private GradientDrawable createColorShape(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(new float[] { PARAM_PADDING,PARAM_PADDING,PARAM_PADDING,PARAM_PADDING,
                    PARAM_PADDING,PARAM_PADDING,PARAM_PADDING,PARAM_PADDING});
            shape.setColor(Color.BLACK);

            shape.setStroke(10, instrumentColor);
            return shape;
        }
        return null;
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
        flowLayout.addView(createOSC1(context));
        synthView.addView(flowLayout);

        return synthView;
    }
    final float PARAM_PADDING = 80;
    private View createADSR(Context context) {

        LinearLayout adsrLayout;
        FlowLayout controllerLayout;
        adsrLayout = new LinearLayout(context);

        adsrLayout.setLayoutParams(fullWRAP);

        adsrLayout.setOrientation(LinearLayout.VERTICAL);
        adsrLayout.setPadding(7, 7, 7, 7);
        adsrLayout.setBackground(createColorShape());
        //label
        labelADSR = new TextView(context);
        labelADSR.setTextSize(LABEL_TEXTSIZE);
        labelADSR.setText("ADSR");
        labelADSR.setGravity(Gravity.CENTER);
        labelADSR.setTextColor(Color.WHITE);
        adsrLayout.addView(labelADSR);

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

        /*adsrLay.addView(controllerLayout);*/
        if (labelADSR.getParent() != null) {
            ((ViewGroup) labelADSR.getParent()).removeView(labelADSR); // <- fix
        }
        adsrLayout.addView(labelADSR);
        adsrLayout.addView(controllerLayout);
        return adsrLayout;

    }

    private View createOSC1(Context context){
        LinearLayout mainLayout;
        FlowLayout controllerLayout;
        mainLayout = new LinearLayout(context);

        mainLayout.setLayoutParams(fullWRAP);

        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(7, 7, 7, 7);
        mainLayout.setBackground(createColorShape());
        //label
        oscLabel = new TextView(context);
        oscLabel.setTextSize(LABEL_TEXTSIZE);
        oscLabel.setText("Oscillator 1");
        oscLabel.setGravity(Gravity.CENTER);
        oscLabel.setTextColor(Color.WHITE);

        //controller
        controllerLayout = new FlowLayout(context);
        controllerLayout.setGravity(Gravity.FILL);
        controllerLayout.setChildSpacing(30);
        controllerLayout.setPadding(8,8,0,8);

        //octave
        LinearLayout octave = new LinearLayout(context);
        octave.setLayoutParams(fullWRAP);
        octave.setOrientation(LinearLayout.VERTICAL);
        octaveBar = new CircularSeekBar(context);
        octaveBar.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        octaveBar.setRotation(180);
        octaveBar.setCircleStyle(Paint.Cap.ROUND);
        octaveBar.setCircleStrokeWidth(40);
        octaveText = new TextView(context);
        octaveText.setTextSize(20);
        octaveText.setText("Octave");
        octaveText.setAllCaps(false);
        octaveText.setGravity(Gravity.CENTER);
        octaveText.setTextColor(Color.WHITE);
        octave.addView(octaveBar);
        octave.addView(octaveText);
        //detune
        LinearLayout detune = new LinearLayout(context);
        detune.setLayoutParams(fullWRAP);
        detune.setOrientation(LinearLayout.VERTICAL);
        detuneBar = new CircularSeekBar(context);
        detuneBar.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        detuneBar.setRotation(180);
        detuneBar.setCircleStyle(Paint.Cap.ROUND);
        detuneBar.setCircleStrokeWidth(40);
        detuneText = new TextView(context);
        detuneText.setTextSize(20);
        detuneText.setText("Detune");
        detuneText.setAllCaps(false);
        detuneText.setGravity(Gravity.CENTER);
        detuneText.setTextColor(Color.WHITE);
        detune.addView(detuneBar);
        detune.addView(detuneText);
        //buttons
        sin = new ImageButton(context);
        square = new ImageButton(context);
        sawtooth = new ImageButton(context);
        triangular = new ImageButton(context);

        LinearLayout.LayoutParams resize = new LinearLayout.LayoutParams(500,
                500);

        sin.setLayoutParams(fullWRAP);
        sin.setImageDrawable(getImageForButton(context,"sin",octaveBar.getLayoutParams().width,octaveBar.getLayoutParams().height));
        square.setLayoutParams(fullWRAP);
        square.setImageDrawable(getImageForButton(context,"square",octaveBar.getLayoutParams().width,octaveBar.getLayoutParams().height));
        sawtooth.setLayoutParams(fullWRAP);
        sawtooth.setImageDrawable(getImageForButton(context,"sawtooth",octaveBar.getLayoutParams().width,octaveBar.getLayoutParams().height));
        triangular.setLayoutParams(fullWRAP);
        triangular.setImageDrawable(getImageForButton(context,"trin",octaveBar.getLayoutParams().width,octaveBar.getLayoutParams().height));
        controllerLayout.addView(sin);
        controllerLayout.addView(sawtooth);
        controllerLayout.addView(square);
        controllerLayout.addView(triangular);


        controllerLayout.addView(octave);
        controllerLayout.addView(detune);
        if (oscLabel.getParent() != null) {
            ((ViewGroup) oscLabel.getParent()).removeView(oscLabel); // <- fix
        }
      /*  controllerLayout.addView(waveGroup);*/
        mainLayout.addView(oscLabel);
        mainLayout.addView(controllerLayout);
        return mainLayout;
    }
}
