package com.riobener.audiostudio.Instruments.Controllers;

import com.riobener.audiostudio.Grid.Note;

public abstract class Controller {
    public abstract Note[][] getNoteMap();
    public abstract void updateNoteMap(Note[][] noteMap);

    public abstract void updateEvents();

    public abstract void updateMapMeasures();
}
