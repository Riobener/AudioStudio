package com.riobener.audiostudio.Instruments.Views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.riobener.audiostudio.Instruments.Controllers.Controller;
import com.riobener.audiostudio.Instruments.Controllers.DrumController;

import java.util.Random;

import nl.igorski.mwengine.core.BaseInstrument;
import nl.igorski.mwengine.core.SampledInstrument;

//what a shit code here...
public class DrumMachine extends InstrumentView {
    LinearLayout.LayoutParams drumsParams;

    GridLayout padsView;
    int instrumentColor;
    LinearLayout drumsView;
    Button loadDrumPatch;
    DrumController drumController;
    //Drum pads
    Button pad1;
    Button pad2;
    Button pad3;
    Button pad4;
    Button pad5;
    Button pad6;
    Button pad7;
    Button pad8;
    Button pad9;

    public DrumMachine() {
        drumController = new DrumController();
        instrumentType = "Drums";
        Random rnd = new Random();
        instrumentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        drumsParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public String getInstrumentType() {
        return this.instrumentType;
    }

    public View createView(Context context) {
        drumsView = new LinearLayout(context);
        drumsView.setLayoutParams(drumsParams);
        drumsView.setOrientation(LinearLayout.VERTICAL);
        loadDrumPatch = new Button(context);
        loadDrumPatch.setText("Режим редактирования барабанов");
        drumsView.addView(loadDrumPatch);
        drumsView.addView(createDrumPads(context));
        return drumsView;
    }

    @Override
    public BaseInstrument getInstrument() {
        return drumController.getSampler();
    }

    @Override
    public Controller getController() {
        return drumController;
    }

    public View createDrumPads(Context context) {
        padsView = new GridLayout(context);
        padsView.setColumnCount(3);
        padsView.setRowCount(3);
        pad1 = new Button(context);
        pad1.setText("PAD 1");
        pad1.setTextSize(15);
        pad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        pad2 = new Button(context);
        pad2.setText("PAD 2");
        pad2.setTextSize(15);
        pad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        pad3 = new Button(context);
        pad3.setText("PAD 3");
        pad3.setTextSize(15);
        pad3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        pad4 = new Button(context);
        pad4.setText("PAD 4");
        pad4.setTextSize(15);
        pad4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        pad5 = new Button(context);
        pad5.setText("PAD 5");
        pad5.setTextSize(15);
        pad5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        pad6 = new Button(context);
        pad6.setText("PAD 6");
        pad6.setTextSize(15);
        pad6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        pad7 = new Button(context);
        pad7.setText("PAD 7");
        pad7.setTextSize(15);
        pad7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        pad8 = new Button(context);
        pad8.setText("PAD 8");
        pad8.setTextSize(15);
        pad8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        pad9 = new Button(context);
        pad9.setText("PAD 9");
        pad9.setTextSize(15);
        pad9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        padsView.addView(pad1);
        padsView.addView(pad2);
        padsView.addView(pad3);
        padsView.addView(pad4);
        padsView.addView(pad5);
        padsView.addView(pad6);
        padsView.addView(pad7);
        padsView.addView(pad8);
        padsView.addView(pad9);
        return padsView;
    }
}
