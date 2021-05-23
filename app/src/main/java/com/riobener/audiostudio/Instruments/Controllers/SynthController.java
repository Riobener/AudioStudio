package com.riobener.audiostudio.Instruments.Controllers;

import nl.igorski.mwengine.core.Filter;
import nl.igorski.mwengine.core.Phaser;
import nl.igorski.mwengine.core.RouteableOscillator;
import nl.igorski.mwengine.core.SynthInstrument;
import nl.igorski.mwengine.core.WaveForms;

public class SynthController {
    /*SINE,
    TRIANGLE,
    SAWTOOTH,
    SQUARE,
    NOISE,
    PWM,
    KARPLUS_STRONG,
    TABLE*/
    private Filter _filter;
    SynthInstrument synth;
    Phaser _phaser;

    public SynthController(){
        synth = new SynthInstrument();
        synth = new SynthInstrument();
        synth.getOscillatorProperties(0).setWaveform(2); // sawtooth (see global.h for enumerations)
        synth.getAdsr().setDecayTime(.1f);

        _phaser = new Phaser(.5f, .7f, .5f, 440.f, 1600.f);
        synth.getAudioChannel().getProcessingChain().addProcessor(_phaser);


    }

    public SynthInstrument getSynth() {
        return synth;
    }
    public void setVolume(float volume){
        synth.getAudioChannel().setVolume(volume);
    }
    public void setAttack(float value){
        synth.getAdsr().setAttackTime(value);
    }
    public void setDecay(float value){
        synth.getAdsr().setDecayTime(value);
    }
    public void setSustain(float value){
        synth.getAdsr().setSustainLevel(value);
    }
    public void setRelease(float value){
        synth.getAdsr().setReleaseTime(value);
    }

}
