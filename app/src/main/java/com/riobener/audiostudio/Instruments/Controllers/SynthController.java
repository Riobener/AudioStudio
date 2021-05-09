package com.riobener.audiostudio.Instruments.Controllers;

import nl.igorski.mwengine.core.Filter;
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
    public SynthController(){
        RouteableOscillator rosc = new RouteableOscillator();

        rosc.setWave(4);
        synth = new SynthInstrument();
        synth.setROsc(rosc);
        synth.getAudioChannel().setVolume(.7f);
       //synth.getAdsr().setReleaseTime(0.3f);
        synth.getAdsr().setDecayTime( .5f );
        synth.getAdsr().setSustainLevel( 1f );
        float maxFilterCutoff = ( float ) 48000 / 8;
        float minFilterCutoff = 50.0f;
        _filter = new Filter(
                maxFilterCutoff / 2, ( float ) ( Math.sqrt( 1 ) / 2 ),
                minFilterCutoff, maxFilterCutoff, 2
        );
        synth.getAudioChannel().getProcessingChain().addProcessor( _filter );
    }

    public SynthInstrument getSynth() {
        return synth;
    }
    public void setVolume(float volume){
        synth.getAudioChannel().setVolume(volume);
    }
}
