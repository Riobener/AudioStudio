package com.riobener.audiostudio.Instruments.Controllers;

import android.content.Context;

import com.riobener.audiostudio.Grid.Note;

import java.util.Vector;

import nl.igorski.mwengine.core.JavaUtilities;
import nl.igorski.mwengine.core.Pitch;
import nl.igorski.mwengine.core.SampleEvent;
import nl.igorski.mwengine.core.SampleManager;
import nl.igorski.mwengine.core.SampledInstrument;
import nl.igorski.mwengine.core.SynthEvent;
import nl.igorski.mwengine.core.SynthInstrument;

import static com.riobener.audiostudio.MainActivity.AMOUNT_OF_MEASURES;
import static com.riobener.audiostudio.MainActivity.STEPS_PER_MEASURE;

public class DrumController extends Controller {
    SampledInstrument sampler;
    SynthInstrument synth;
    Note[][] noteMap;
    Vector<SampleEvent> sampleEvents = new Vector<>();
    public DrumController(){
        sampler = new SampledInstrument();
        initInstrument();
    }

    public SampledInstrument getSampler() {
        return sampler;
    }


    public void initInstrument() {
        noteMap = new Note[AMOUNT_OF_MEASURES][9]; //standard size
        sampleEvents = new Vector<SampleEvent>();
        for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
            for (int j = 0; j < 9; j++) {
                noteMap[i][j] = new Note();
            }
        }
    }
    public void updateMapMeasures() {

        Note[][] newNoteMap = new Note[AMOUNT_OF_MEASURES][9];
        if (this.noteMap != null) {
            for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                for (int j = 0; j < 9; j++) {
                    if (i < AMOUNT_OF_MEASURES / 2)
                        newNoteMap[i][j] = this.noteMap[i][j];
                    else
                        newNoteMap[i][j] = new Note();
                }
            }
            this.noteMap = new Note[AMOUNT_OF_MEASURES][9];
            for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                for (int j = 0; j < 73; j++) {
                    this.noteMap[i][j] = newNoteMap[i][j];
                }
            }
        }
    }
    public void updateNoteMap(Note[][] noteMap) {
        for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
            for (int j = 0; j < 9; j++) {
                this.noteMap[i][j] = noteMap[i][j];
            }
        }
    }
    public Note[][] getNoteMap() {
        return noteMap;
    }

    private void createDrumEvent(String key, int position) {
        final SampleEvent drumEvent = new SampleEvent(sampler);
        drumEvent.setSample(SampleManager.getSample(key));
        drumEvent.positionEvent(0, STEPS_PER_MEASURE, position);
        drumEvent.addToSequencer(); // samples have to be explicitly added for playback

        sampleEvents.add(drumEvent);
    }
    private void loadWAVAsset(String key, String samplePath) {

        JavaUtilities.createSampleFromFile(key,samplePath);
    }
    private void resetEvents() {
        for (SampleEvent event : sampleEvents)
            event.delete();
        sampleEvents.clear();
    }
    public void updateEvents() {
        resetEvents();
        for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {

            for (int j = 0; j < 9; j++) {

                if (noteMap[i][j].isDrawable()) {
                    createDrumEvent(noteMap[i][j].getKeySample(),i);
                }

            }

        }
    }

}
