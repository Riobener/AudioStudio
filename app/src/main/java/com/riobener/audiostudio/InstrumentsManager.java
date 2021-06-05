package com.riobener.audiostudio;

import android.content.Context;
import android.view.View;

import com.riobener.audiostudio.Instruments.Controllers.Controller;
import com.riobener.audiostudio.Instruments.Controllers.SynthController;
import com.riobener.audiostudio.Instruments.Views.DrumMachine;
import com.riobener.audiostudio.Instruments.Views.InstrumentView;
import com.riobener.audiostudio.Instruments.Views.SynthesizerView;

import java.util.ArrayList;

import nl.igorski.mwengine.core.BaseInstrument;
import nl.igorski.mwengine.core.SynthInstrument;

public class InstrumentsManager {

   private ArrayList<InstrumentView> instruments = new ArrayList<>();
    public View createSynthView(Context context){
        InstrumentView synth = new SynthesizerView();
        instruments.add(synth);
        return synth.createView(context);
    }
    public View createDrumMachine(Context context){
        InstrumentView drums = new DrumMachine();
        instruments.add(drums);
        return drums.createView(context);
    }
    public BaseInstrument getInstrument(int index) {
        return instruments.get(index).getInstrument();

    }
    public Controller getController(int index){
        return instruments.get(index).getController();
    }

    public int size() {
        return instruments.size();
    }
}
