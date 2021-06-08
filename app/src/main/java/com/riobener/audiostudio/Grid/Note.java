package com.riobener.audiostudio.Grid;

import android.util.Log;

import nl.igorski.mwengine.core.SampleEvent;

import static com.riobener.audiostudio.MainActivity.AMOUNT_OF_MEASURES;

public class Note {
    private int duration;
    private boolean isHighlighted;
    private boolean isDrawable;
    private boolean isPlaying;

    //if drumEvent
    private String keySample;
    private String filePath;
    SampleEvent event;
    public Note() {
        isPlaying = false;
        keySample = "0";
        filePath = "0";
        duration = 1;
        isHighlighted = false;
        isDrawable = false;
        event =null;
    }
    public void setEvent(SampleEvent event){
        this.event = event;
    }
    public SampleEvent getEvent(){
        return this.event;
    }
    public void enlargeDuration(){
        if(duration<AMOUNT_OF_MEASURES)
        duration++;

    }
    public void reduceDuration(){
        if(duration>1)
        duration--;
    }
    public int getDuration() {
        return duration;
    }
    public boolean isPlaying(){
        return isPlaying;
    }
    public void setPlaying(boolean state){
        this.isPlaying = state;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public boolean isDrawable() {
        return isDrawable;
    }

    public void setDrawable(boolean drawable) {
        isDrawable = drawable;
        duration=1;
    }

    public String getKeySample() {
        return keySample;
    }

    public void setKeySample(String keySample) {
        this.keySample = keySample;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
