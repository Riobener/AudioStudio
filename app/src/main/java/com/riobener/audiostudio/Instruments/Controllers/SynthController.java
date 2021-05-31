package com.riobener.audiostudio.Instruments.Controllers;

import android.util.Log;

import com.riobener.audiostudio.Grid.Note;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import nl.igorski.mwengine.core.BaseAudioEvent;
import nl.igorski.mwengine.core.Delay;
import nl.igorski.mwengine.core.Filter;
import nl.igorski.mwengine.core.Phaser;
import nl.igorski.mwengine.core.Pitch;
import nl.igorski.mwengine.core.RouteableOscillator;
import nl.igorski.mwengine.core.SynthEvent;
import nl.igorski.mwengine.core.SynthInstrument;
import nl.igorski.mwengine.core.WaveForms;

import static com.riobener.audiostudio.MainActivity.AMOUNT_OF_MEASURES;
import static nl.igorski.mwengine.MWEngine.OUTPUT_CHANNELS;

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
        synth = new SynthInstrument();
        delay = new Delay(250, 2000, .35f, .5f, OUTPUT_CHANNELS);
        synth.getOscillatorProperties(0).setWaveform(5); // pulse width modulation
        synth.getAdsr().setReleaseTime(0.15f);
        synth.getAudioChannel().getProcessingChain().addProcessor(delay);
        // adjust synthesizer volumes
        synth.getAudioChannel().setVolume(.7f);
        /*phaser = new Phaser(.5f, .7f, .5f, 440.f, 1600.f);
        synth.getAudioChannel().getProcessingChain().addProcessor(phaser);*/
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
        this.noteMap = new Note[AMOUNT_OF_MEASURES][73];
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
        final SynthEvent event = new SynthEvent((float) frequency, position, duration, synth);
        event.calculateBuffers();
        synthEvents.add(event);
        //synth.updateEvents();
    }

    public void updateEvents() {
        for (SynthEvent event : synthEvents)
            event.delete();
        synthEvents.clear();
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
                    Log.d("ASSSSSSSSF", "DURATION OF " + noteNames.get(j)+" in column "+ i + "  with duration of "+ noteMap[i][j].getDuration());
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

}
