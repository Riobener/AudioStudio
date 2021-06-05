package com.riobener.audiostudio.Instruments.Views;

import android.content.Context;
import android.view.View;

import com.riobener.audiostudio.Instruments.Controllers.Controller;

import nl.igorski.mwengine.core.BaseInstrument;
import nl.igorski.mwengine.core.SynthInstrument;

public abstract class InstrumentView {
    String instrumentType;

    public abstract String getInstrumentType();

    public abstract View createView(Context context);
    public abstract BaseInstrument getInstrument();
    public abstract Controller getController();
}
