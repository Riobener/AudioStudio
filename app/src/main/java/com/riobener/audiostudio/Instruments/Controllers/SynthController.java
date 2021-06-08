package com.riobener.audiostudio.Instruments.Controllers;

import android.util.Log;

import androidx.constraintlayout.motion.utils.Oscillator;

import com.riobener.audiostudio.Grid.Note;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import nl.igorski.mwengine.core.BaseAudioEvent;
import nl.igorski.mwengine.core.BitCrusher;
import nl.igorski.mwengine.core.Delay;
import nl.igorski.mwengine.core.Filter;
import nl.igorski.mwengine.core.LFO;
import nl.igorski.mwengine.core.Phaser;
import nl.igorski.mwengine.core.Pitch;
import nl.igorski.mwengine.core.Reverb;
import nl.igorski.mwengine.core.RouteableOscillator;
import nl.igorski.mwengine.core.SampleManager;
import nl.igorski.mwengine.core.SynthEvent;
import nl.igorski.mwengine.core.SynthInstrument;
import nl.igorski.mwengine.core.WaveForms;

import static com.riobener.audiostudio.MainActivity.AMOUNT_OF_MEASURES;
import static com.riobener.audiostudio.MainActivity.TOTAL_COUNT_OF_INSTRUMENTS;
import static nl.igorski.mwengine.MWEngine.OUTPUT_CHANNELS;
import static nl.igorski.mwengine.MWEngine.SAMPLE_RATE;

public class SynthController extends Controller {
    /*SINE,
    TRIANGLE,
    SAWTOOTH,
    SQUARE,
    NOISE,
    PWM,
    KARPLUS_STRONG,
    TABLE*/
    Filter filter;
    BitCrusher bitCrusher;
    SynthInstrument synth;
    LFO lfo;
    Reverb reverb;
    Phaser phaser;
    Delay delay;
    Note[][] noteMap;
    Vector<SynthEvent> synthEvents = new Vector<>();
    List<String> noteNames = Arrays.asList("C", "B", "A#", "A", "G#", "G", "F#", "F", "E", "D#", "D", "C#", "C",
            "B", "A#", "A", "G#", "G", "F#", "F", "E", "D#", "D", "C#", "C",
            "B", "A#", "A", "G#", "G", "F#", "F", "E", "D#", "D", "C#", "C",
            "B", "A#", "A", "G#", "G", "F#", "F", "E", "D#", "D", "C#", "C",
            "B", "A#", "A", "G#", "G", "F#", "F", "E", "D#", "D", "C#", "C",
            "B", "A#", "A", "G#", "G", "F#", "F", "E", "D#", "D", "C#", "C");

    public SynthController() {
        this.name = "Synth"+TOTAL_COUNT_OF_INSTRUMENTS;
        synth = new SynthInstrument();
        synth.setOscillatorAmount(1);
        synth.getOscillatorProperties(0).setWaveform(5); // pulse width modulation
        synth.getAdsr().setReleaseTime(0.25f);
        synth.getAdsr().setSustainLevel(0.9f);
        synth.getAdsr().setDecayTime(0.5f);
        synth.getAdsr().setAttackTime(0.0002f);

        // adjust synthesizer volumes
        synth.getAudioChannel().setVolume(.7f);
        delay = new Delay(250, 2000, .35f, .5f, OUTPUT_CHANNELS);
        reverb = new Reverb(.1f,0,.4f,.6f);

        phaser = new Phaser(.5f, .7f, .5f, 440.f, 1600.f) ;
        filter = new Filter(0,0,440.f, SAMPLE_RATE/8,OUTPUT_CHANNELS);
        lfo = new LFO();
        bitCrusher = new BitCrusher(0,8f,0);

        initInstrument();
    }

