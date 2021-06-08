package com.riobener.audiostudio.Instruments.Controllers;

import com.riobener.audiostudio.Grid.Note;

import nl.igorski.mwengine.core.BaseInstrument;

public abstract class Controller {
    String name;
    boolean onlyPlaying;
    public boolean isOnlyPlaying(){return this.onlyPlaying;}
    public void setOnlyPlaying(boolean state){
        this.onlyPlaying = state;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public abstract void flushInstrument();
    public abstract Note[][] getNoteMap();
    public abstract void updateNoteMap(Note[][] noteMap);
    public abstract void muteInstrument(boolean state);
    public abstract boolean isMuted();
    public abstract void updateEvents();

    public abstract void updateMapMeasures();
}
