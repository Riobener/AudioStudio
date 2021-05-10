package com.riobener.audiostudio.Grid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.riobener.audiostudio.MainActivity;
import com.riobener.audiostudio.R;

import java.util.HashMap;


public class PianoRoll extends View {

    private int numColumns, numRows;
    private int cellWidth, cellHeight;
    private Paint blackPaint = new Paint();
    private Paint paintBorder = new Paint();
    private Paint blackKeyRow = new Paint();
    private Paint paintForColumn = new Paint();

    private Note[][] noteMap;

    private boolean isHiglightMode = false;

    public PianoRoll(Context context) {
        this(context, null);
    }

    public PianoRoll(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintForColumn.setStrokeWidth(5);
        blackKeyRow.setColor(Color.GRAY);
        blackKeyRow.setStrokeWidth(10);
        blackKeyRow.setAlpha(64);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setColor(Color.BLUE);
        paintBorder.setStrokeWidth(7);

    }

    public void getCellsArray() {

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

        cellWidth = getWidth() / numColumns;
        cellHeight = getHeight() / numRows;

        noteMap = new Note[numColumns][numRows];
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                noteMap[i][j] = new Note();
            }
        }
        invalidate();
    }


    final int RECTANGLE_PADDING = 15;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        if (numColumns == 0 || numRows == 0) {
            return;
        }
        int width = getWidth();
        int height = getHeight();
        //drawing black keys
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (j == 1 || j == 3 || j == 6 || j == 8 | j == 10)
                    canvas.drawRect(i * cellWidth, j * cellHeight,
                            (i + 1) * cellWidth, (j + 1) * cellHeight,
                            blackKeyRow);
            }
        }
        //fill cells by click event
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {


                if (noteMap[i][j].isDrawable() && noteMap[i][j].isHighlighted()) {
                    canvas.drawRect(i * cellWidth + RECTANGLE_PADDING, j * cellHeight + RECTANGLE_PADDING,
                            (i + 1) * cellWidth - RECTANGLE_PADDING, (j + 1) * cellHeight - RECTANGLE_PADDING,
                            paintBorder);
                    canvas.drawRect(i * cellWidth + RECTANGLE_PADDING, j * cellHeight + RECTANGLE_PADDING,
                            (i + 1) * cellWidth - RECTANGLE_PADDING, (j + 1) * cellHeight - RECTANGLE_PADDING,
                            blackPaint);
                } else if (noteMap[i][j].isDrawable() && !noteMap[i][j].isHighlighted()) {
                    canvas.drawRect(i * cellWidth + RECTANGLE_PADDING, j * cellHeight + RECTANGLE_PADDING,
                            (i + 1) * cellWidth - RECTANGLE_PADDING, (j + 1) * cellHeight - RECTANGLE_PADDING,
                            blackPaint);
                }

            }
        }

        for (int i = 1; i < numColumns; i++) {
            if (i % 4 == 0) {
                canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, paintForColumn);
            } else {
                canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, blackPaint);
            }

        }

        for (int i = 1; i < numRows; i++) {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, blackPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int column = (int) (event.getX() / cellWidth);
            int row = (int) (event.getY() / cellHeight);
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
        return true;
    }

    public void setHighLightMode(boolean mode) {
        isHiglightMode = mode;
    }

    public boolean isHiglightMode() {
        return isHiglightMode;
    }
}
