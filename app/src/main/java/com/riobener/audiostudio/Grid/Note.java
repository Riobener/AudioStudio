package com.riobener.audiostudio.Grid;

public class Note {
    private int duration;
    private boolean isHighlighted;
    private boolean isDrawable;

    public Note() {
        duration = 1;
        isHighlighted = false;
        isDrawable = false;
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
    }
}
