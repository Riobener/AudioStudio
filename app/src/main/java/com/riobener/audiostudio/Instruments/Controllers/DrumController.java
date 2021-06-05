package com.riobener.audiostudio.Instruments.Controllers;

import nl.igorski.mwengine.core.SampledInstrument;

public class DrumController extends Controller {
    SampledInstrument sampler;
    public DrumController(){
        sampler = new SampledInstrument();
    }

    public SampledInstrument getSampler() {
        return sampler;
    }
}
