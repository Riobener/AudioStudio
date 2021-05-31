package com.riobener.audiostudio.Grid;

import android.util.Log;

import static com.riobener.audiostudio.MainActivity.AMOUNT_OF_MEASURES;

public class Note {
    private int duration;
    private boolean isHighlighted;
    private boolean isDrawable;

    public Note() {
        duration = 1;
        isHighlighted = false;
        isDrawable = false;
    }
    public void enlargeDuration(){
        if(duration<AMOUNT_OF_MEASURES)
        duration++;
        Log.d("TAGDUR","DURATION"+duration);
    }
    public void reduceDuration(){
        if(duration>1)
        duration--;
    }
    public int getDuration() {
        return duration;
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
}
