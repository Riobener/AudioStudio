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

import static nl.igorski.mwengine.MWEngine.SAMPLE_RATE;

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
    RadioButton toggleFilter;

    //LFO View
    TextView lfoLabel;
    CircularSeekBar lfo;
    TextView lfoText;
    RadioButton sineChooser;
    RadioButton trinChooser;
    RadioButton squarChooser;
    RadioButton toggleLFO;

    //ReverbXdelay
    TextView revDelabel;
    CircularSeekBar reverbBar;
    TextView reverbText;
    CircularSeekBar delayBar;
    TextView delayText;
    RadioButton revToggler;

    //phaser
    RadioButton togglePhaser;
    TextView phaserLabel;
    CircularSeekBar rate;
    TextView rateText;
    CircularSeekBar depthBar;
    TextView depthText;
    CircularSeekBar feedbackBar;
    TextView feedbackText;

    //bitcrusher
    TextView bitLabel;
    CircularSeekBar amountBar;
    TextView amountText;
    CircularSeekBar outBar;
    TextView outText;
    RadioButton toggleBitcrusher;

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
        flowLayout.addView(createLFO(context));
        flowLayout.addView(createVolume(context));
        flowLayout.addView(createOSC(context));
        flowLayout.addView(createOSC1(context));
        flowLayout.addView(createFilter(context));
        flowLayout.addView(createReverbDelay(context));
        flowLayout.addView(createPhaser(context));
        flowLayout.addView(createBitCrusher(context));
        synthView.addView(flowLayout);

        return synthView;
    }

    @Override
    public View getView() {
        return synthView;
    }

    @Override
    public void makeViewNull() {
        this.synthController = null;
        this.synthView = null;
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
        octaveBar.setMax(5);

        octaveBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                synthController.setOctave((int)progress,0);
                octaveText.setText((int) progress+"");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
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
        detuneBar.setMax(100f);
        detuneBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                synthController.setDetune(progress-50,0);
                detuneText.setText(progress-50+"");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
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

        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    synthController.setOscillatorWave(0,0);
                    synthController.setDetune(-50,0);
            }
        });

        square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                        synthController.setOscillatorWave(3,0);
                synthController.setDetune(-100,0);

            }
        });
        sawtooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        synthController.setOscillatorWave(2,0);
                synthController.setDetune(100,0);
            }
        });
        triangular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        synthController.setOscillatorWave(1,0);
                synthController.setDetune(50,0);
            }
        });
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

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(toggleButton.getText()=="Off"){
                    synthController.setOscillatorAmount(2);
                    toggleButton.setText("On");
                    toggleButton.setChecked(true);
                }else{
                    synthController.setOscillatorAmount(1);
                    toggleButton.setText("Off");
                    toggleButton.setChecked(false);
                }
            }
        });
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
        octaveBar1.setMax(5);

        octaveBar1.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                if(toggleButton.getText()=="On")
                synthController.setOctave((int)progress,1);
                octaveText1.setText((int) progress+"");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
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
        detuneBar1.setMax(100f);
        detuneBar1.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                if(toggleButton.getText()=="On")
                synthController.setDetune(progress-50,1);
                detuneText1.setText(progress-50+"");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
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


        sin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleButton.getText()=="On"){
                    synthController.setOscillatorWave(0,1);
                }
            }
        });

        square1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleButton.getText()=="On"){
                    if(toggleButton.getText()=="On"){
                        synthController.setOscillatorWave(3,1);
                    }
                }
            }
        });
        sawtooth1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleButton.getText()=="On"){
                    if(toggleButton.getText()=="On"){
                        synthController.setOscillatorWave(2,1);
                    }
                }
            }
        });
        triangular1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleButton.getText()=="On"){
                    if(toggleButton.getText()=="On"){
                        synthController.setOscillatorWave(1,1);
                    }
                }
            }
        });

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
        //toggler
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setLayoutParams(fullWRAP);
        toggleFilter = new RadioButton(context);
        toggleFilter.setText("Off");
        toggleFilter.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toggleFilter.setButtonTintList(ColorStateList.valueOf(instrumentColor));
        }
        toggleFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(toggleFilter.getText()=="Off"){
                    synthController.enableFilter(true);
                    toggleFilter.setText("On");
                    toggleFilter.setChecked(true);
                }else{
                    synthController.enableFilter(false);
                    toggleFilter.setText("Off");
                    toggleFilter.setChecked(false);
                    toggleLFO.setText("Off");
                }
            }
        });
        radioGroup.addView(toggleFilter);
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
        cutoff.setMax(SAMPLE_RATE/8);
        cutoff.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                cutoffText.setText(progress/100f+"");
                synthController.setFilterCutoff(progress);
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
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
        reso.setMax(0.7f);
        reso.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                resoText.setText(progress+"");
                synthController.setFilterReso(progress);
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
        resoText = new TextView(context);
        resoText.setTextSize(TEXT_SIZE);
        resoText.setText("Reso");
        resoText.setAllCaps(false);
        resoText.setGravity(Gravity.CENTER);
        resoText.setTextColor(Color.WHITE);
        resoLayout.addView(reso);
        resoLayout.addView(resoText);

        controllerLayout.addView(radioGroup);
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
    private View createBitCrusher(Context context){
        LinearLayout mainLayout;
        FlowLayout controllerLayout;
        mainLayout = new LinearLayout(context);
        mainLayout.setLayoutParams(fullWRAP);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(7, 7, 7, 7);
        mainLayout.setBackground(createColorShape());

        //label
        bitLabel = new TextView(context);
        bitLabel.setTextSize(LABEL_TEXTSIZE-4);
        bitLabel.setText("Bitcrusher");
        bitLabel.setGravity(Gravity.CENTER);
        bitLabel.setTextColor(Color.WHITE);

        //controller
        controllerLayout = new FlowLayout(context);
        controllerLayout.setGravity(Gravity.FILL);
        controllerLayout.setChildSpacing(CHILD_SPACING);
        controllerLayout.setPadding(8,8,0,8);

        //Amount
        LinearLayout bitLayout = new LinearLayout(context);
        bitLayout.setLayoutParams(fullWRAP);
        bitLayout.setLayoutParams(fullWRAP);
        bitLayout.setOrientation(LinearLayout.VERTICAL);
        bitLayout.setOrientation(LinearLayout.VERTICAL);
        amountBar = new CircularSeekBar(context);
        amountBar.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        amountBar.setRotation(180);
        amountBar.setCircleStyle(Paint.Cap.ROUND);
        amountBar.setCircleStrokeWidth(40);
        amountBar.setCircleProgressColor(instrumentColor);
        amountBar.setPointerColor(instrumentColor);
        amountBar.setMax(1);
        amountBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                amountText.setText(progress+"");
                synthController.setBitCrusherAmount(progress);
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
        amountText = new TextView(context);
        amountText.setTextSize(TEXT_SIZE);
        amountText.setText("Amount");
        amountText.setAllCaps(false);
        amountText.setGravity(Gravity.CENTER);
        amountText.setTextColor(Color.WHITE);
        bitLayout.addView(amountBar);
        bitLayout.addView(amountText);

        //Out
        LinearLayout outLayout = new LinearLayout(context);
        outLayout.setLayoutParams(fullWRAP);
        outLayout.setLayoutParams(fullWRAP);
        outLayout.setOrientation(LinearLayout.VERTICAL);
        outLayout.setOrientation(LinearLayout.VERTICAL);
        outBar = new CircularSeekBar(context);
        outBar.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        outBar.setRotation(180);
        outBar.setCircleStyle(Paint.Cap.ROUND);
        outBar.setCircleStrokeWidth(40);
        outBar.setCircleProgressColor(instrumentColor);
        outBar.setPointerColor(instrumentColor);
        outBar.setMax(16);
        outBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                synthController.setBitCrusherOut(progress);
                outText.setText(progress+"");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
        outText = new TextView(context);
        outText.setTextSize(TEXT_SIZE);
        outText.setText("Out mix");
        outText.setAllCaps(false);
        outText.setGravity(Gravity.CENTER);
        outText.setTextColor(Color.WHITE);
        outLayout.addView(outBar);
        outLayout.addView(outText);
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setLayoutParams(fullWRAP);
        toggleBitcrusher = new RadioButton(context);
        toggleBitcrusher.setText("Off");
        toggleBitcrusher.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toggleBitcrusher.setButtonTintList(ColorStateList.valueOf(instrumentColor));
        }
        toggleBitcrusher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(toggleBitcrusher.getText()=="Off"){
                    synthController.enableBitCrusher(true);
                    toggleBitcrusher.setText("On");
                    toggleBitcrusher.setChecked(true);
                }else{
                    synthController.enableBitCrusher(false);
                    toggleBitcrusher.setText("Off");
                    toggleBitcrusher.setChecked(false);
                }
            }
        });
        radioGroup.addView(toggleBitcrusher);
        controllerLayout.addView(radioGroup);
        controllerLayout.addView(bitLayout);
        controllerLayout.addView(outLayout);



        if (bitLabel.getParent() != null) {
            ((ViewGroup) bitLabel.getParent()).removeView(bitLabel); // <- fix
        }
        /*  controllerLayout.addView(waveGroup);*/
        mainLayout.addView(bitLabel);
        mainLayout.addView(controllerLayout);
        return mainLayout;
    }
    private View createReverbDelay(Context context){
        LinearLayout mainLayout;
        FlowLayout controllerLayout;
        mainLayout = new LinearLayout(context);
        mainLayout.setLayoutParams(fullWRAP);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(7, 7, 7, 7);
        mainLayout.setBackground(createColorShape());

        //label
        revDelabel = new TextView(context);
        revDelabel.setTextSize(LABEL_TEXTSIZE);
        revDelabel.setText("Reverb and Delay");
        revDelabel.setGravity(Gravity.CENTER);
        revDelabel.setTextColor(Color.WHITE);

        //controller
        controllerLayout = new FlowLayout(context);
        controllerLayout.setGravity(Gravity.FILL);
        controllerLayout.setChildSpacing(CHILD_SPACING);
        controllerLayout.setPadding(8,8,0,8);
        //toggle button
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setLayoutParams(fullWRAP);
        revToggler= new RadioButton(context);
        revToggler.setText("Off");
        revToggler.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toggleButton.setButtonTintList(ColorStateList.valueOf(instrumentColor));
        }

        revToggler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(revToggler.getText()=="Off"){
                    synthController.enableReverbDelay(true);
                    revToggler.setText("On");
                    revToggler.setChecked(true);
                }else{
                    synthController.enableReverbDelay(false);
                    revToggler.setText("Off");
                    revToggler.setChecked(false);
                }
            }
        });
        radioGroup.addView(revToggler);
        //Reverb
        LinearLayout reverbLayout = new LinearLayout(context);
        reverbLayout.setLayoutParams(fullWRAP);
        reverbLayout.setLayoutParams(fullWRAP);
        reverbLayout.setOrientation(LinearLayout.VERTICAL);
        reverbLayout.setOrientation(LinearLayout.VERTICAL);
        reverbBar = new CircularSeekBar(context);
        reverbBar.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        reverbBar.setRotation(180);
        reverbBar.setCircleStyle(Paint.Cap.ROUND);
        reverbBar.setCircleStrokeWidth(40);
        reverbBar.setCircleProgressColor(instrumentColor);
        reverbBar.setPointerColor(instrumentColor);
        reverbText = new TextView(context);
        reverbText.setTextSize(TEXT_SIZE);
        reverbText.setText("Reverb");
        reverbText.setAllCaps(false);
        reverbText.setGravity(Gravity.CENTER);
        reverbText.setTextColor(Color.WHITE);
        reverbLayout.addView(reverbBar);
        reverbLayout.addView(reverbText);
        reverbBar.setMax(100);
        reverbBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                reverbText.setText(progress/100f+"");
                synthController.setReverbSize(progress / 100f);
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        //delay
        LinearLayout delayLayout = new LinearLayout(context);
        delayLayout.setLayoutParams(fullWRAP);
        delayLayout.setOrientation(LinearLayout.VERTICAL);
        delayBar = new CircularSeekBar(context);
        delayBar.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        delayBar.setRotation(180);
        delayBar.setCircleStyle(Paint.Cap.ROUND);
        delayBar.setCircleStrokeWidth(40);
        delayBar.setCircleProgressColor(instrumentColor);
        delayBar.setPointerColor(instrumentColor);
        delayText = new TextView(context);
        delayText.setTextSize(TEXT_SIZE);
        delayText.setText("Delay");
        delayText.setAllCaps(false);
        delayText.setGravity(Gravity.CENTER);
        delayText.setTextColor(Color.WHITE);
        delayLayout.addView(delayBar);
        delayLayout.addView(delayText);
        delayBar.setMax(100);
        delayBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                delayText.setText(progress/100f+"");
                synthController.setDelayFeedback(progress / 100f);
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

        controllerLayout.addView(radioGroup);
        controllerLayout.addView(reverbLayout);
        controllerLayout.addView(delayLayout);

        if (revDelabel.getParent() != null) {
            ((ViewGroup) revDelabel.getParent()).removeView(revDelabel); // <- fix
        }
        /*  controllerLayout.addView(waveGroup);*/
        mainLayout.addView(revDelabel);
        mainLayout.addView(controllerLayout);
        return mainLayout;
    }
    private View createPhaser(Context context){
        LinearLayout mainLayout;
        FlowLayout controllerLayout;
        mainLayout = new LinearLayout(context);
        mainLayout.setLayoutParams(fullWRAP);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(7, 7, 7, 7);
        mainLayout.setBackground(createColorShape());

        //label
        phaserLabel = new TextView(context);
        phaserLabel.setTextSize(LABEL_TEXTSIZE);
        phaserLabel.setText("Phaser");
        phaserLabel.setGravity(Gravity.CENTER);
        phaserLabel.setTextColor(Color.WHITE);

        //controller
        controllerLayout = new FlowLayout(context);
        controllerLayout.setGravity(Gravity.FILL);
        controllerLayout.setChildSpacing(CHILD_SPACING);
        controllerLayout.setPadding(8,8,0,8);

        //Rate
        LinearLayout rateLayout = new LinearLayout(context);
        rateLayout.setLayoutParams(fullWRAP);
        rateLayout.setLayoutParams(fullWRAP);
        rateLayout.setOrientation(LinearLayout.VERTICAL);
        rateLayout.setOrientation(LinearLayout.VERTICAL);
        rate = new CircularSeekBar(context);
        rate.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        rate.setRotation(180);
        rate.setCircleStyle(Paint.Cap.ROUND);
        rate.setCircleStrokeWidth(40);
        rate.setCircleProgressColor(instrumentColor);
        rate.setPointerColor(instrumentColor);
        rate.setMax(10);
        rate.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                synthController.setPhaserRate(progress);
                rateText.setText(progress+"");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
        rateText = new TextView(context);
        rateText.setTextSize(TEXT_SIZE);
        rateText.setText("Rate");
        rateText.setAllCaps(false);
        rateText.setGravity(Gravity.CENTER);
        rateText.setTextColor(Color.WHITE);
        rateLayout.addView(rate);
        rateLayout.addView(rateText);

        //depth
        LinearLayout depthLayout = new LinearLayout(context);
        depthLayout.setLayoutParams(fullWRAP);
        depthLayout.setOrientation(LinearLayout.VERTICAL);
        depthBar = new CircularSeekBar(context);
        depthBar.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        depthBar.setRotation(180);
        depthBar.setCircleStyle(Paint.Cap.ROUND);
        depthBar.setCircleStrokeWidth(40);
        depthBar.setCircleProgressColor(instrumentColor);
        depthBar.setPointerColor(instrumentColor);
        depthBar.setMax(1);
        depthBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                synthController.setPhaserDepth(progress);
                depthText.setText(progress+"");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
        depthText = new TextView(context);
        depthText.setTextSize(TEXT_SIZE);
        depthText.setText("Depth");
        depthText.setAllCaps(false);
        depthText.setGravity(Gravity.CENTER);
        depthText.setTextColor(Color.WHITE);
        depthLayout.addView(depthBar);
        depthLayout.addView(depthText);

        //feedback
        LinearLayout feedbackLayout = new LinearLayout(context);
        feedbackLayout.setLayoutParams(fullWRAP);
        feedbackLayout.setOrientation(LinearLayout.VERTICAL);
        feedbackBar = new CircularSeekBar(context);
        feedbackBar.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        feedbackBar.setRotation(180);
        feedbackBar.setCircleStyle(Paint.Cap.ROUND);
        feedbackBar.setCircleStrokeWidth(40);
        feedbackBar.setCircleProgressColor(instrumentColor);
        feedbackBar.setPointerColor(instrumentColor);
        feedbackBar.setMax(1);
        feedbackBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                synthController.setPhaserFeedback(progress);
                feedbackText.setText(progress+"");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
        feedbackText = new TextView(context);
        feedbackText.setTextSize(TEXT_SIZE);
        feedbackText.setText("Feedback");
        feedbackText.setAllCaps(false);
        feedbackText.setGravity(Gravity.CENTER);
        feedbackText.setTextColor(Color.WHITE);
        feedbackLayout.addView(feedbackBar);
        feedbackLayout.addView(feedbackText);

        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setLayoutParams(fullWRAP);
        togglePhaser = new RadioButton(context);
        togglePhaser.setText("Off");
        togglePhaser.setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            togglePhaser.setButtonTintList(ColorStateList.valueOf(instrumentColor));
        }
        togglePhaser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(togglePhaser.getText()=="Off"){
                    synthController.enablePhaser(true);
                    togglePhaser.setText("On");
                    togglePhaser.setChecked(true);
                }else{
                    synthController.enablePhaser(false);
                    togglePhaser.setText("Off");
                    togglePhaser.setChecked(false);
                }
            }
        });
        radioGroup.addView(togglePhaser);
        controllerLayout.addView(radioGroup);
        controllerLayout.addView(rateLayout);
        controllerLayout.addView(depthLayout);
        controllerLayout.addView(feedbackLayout);

        if (phaserLabel.getParent() != null) {
            ((ViewGroup) phaserLabel.getParent()).removeView(phaserLabel); // <- fix
        }
        /*  controllerLayout.addView(waveGroup);*/
        mainLayout.addView(phaserLabel);
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
        sineChooser = new RadioButton(context);
        sineChooser.setText("Sine");
        sineChooser.setTextColor(Color.WHITE);

        toggleLFO = new RadioButton(context);
        toggleLFO.setText("Off");
        toggleLFO.setTextColor(Color.WHITE);

        trinChooser = new RadioButton(context);
        trinChooser.setText("Tringular");
        trinChooser.setTextColor(Color.WHITE);

        radioGroup.addView(toggleLFO);
        radioGroup.addView(sineChooser);
        radioGroup.addView(trinChooser);



        sineChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(squarChooser.isChecked()){
                    synthController.setLFOWave(0);
                }
            }
        });
        trinChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(squarChooser.isChecked()){
                    synthController.setLFOWave(1);
                }
            }
        });

        toggleLFO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleLFO.getText()=="Off"&&toggleFilter.getText()=="On"){
                    synthController.enableLFO(true);
                    toggleLFO.setText("On");
                    toggleLFO.setChecked(true);
                }else if(toggleLFO.getText()=="On"){
                    synthController.enableLFO(false);
                    toggleLFO.setText("Off");
                    toggleLFO.setChecked(false);
                }
            }
        });


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
        lfo.setMax(10);
        lfo.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                synthController.setLFOValue(progress);
               lfoText.setText(progress+"");
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
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