    public void initInstrument() {
        noteMap = new Note[AMOUNT_OF_MEASURES][73]; //standard size
        synthEvents = new Vector<SynthEvent>();
        for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
            for (int j = 0; j < 73; j++) {
                noteMap[i][j] = new Note();
            }
        }
    }
    @Override
    public boolean isMuted() {
        return synth.getAudioChannel().getMuted();
    }
    @Override
    public void muteInstrument(boolean state) {
        synth.getAudioChannel().setMuted(state);
    }
    public void updateMapMeasures() {

        Note[][] newNoteMap = new Note[AMOUNT_OF_MEASURES][73];
        if (this.noteMap != null) {
            for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                for (int j = 0; j < 73; j++) {
                    if (i < AMOUNT_OF_MEASURES / 2)
                        newNoteMap[i][j] = this.noteMap[i][j];
                    else
                        newNoteMap[i][j] = new Note();
                }
            }
            this.noteMap = new Note[AMOUNT_OF_MEASURES][73];
            for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                for (int j = 0; j < 73; j++) {
                    this.noteMap[i][j] = newNoteMap[i][j];
                }
            }
        }
    }

    public void updateNoteMap(Note[][] noteMap) {
        for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
            for (int j = 0; j < 73; j++) {
                this.noteMap[i][j] = noteMap[i][j];
            }
        }
    }

    public Note[][] getNoteMap() {
        return noteMap;
    }

    private void createSynthEvent(double frequency, int position, int duration) {
        SynthEvent event = new SynthEvent((float) frequency, position, duration, synth);
        event.calculateBuffers();
        synthEvents.add(event);
        //synth.updateEvents();
    }

    private void resetEvents() {
        for (SynthEvent event : synthEvents)
            event.delete();
        synthEvents.clear();

    }

    public void updateEvents() {
        resetEvents();
        for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {

            for (int j = 0; j < 73; j++) {

                if (noteMap[i][j].isDrawable()) {
                    int octave = 0;
                    if (j == 0) {
                        octave = 7;
                    } else if (j > 0 && j <= 12) {
                        octave = 6;
                    } else if (j > 12 && j <= 24) {
                        octave = 5;
                    } else if (j > 24 && j <= 36) {
                        octave = 4;
                    } else if (j > 36 && j <= 48) {
                        octave = 3;
                    } else if (j > 48 && j <= 61) {
                        octave = 2;
                    } else if (j > 60 && j <= 72) {
                        octave = 1;
                    }
                    //Log.d("ASSSSSSSSF", "DURATION OF " + noteNames.get(j) + " in column " + i + "  with duration of " + noteMap[i][j].getDuration());
                    createSynthEvent(Pitch.note(noteNames.get(j), octave), i, noteMap[i][j].getDuration());
                }

            }

        }
    }

    public void flushInstrument() {
        for (final BaseAudioEvent event : synthEvents)
            event.delete();
        synthEvents.clear();
        synth = null;
        phaser = null;
        delay = null;
        noteMap = null;
    }


    public SynthInstrument getSynth() {
        return synth;
    }

    public void setVolume(float volume) {
        synth.getAudioChannel().setVolume(volume);
    }

    public void setAttack(float value) {
        synth.getAdsr().setAttackTime(value);
    }

    public void setDecay(float value) {
        synth.getAdsr().setDecayTime(value);
    }

    public void setSustain(float value) {
        synth.getAdsr().setSustainLevel(value);
    }

    public void setRelease(float value) {
        synth.getAdsr().setReleaseTime(value);
    }

    public void setOscillatorAmount(int i){
        synth.setOscillatorAmount(i);
    }
    public void setOscillatorWave(int value, int oscillatorNum){
        synth.getOscillatorProperties(oscillatorNum).setWaveform(value);
    }
    public void setDetune(float value, int oscillatorNum){
        synth.getOscillatorProperties(oscillatorNum).setDetune(value);
    }
    public void setOctave(int value, int oscillatorNum){
        synth.getOscillatorProperties(oscillatorNum).setOctaveShift(value);
    }
    public void setDelayFeedback(float value){
        delay.setFeedback(value);
    }
    public void setReverbSize(float value){
        reverb.setSize(value);
    }
    public void enableReverbDelay(boolean state){
        if(state){
            synth.getAudioChannel().getProcessingChain().addProcessor(reverb);
            synth.getAudioChannel().getProcessingChain().addProcessor(delay);
        }else{
            synth.getAudioChannel().getProcessingChain().removeProcessor(reverb);
            synth.getAudioChannel().getProcessingChain().removeProcessor(delay);
        }
    }
    public void setPhaserDepth(float depth){
        phaser.setDepth(depth);
    }
    public void setPhaserFeedback(float feedback){
        phaser.setFeedback(feedback);
    }
    public void setPhaserRate(float depth){
        phaser.setRate(depth);
    }
    public void enablePhaser(boolean state){
        if(state){
            synth.getAudioChannel().getProcessingChain().addProcessor(phaser);

        }else{
            synth.getAudioChannel().getProcessingChain().removeProcessor(phaser);
        }
    }
    public void enableFilter(boolean state){
        if(state){
            synth.getAudioChannel().getProcessingChain().addProcessor(filter);

        }else{
            synth.getAudioChannel().getProcessingChain().removeProcessor(filter);
        }
    }
    public void setFilterCutoff(float cutoff){
        filter.setCutoff(cutoff);
    }
    public void setFilterReso(float reso){
        filter.setResonance(reso);
    }
    public void enableLFO(boolean state){
        if(state){
            filter.setLFO(lfo);

        }else{
            filter.setLFO(null);
        }
    }
    public void setLFOValue(float value){
        lfo.setDepth(value);
        lfo.setRate((int)value);
    }
    public void setLFOWave(int value){
        lfo.setWave(value);
    }
    public void enableBitCrusher(boolean state){
        if(state){

            synth.getAudioChannel().getProcessingChain().addProcessor(bitCrusher);

        }else{
            synth.getAudioChannel().getProcessingChain().removeProcessor(bitCrusher);
        }
    }
    public void setBitCrusherAmount(float amount ){
        bitCrusher.setAmount(amount);
    }
    public void setBitCrusherOut(float out ){
        bitCrusher.setOutputMix(out);
    }
}

