package com.riobener.audiostudio.Instruments.Views;


import android.content.Context;
import android.content.res.ColorStateList;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;


import com.nex3z.flowlayout.FlowLayout;
import com.riobener.audiostudio.Instruments.Controllers.Controller;
import com.riobener.audiostudio.Instruments.Controllers.SynthController;
import com.riobener.audiostudio.R;


// org.apmem.tools.layouts.FlowLayout;

import androidx.core.content.ContextCompat;


import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import me.tankery.lib.circularseekbar.CircularSeekBar;

import nl.igorski.mwengine.core.SynthInstrument;

//The class which creating view for synthesizer programmatically
public class SynthesizerView extends InstrumentView {
    LinearLayout.LayoutParams fullWRAP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams fullMATCH = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams wrapMatch = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT);

    ScrollView synthView;
    FlowLayout flowLayout;
    final int LABEL_TEXTSIZE = 25;
    int TEXT_SIZE = 15;
    int CHILD_SPACING = 25;
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

    //OSC2 View
    TextView oscLabel1;
    CircularSeekBar octaveBar1;
    TextView octaveText1;
    CircularSeekBar detuneBar1;
    TextView detuneText1;
    ImageButton sin1;
    ImageButton square1;
    ImageButton sawtooth1;
    ImageButton triangular1;
    RadioButton toggleButton;

    //Volume View
    TextView volumeLabel;
    CircularSeekBar volume;
    TextView volumeText;

    //Filter View
    TextView filterLabel;
    CircularSeekBar cutoff;
    TextView cutoffText;
    CircularSeekBar reso;
    TextView resoText;

    //LFO View
    TextView lfoLabel;
    CircularSeekBar lfo;
    TextView lfoText;
    RadioButton waveChooser;

    //TODO
    //Arpeggiator View
    CircularSeekBar step1;
    CircularSeekBar step2;
    CircularSeekBar step3;
    CircularSeekBar step4;
    CircularSeekBar step5;
    CircularSeekBar step6;
    CircularSeekBar step7;
    CircularSeekBar step8;
    TextView step1Text;
    TextView step2Text;
    TextView step3Text;
    TextView step4Text;
    TextView step5Text;
    TextView step6Text;
    TextView step7Text;
    TextView step8Text;
    TextView arpeggioLabel;

    SynthController synthController;

    public SynthesizerView() {
        instrumentType = "Synth";
        synthController = new SynthController();
        Random rnd = new Random();
        instrumentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        fullWRAP.setMargins(20,20,20,20);
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
            Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width-45,height-45, false);
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

    @Override
    public String getInstrumentType() {
        return this.instrumentType;
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
        flowLayout.addView(createVolume(context));
        flowLayout.addView(createOSC(context));
        flowLayout.addView(createOSC1(context));
        flowLayout.addView(createFilter(context));
        flowLayout.addView(createLFO(context));
        synthView.addView(flowLayout);

        return synthView;
    }

    @Override
    public SynthInstrument getInstrument() {
        return synthController.getSynth();
    }

    @Override
    public Controller getController() {
        return synthController;
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
        controllerLayout.setChildSpacing(CHILD_SPACING);

        //attack
        LinearLayout atck = new LinearLayout(context);
        atck.setLayoutParams(fullWRAP);
        atck.setOrientation(LinearLayout.VERTICAL);
        attack = new CircularSeekBar(context);
        attack.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        attack.setRotation(180);
        attack.setCircleStyle(Paint.Cap.ROUND);
        attack.setCircleStrokeWidth(40);
        attack.setCircleProgressColor(instrumentColor);
        attack.setPointerColor(instrumentColor);
        attack.setMax(1f);
        attack.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                attackText.setText(progress+"");
                synthController.setAttack(progress);
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
        attackText = new TextView(context);
        attackText.setTextSize(TEXT_SIZE);
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
        decay.setCircleProgressColor(instrumentColor);
        decay.setPointerColor(instrumentColor);
        decay.setMax(1f);
        decay.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                synthController.setDecay(progress);
                decayText.setText(progress+"");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
        decayText = new TextView(context);
        decayText.setTextSize(TEXT_SIZE);
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
        sustain.setCircleProgressColor(instrumentColor);
        sustain.setPointerColor(instrumentColor);
        sustain.setMax(1f);
        sustain.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                synthController.setSustain(progress);
                sustainText.setText(progress+"");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
        sustainText = new TextView(context);
        sustainText.setTextSize(TEXT_SIZE);
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
        release.setCircleProgressColor(instrumentColor);
        release.setPointerColor(instrumentColor);
        release.setMax(1f);
        release.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                synthController.setRelease(progress);
                releaseText.setText(progress+"");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
        releaseText = new TextView(context);
        releaseText.setTextSize(TEXT_SIZE);
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

    private View createOSC(Context context){
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
        controllerLayout.setChildSpacing(CHILD_SPACING);
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
        octaveBar.setCircleProgressColor(instrumentColor);
        octaveBar.setPointerColor(instrumentColor);
        octaveText = new TextView(context);
        octaveText.setTextSize(TEXT_SIZE);
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
        detuneBar.setCircleProgressColor(instrumentColor);
        detuneBar.setPointerColor(instrumentColor);
        detuneText = new TextView(context);
        detuneText.setTextSize(TEXT_SIZE);
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
    private View createOSC1(Context context){
        LinearLayout mainLayout;
        FlowLayout controllerLayout;
        mainLayout = new LinearLayout(context);

        mainLayout.setLayoutParams(fullWRAP);

        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(7, 7, 7, 7);
        mainLayout.setBackground(createColorShape());
        //label
        oscLabel1 = new TextView(context);
        oscLabel1.setTextSize(LABEL_TEXTSIZE);
        oscLabel1.setText("Oscillator 2");
        oscLabel1.setGravity(Gravity.CENTER);
        oscLabel1.setTextColor(Color.WHITE);

        //controller
        controllerLayout = new FlowLayout(context);
        controllerLayout.setGravity(Gravity.FILL);
        controllerLayout.setChildSpacing(CHILD_SPACING);
        controllerLayout.setPadding(3,3,3,3);

        //toggle button
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setLayoutParams(fullWRAP);
        toggleButton = new RadioButton(context);
        toggleButton.setText("Off");
        toggleButton.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toggleButton.setButtonTintList(ColorStateList.valueOf(instrumentColor));
        }

        radioGroup.addView(toggleButton);

        //octave
        LinearLayout octave = new LinearLayout(context);
        octave.setLayoutParams(fullWRAP);
        octave.setOrientation(LinearLayout.VERTICAL);
        octaveBar1 = new CircularSeekBar(context);
        octaveBar1.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        octaveBar1.setRotation(180);
        octaveBar1.setCircleStyle(Paint.Cap.ROUND);
        octaveBar1.setCircleStrokeWidth(40);
        octaveBar1.setCircleProgressColor(instrumentColor);
        octaveBar1.setPointerColor(instrumentColor);
        octaveText1 = new TextView(context);
        octaveText1.setTextSize(TEXT_SIZE);
        octaveText1.setText("Octave");
        octaveText1.setAllCaps(false);
        octaveText1.setGravity(Gravity.CENTER);
        octaveText1.setTextColor(Color.WHITE);
        octave.addView(octaveBar1);
        octave.addView(octaveText1);
        //detune
        LinearLayout detune = new LinearLayout(context);
        detune.setLayoutParams(fullWRAP);
        detune.setOrientation(LinearLayout.VERTICAL);
        detuneBar1 = new CircularSeekBar(context);
        detuneBar1.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        detuneBar1.setRotation(180);
        detuneBar1.setCircleStyle(Paint.Cap.ROUND);
        detuneBar1.setCircleStrokeWidth(40);
        detuneBar1.setCircleProgressColor(instrumentColor);
        detuneBar1.setPointerColor(instrumentColor);
        detuneText1 = new TextView(context);
        detuneText1.setTextSize(TEXT_SIZE);
        detuneText1.setText("Detune");
        detuneText1.setAllCaps(false);
        detuneText1.setGravity(Gravity.CENTER);
        detuneText1.setTextColor(Color.WHITE);
        detune.addView(detuneBar1);
        detune.addView(detuneText1);
        //buttons
        sin1 = new ImageButton(context);
        square1 = new ImageButton(context);
        sawtooth1 = new ImageButton(context);
        triangular1 = new ImageButton(context);

        LinearLayout.LayoutParams resize = new LinearLayout.LayoutParams(500,
                500);
        //wave buttons
        sin1.setLayoutParams(fullWRAP);
        sin1.setImageDrawable(getImageForButton(context,"sin",octaveBar1.getLayoutParams().width,octaveBar1.getLayoutParams().height));
        square1.setLayoutParams(fullWRAP);
        square1.setImageDrawable(getImageForButton(context,"square",octaveBar1.getLayoutParams().width,octaveBar1.getLayoutParams().height));
        sawtooth1.setLayoutParams(fullWRAP);
        sawtooth1.setImageDrawable(getImageForButton(context,"sawtooth",octaveBar1.getLayoutParams().width,octaveBar1.getLayoutParams().height));
        triangular1.setLayoutParams(fullWRAP);
        triangular1.setImageDrawable(getImageForButton(context,"trin",octaveBar1.getLayoutParams().width,octaveBar1.getLayoutParams().height));

        //add views
        controllerLayout.addView(radioGroup);
        controllerLayout.addView(sin1);
        controllerLayout.addView(sawtooth1);
        controllerLayout.addView(square1);
        controllerLayout.addView(triangular1);


        controllerLayout.addView(octave);
        controllerLayout.addView(detune);
        if (oscLabel1.getParent() != null) {
            ((ViewGroup) oscLabel1.getParent()).removeView(oscLabel1); // <- fix
        }
        /*  controllerLayout.addView(waveGroup);*/
        mainLayout.addView(oscLabel1);
        mainLayout.addView(controllerLayout);
        return mainLayout;
    }
    private View createVolume(Context context){
        LinearLayout mainLayout;
        FlowLayout controllerLayout;
        mainLayout = new LinearLayout(context);
        mainLayout.setLayoutParams(fullWRAP);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(7, 7, 7, 7);
        mainLayout.setBackground(createColorShape());
        //label
        /*volumeLabel = new TextView(context);
        volumeLabel.setTextSize(LABEL_TEXTSIZE);
        volumeLabel.setText("Volume");
        volumeLabel.setGravity(Gravity.CENTER);
        volumeLabel.setTextColor(Color.WHITE);
        volumeLabel.setPadding(30,30,30, 10);*/

        //controller
        controllerLayout = new FlowLayout(context);
        controllerLayout.setGravity(Gravity.CENTER);
        controllerLayout.setChildSpacing(CHILD_SPACING);
        controllerLayout.setPadding(8,8,0,8);

        //volume
        LinearLayout volumeLayout = new LinearLayout(context);
        volumeLayout.setLayoutParams(fullWRAP);
        volumeLayout.setOrientation(LinearLayout.VERTICAL);
        volume = new CircularSeekBar(context);
        volume.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        volume.setRotation(180);
        volume.setCircleStyle(Paint.Cap.ROUND);
        volume.setCircleStrokeWidth(40);
        volume.setCircleProgressColor(instrumentColor);
        volume.setPointerColor(instrumentColor);
        volume.setMax(10f);
        volume.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                volumeText.setText(progress+"");
                synthController.setVolume(progress);
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
        volumeText = new TextView(context);
        volumeText.setTextSize(TEXT_SIZE);
        volumeText.setText("Volume");
        volumeText.setAllCaps(false);
        volumeText.setGravity(Gravity.CENTER);
        volumeText.setTextColor(Color.WHITE);
        volumeLayout.addView(volume);
        volumeLayout.addView(volumeText);
        controllerLayout.addView(volumeLayout);
        /*if (volumeLabel.getParent() != null) {
            ((ViewGroup) volumeLabel.getParent()).removeView(volumeLabel); // <- fix
        }*/
        /*mainLayout.addView(volumeLabel);*/

        mainLayout.addView(controllerLayout);
        return mainLayout;
    }
    private View createFilter(Context context){

        LinearLayout mainLayout;
        FlowLayout controllerLayout;
        mainLayout = new LinearLayout(context);
        mainLayout.setLayoutParams(fullWRAP);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(7, 7, 7, 7);
        mainLayout.setBackground(createColorShape());

        //label
        filterLabel = new TextView(context);
        filterLabel.setTextSize(LABEL_TEXTSIZE);
        filterLabel.setText("Filter");
        filterLabel.setGravity(Gravity.CENTER);
        filterLabel.setTextColor(Color.WHITE);

        //controller
        controllerLayout = new FlowLayout(context);
        controllerLayout.setGravity(Gravity.FILL);
        controllerLayout.setChildSpacing(CHILD_SPACING);
        controllerLayout.setPadding(8,8,0,8);

        //cutoff
        LinearLayout cutoffLayout = new LinearLayout(context);
        cutoffLayout.setLayoutParams(fullWRAP);
        cutoffLayout.setOrientation(LinearLayout.VERTICAL);
        cutoff = new CircularSeekBar(context);
        cutoff.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        cutoff.setRotation(180);
        cutoff.setCircleStyle(Paint.Cap.ROUND);
        cutoff.setCircleStrokeWidth(40);
        cutoff.setCircleProgressColor(instrumentColor);
        cutoff.setPointerColor(instrumentColor);
        cutoffText = new TextView(context);
        cutoffText.setTextSize(TEXT_SIZE);
        cutoffText.setText("Cutoff");
        cutoffText.setAllCaps(false);
        cutoffText.setGravity(Gravity.CENTER);
        cutoffText.setTextColor(Color.WHITE);
        cutoffLayout.addView(cutoff);
        cutoffLayout.addView(cutoffText);
        //res
        LinearLayout resoLayout = new LinearLayout(context);
        resoLayout.setLayoutParams(fullWRAP);
        resoLayout.setOrientation(LinearLayout.VERTICAL);
        reso = new CircularSeekBar(context);
        reso.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        reso.setRotation(180);
        reso.setCircleStyle(Paint.Cap.ROUND);
        reso.setCircleStrokeWidth(40);
        reso.setCircleProgressColor(instrumentColor);
        reso.setPointerColor(instrumentColor);
        resoText = new TextView(context);
        resoText.setTextSize(TEXT_SIZE);
        resoText.setText("Reso");
        resoText.setAllCaps(false);
        resoText.setGravity(Gravity.CENTER);
        resoText.setTextColor(Color.WHITE);
        resoLayout.addView(reso);
        resoLayout.addView(resoText);

        controllerLayout.addView(cutoffLayout);
        controllerLayout.addView(resoLayout);

        if (filterLabel.getParent() != null) {
            ((ViewGroup) filterLabel.getParent()).removeView(filterLabel); // <- fix
        }
        /*  controllerLayout.addView(waveGroup);*/
        mainLayout.addView(filterLabel);
        mainLayout.addView(controllerLayout);
        return mainLayout;
    }
    private View createLFO(Context context){
        LinearLayout mainLayout;
        FlowLayout controllerLayout;
        mainLayout = new LinearLayout(context);
        mainLayout.setLayoutParams(fullWRAP);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(7, 7, 7, 7);
        mainLayout.setBackground(createColorShape());
        //label
        lfoLabel = new TextView(context);
        lfoLabel.setTextSize(LABEL_TEXTSIZE);
        lfoLabel.setText("LFO");
        lfoLabel.setGravity(Gravity.CENTER);
        lfoLabel.setTextColor(Color.WHITE);
        lfoLabel.setPadding(30,30,30, 10);

        //controller
        controllerLayout = new FlowLayout(context);
        controllerLayout.setGravity(Gravity.CENTER);
        controllerLayout.setChildSpacing(CHILD_SPACING);
        controllerLayout.setPadding(8,8,0,8);

        //wave chooser
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setLayoutParams(fullWRAP);
        waveChooser = new RadioButton(context);
        waveChooser.setText("Sine");
        waveChooser.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            waveChooser.setButtonTintList(ColorStateList.valueOf(instrumentColor));
        }
        radioGroup.addView(waveChooser);

        //volume
        LinearLayout lfoLayout = new LinearLayout(context);
        lfoLayout.setLayoutParams(fullWRAP);
        lfoLayout.setOrientation(LinearLayout.VERTICAL);
        lfo = new CircularSeekBar(context);
        lfo.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        lfo.setRotation(180);
        lfo.setCircleStyle(Paint.Cap.ROUND);
        lfo.setCircleStrokeWidth(40);
        lfo.setCircleProgressColor(instrumentColor);
        lfo.setPointerColor(instrumentColor);
        lfoText = new TextView(context);
        lfoText.setTextSize(TEXT_SIZE);
        lfoText.setText("1");
        lfoText.setAllCaps(false);
        lfoText.setGravity(Gravity.CENTER);
        lfoText.setTextColor(Color.WHITE);
        lfoLayout.addView(lfo);
        lfoLayout.addView(lfoText);

        controllerLayout.addView(radioGroup);
        controllerLayout.addView(lfoLayout);

        if (lfoLabel.getParent() != null) {
            ((ViewGroup) lfoLabel.getParent()).removeView(lfoLabel); // <- fix
        }
        mainLayout.addView(lfoLabel);
        mainLayout.addView(controllerLayout);
        return mainLayout;
    }
}
