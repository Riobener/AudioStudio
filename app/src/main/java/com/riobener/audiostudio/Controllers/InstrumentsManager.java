package com.riobener.audiostudio.Controllers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;

import com.riobener.audiostudio.Instruments.Synthesizer;
import com.riobener.audiostudio.R;

import java.util.ArrayList;

public class InstrumentsManager {

   private ArrayList<Object> instruments = new ArrayList<>();
    public View createSynthView(Context context){
        Synthesizer synth = new Synthesizer();
        instruments.add(synth);
        return synth.createView(context);
    }

    public Object getInstruments(int index) {
        return instruments.get(index);
    }


}
