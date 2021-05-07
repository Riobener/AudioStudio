package com.riobener.audiostudio.Instruments.Controllers;

import nl.igorski.mwengine.core.RouteableOscillator;
import nl.igorski.mwengine.core.SynthInstrument;

public class SynthController {
    /*SINE,
    TRIANGLE,
    SAWTOOTH,
    SQUARE,
    NOISE,
    PWM,
    KARPLUS_STRONG,
    TABLE*/
    SynthInstrument synth;
    public SynthController(){
        synth = new SynthInstrument();
        synth.setOscillatorAmount(2);
        synth.getOscillatorProperties(0).setWaveform(0);
        synth.getAdsr().setReleaseTime( 0.15f );
        synth.getAudioChannel().setVolume(.7f);
    }

    public SynthInstrument getSynth() {
        return synth;
    }
}
