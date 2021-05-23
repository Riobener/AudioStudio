package com.riobener.audiostudio.Grid;

public class Camera{

    private float offsetX, offsetY;
    //The constructor. ix and iy is the initial offset. Useful if you are creating a game and need to change the initial offset to center around a starting position.
    //Most of the time it will be enough to set the values to 0
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
}