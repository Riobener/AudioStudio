package com.riobener.audiostudio.Grid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;


public class PianoRoll extends View {

    private int numColumns, numRows;
    private int cellWidth, cellHeight;
    private int cWidth, cHeight;
    private Paint blackPaint = new Paint();
    private Paint paintBorder = new Paint();
    private Paint blackKeyRow = new Paint();
    private Paint paintForColumn = new Paint();
    private Camera camera;
    private Note[][] noteMap;

    private boolean isHiglightMode = false;

    public PianoRoll(Context context) {
        this(context, null);
    }

    public PianoRoll(Context context, AttributeSet attrs) {
        super(context, attrs);

        blackPaint.setStrokeWidth(2);
        paintForColumn.setStrokeWidth(5);
        blackKeyRow.setColor(Color.GRAY);
        blackKeyRow.setStrokeWidth(10);
        blackKeyRow.setAlpha(64);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setColor(Color.rgb(255, 94, 19));
        paintBorder.setStrokeWidth(10);

    }

    public Note[][] getNoteMap() {
        return noteMap;
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    public int getNumRows() {
        return numRows;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    private void calculateDimensions() {

        if (numColumns < 1 || numRows < 1) {
            return;
        }
        cHeight = getHeight() * 3 - 300;
        cWidth = getWidth();
        camera = new Camera(0, 0);
        cellWidth = cWidth / numColumns;
        cellHeight = cHeight / numRows;

        noteMap = new Note[numColumns][numRows];
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                noteMap[i][j] = new Note();
            }
        }
        invalidate();
    }


    final int RECTANGLE_PADDING = 10;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);


        //Log.d("WI", "WIDTH = " + cWidth);
        if (numColumns == 0 || numRows == 0) {
            return;
        }
        if (camera.getOffsetY() < 0) {
            camera.setOffsetY(0);
        }
        if (camera.getOffsetY() > cHeight - getHeight() - cellHeight + 25) {
            camera.setOffsetY(cHeight - getHeight() - cellHeight + 35);
        }

        //drawing black keys
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (j == 1 || j == 3 || j == 6 || j == 8 | j == 10 | j == 47)
                    canvas.drawRect(i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY(),
                            (i + 1) * cellWidth - camera.getOffsetX(), (j + 1) * cellHeight - camera.getOffsetY(),
                            blackKeyRow);
            }
        }
        //fill cells by click event
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {

                if (noteMap[i][j].isDrawable() && noteMap[i][j].isHighlighted()) {
                    canvas.drawRect(i * cellWidth + RECTANGLE_PADDING - camera.getOffsetX(), j * cellHeight + RECTANGLE_PADDING - camera.getOffsetY(),
                            (i + 1) * cellWidth - RECTANGLE_PADDING - camera.getOffsetX(), (j + 1) * cellHeight - RECTANGLE_PADDING - camera.getOffsetY(),
                            paintBorder);
                    canvas.drawRect(i * cellWidth + RECTANGLE_PADDING - camera.getOffsetX(), j * cellHeight + RECTANGLE_PADDING - camera.getOffsetY(),
                            (i + 1) * cellWidth - RECTANGLE_PADDING - camera.getOffsetX(), (j + 1) * cellHeight - RECTANGLE_PADDING - camera.getOffsetY(),
                            blackPaint);
                } else if (noteMap[i][j].isDrawable() && !noteMap[i][j].isHighlighted()) {
                    canvas.drawRect(i * cellWidth + RECTANGLE_PADDING - camera.getOffsetX(), j * cellHeight + RECTANGLE_PADDING - camera.getOffsetY(),
                            (i + 1) * cellWidth - RECTANGLE_PADDING - camera.getOffsetX(), (j + 1) * cellHeight - RECTANGLE_PADDING - camera.getOffsetY(),
                            blackPaint);
                }

            }
        }

        for (int i = 1; i < numColumns; i++) {
            if (i % 4 == 0) {
                canvas.drawLine(i * cellWidth - camera.getOffsetX(), 0 - camera.getOffsetY(),
                        i * cellWidth - camera.getOffsetX(), cHeight - camera.getOffsetY(), paintForColumn);
            } else {
                canvas.drawLine(i * cellWidth - camera.getOffsetX(), 0 - camera.getOffsetY(),
                        i * cellWidth - camera.getOffsetX(), cHeight - camera.getOffsetY(), blackPaint);
            }

        }

        for (int i = 1; i < numRows; i++) {
            canvas.drawLine(0 - camera.getOffsetX(), i * cellHeight - camera.getOffsetY(),
                    cWidth - camera.getOffsetX(), i * cellHeight - camera.getOffsetY(), blackPaint);
        }

    }

    public float convertDpToPixel(float dp, Context context) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    float x, y;
    private static final int MAX_CLICK_DURATION = 100;
    private long startClickTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startClickTime = Calendar.getInstance().getTimeInMillis();
            x = event.getX();
            y = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float currX = event.getX();
            float currY = event.getY();

            float newOffsetX = (x - currX),
                    newOffsetY = (y - currY);

            if (camera.getOffsetY() >= 0 && camera.getOffsetY() <= cHeight - getHeight()) {
                camera.addOffsetY(newOffsetY);
            }

            Log.d("OFFSET", "OFFSEtY = " + camera.getOffsetY());

            x = event.getX();
            y = event.getY();
            invalidate();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
            if (clickDuration < MAX_CLICK_DURATION) {
                int column = (int) (event.getX() / cellWidth);
                int row = (int) ((camera.getOffsetY() + event.getY()) / cellHeight);

                Log.v("Cell is ", column + " " + row);
                if (!isHiglightMode) {
                    if (noteMap[column][row].isDrawable()) {
                        noteMap[column][row].setDrawable(false);
                    } else {
                        noteMap[column][row].setDrawable(true);
                    }
                } else if (isHiglightMode) {
                    if (noteMap[column][row].isHighlighted() && noteMap[column][row].isDrawable()) {
                        noteMap[column][row].setHighlighted(false);
                    } else if (!noteMap[column][row].isHighlighted() && noteMap[column][row].isDrawable()) {
                        noteMap[column][row].setHighlighted(true);
                    }
                }
                invalidate();
            }

        }
        invalidate();
        return true;
    }

    public void setHighLightMode(boolean mode) {
        isHiglightMode = mode;
    }

    public boolean isHiglightMode() {
        return isHiglightMode;
    }
    public int getOctave(int row){
        return numColumns/7;
    }
}
