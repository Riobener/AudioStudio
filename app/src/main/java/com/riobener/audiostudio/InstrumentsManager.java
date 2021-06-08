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
        InstrumentView instrument = new SynthesizerView();
        instruments.add(instrument);

        return instrument.createView(context);
    }
    public ArrayList<InstrumentView> getInstruments(){
        return instruments;
    }
    public void removeInstrument(int index){
        instruments.get(index).getController().flushInstrument();
        instruments.get(index).makeViewNull();
        instruments.remove(index);
    }
    public View createDrumMachine(Context context, int width, int height){
        InstrumentView drums = new DrumMachine(width, height);
        instruments.add(drums);
        return drums.createView(context);
    }


    public BaseInstrument getInstrument(int index) {
        return instruments.get(index).getInstrument();
    }
    public View getInstrumentView(int index){
        return instruments.get(index).getView();
    }
    public String getInstrumentType(int index){
        return instruments.get(index).getInstrumentType();
    }
    public Controller getController(int index){
        return instruments.get(index).getController();
    }

    public int size() {
        return instruments.size();
    }
}
