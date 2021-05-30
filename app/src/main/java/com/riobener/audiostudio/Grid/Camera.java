package com.riobener.audiostudio.Grid;

public class Camera{

    private float offsetX, offsetY;

    public Camera(float ix, float iy){
        this.offsetX = ix;
        this.offsetY = iy;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }
    public void addOffsetY(float newOffsetY){
        this.offsetY +=newOffsetY;
    }

    public void addOffsetX(float newOffsetX) {
        this.offsetX +=newOffsetX;
    }
}