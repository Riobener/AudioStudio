package com.riobener.audiostudio;

import android.content.Context;
import android.view.View;

import com.riobener.audiostudio.Instruments.Controllers.SynthController;
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

    public SynthInstrument getInstrument(int index) {
        return instruments.get(index).getSynth();
    }
    public SynthController getController(int index){
        return instruments.get(index).getSynthController();
    }

    public int size() {
        return instruments.size();
    }
}
