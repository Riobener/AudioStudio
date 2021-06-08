package com.riobener.audiostudio.Instruments.Controllers;

import com.riobener.audiostudio.Grid.Note;

public abstract class Controller {
    String name;
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public abstract void flushInstrument();
    public abstract Note[][] getNoteMap();
    public abstract void updateNoteMap(Note[][] noteMap);

    public abstract void updateEvents();

    public abstract void updateMapMeasures();
}
