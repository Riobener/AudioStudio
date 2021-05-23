package com.riobener.audiostudio;

import android.content.Context;
import android.view.View;

import com.riobener.audiostudio.Instruments.Views.SynthesizerView;

import java.util.ArrayList;

import nl.igorski.mwengine.core.SynthInstrument;

public class InstrumentsManager {

   private ArrayList<SynthesizerView> instruments = new ArrayList<>();
    public View createSynthView(Context context){
        SynthesizerView synth = new SynthesizerView();
        instruments.add(synth);
        return synth.createView(context);
    }

    public SynthInstrument getInstruments(int index) {
        return instruments.get(index).getSynth();
    }


    public int size() {
        return instruments.size();
    }
}
