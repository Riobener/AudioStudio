package com.riobener.audiostudio.Instruments.Controllers;

import android.content.Context;

import com.riobener.audiostudio.Grid.Note;

import java.util.Vector;

import nl.igorski.mwengine.core.BaseAudioEvent;
import nl.igorski.mwengine.core.JavaUtilities;
import nl.igorski.mwengine.core.Pitch;
import nl.igorski.mwengine.core.SampleEvent;
import nl.igorski.mwengine.core.SampleManager;
import nl.igorski.mwengine.core.SampledInstrument;
import nl.igorski.mwengine.core.SynthEvent;
import nl.igorski.mwengine.core.SynthInstrument;

import static com.riobener.audiostudio.MainActivity.AMOUNT_OF_MEASURES;
import static com.riobener.audiostudio.MainActivity.STEPS_PER_MEASURE;
import static com.riobener.audiostudio.MainActivity.TOTAL_COUNT_OF_INSTRUMENTS;

public class DrumController extends Controller {
    SampledInstrument sampler;
    Note[][] drumMap;
    Vector<SampleEvent> sampleEvents = new Vector<>();

    public DrumController(){
        this.name = "Drums" + TOTAL_COUNT_OF_INSTRUMENTS;
        sampler = new SampledInstrument();
        initInstrument();
    }

    public SampledInstrument getSampler() {
        return sampler;
    }


    public void initInstrument() {
        drumMap = new Note[AMOUNT_OF_MEASURES][9]; //standard size
        sampleEvents = new Vector<SampleEvent>();
        for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
            for (int j = 0; j < 9; j++) {
                drumMap[i][j] = new Note();
            }
        }
    }
    public void updateMapMeasures() {

        Note[][] newdrumMap = new Note[AMOUNT_OF_MEASURES][9];
        if (this.drumMap != null) {
            for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                for (int j = 0; j < 9; j++) {
                    if (i < AMOUNT_OF_MEASURES / 2)
                        newdrumMap[i][j] = this.drumMap[i][j];
                    else
                        newdrumMap[i][j] = new Note();
                }
            }
            this.drumMap = new Note[AMOUNT_OF_MEASURES][9];
            for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                for (int j = 0; j < 9; j++) {
                    this.drumMap[i][j] = newdrumMap[i][j];
                }
            }
        }
    }
    public void updateNoteMap(Note[][] drumMap) {
        for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
            for (int j = 0; j < 9; j++) {
                this.drumMap[i][j] = drumMap[i][j];
            }
        }
    }
    public Note[][] getNoteMap() {
        return drumMap;
    }
    public Vector<SampleEvent> getEvents(){
        return sampleEvents;
    }
    private void createDrumEvent(String key, int position) {
        final SampleEvent drumEvent = new SampleEvent(sampler);
        drumEvent.setSample(SampleManager.getSample(key));
        drumEvent.positionEvent(0, STEPS_PER_MEASURE, position);
        drumEvent.addToSequencer(); // samples have to be explicitly added for playback

        sampleEvents.add(drumEvent);
    }

    private void resetEvents() {
        for (SampleEvent event : sampleEvents)
            event.delete();
        sampleEvents.clear();

    }
    public void flushInstrument(){
        for (final BaseAudioEvent event : sampleEvents)
            event.delete();
        sampleEvents.clear();
        sampler.delete();
        sampler = null;
        drumMap = null;
    }
    public void updateEvents() {
        resetEvents();
        for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {

            for (int j = 0; j < 9; j++) {

                if(drumMap[i][j].getKeySample()!="0"){
                    SampleEvent drumEvent = new SampleEvent(sampler);
                    drumEvent.setSample(SampleManager.getSample(drumMap[i][j].getKeySample()));
                    drumMap[i][j].setEvent(drumEvent);
                if (drumMap[i][j].isDrawable()) {
                        createDrumEvent(drumMap[i][j].getKeySample(),i);
                    }

                }

            }

        }
    }

}
