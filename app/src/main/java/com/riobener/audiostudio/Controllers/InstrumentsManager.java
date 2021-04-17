package com.riobener.audiostudio.Controllers;

import com.riobener.audiostudio.Instruments.Synthesizer;

import java.util.ArrayList;

public class InstrumentsManager {

    ArrayList<Object> instruments = new ArrayList<>();
    public InstrumentsManager(){
        instruments.add(new Synthesizer());
    }
    public int getCount() {
        return instruments.size();
    }

    public void addInstrument() {
        instruments.add(new Synthesizer());
    }
}
