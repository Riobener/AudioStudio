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
    private Paint drawingText = new Paint();
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

        drawingText.setColor(Color.BLACK);
        drawingText.setStrokeWidth(2);


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
        cellWidth = getWidth()/16;
        cellHeight = getHeight()/12;
        cHeight = cellHeight*numRows;
        cWidth = cellWidth*numColumns;

        camera = new Camera(0, 0);
        cellWidth = cWidth / numColumns;
        cellHeight = cHeight / numRows;
        drawingText.setTextSize(cellHeight);
        noteMap = new Note[numColumns][numRows];
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                noteMap[i][j] = new Note();
            }
        }
        invalidate();
    }


    final int RECTANGLE_PADDING = 10;
    final int TEXTY = 10;

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
        if (camera.getOffsetY() > cHeight - getHeight() - cellHeight) {
            camera.setOffsetY(cHeight - getHeight() - cellHeight);
        }
        if (camera.getOffsetX() < 0) {
            camera.setOffsetX(0);
        }
        if (camera.getOffsetX() > cWidth - getWidth()) {
            camera.setOffsetX(cWidth - getWidth());
        }

        //drawing black keys
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (j == 2 || j == 4 || j == 6 || j == 9 || j == 11 //C7
                        || j == 14 || j == 16 || j == 18 || j == 21 || j == 23
                        || j == 26 || j == 28 || j == 30 || j == 33 || j == 35
                        || j == 38 || j == 40 || j == 42 || j == 45 || j == 47
                        || j == 50 || j == 52 || j == 54 || j == 57 || j == 59
                        || j == 62 || j == 64 || j == 66 || j == 69 || j == 71) //C2
                    canvas.drawRect(i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY(),
                            (i + 1) * cellWidth- camera.getOffsetX(), (j + 1) * cellHeight - camera.getOffsetY(),
                            blackKeyRow);
                if (i == 0) {
                    if (j == 1) {
                        canvas.drawText("C7", i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY() - TEXTY, drawingText);
                    } else if (j == 13) {
                        canvas.drawText("C6", i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY() - TEXTY, drawingText);
                    } else if (j == 25) {
                        canvas.drawText("C5", i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY() - TEXTY, drawingText);
                    } else if (j == 37) {
                        canvas.drawText("C4", i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY() - TEXTY, drawingText);
                    } else if (j == 49) {
                        canvas.drawText("C3", i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY() - TEXTY, drawingText);
                    } else if (j == 61) {
                        canvas.drawText("C2", i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY() - TEXTY, drawingText);
                    } else if (j == 73) {
                        canvas.drawText("C1", i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY() - TEXTY, drawingText);
                    }
                }

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
                if(i%16==0){
                    paintForColumn.setColor(Color.GREEN);
                    canvas.drawLine(i * cellWidth - camera.getOffsetX(), 0 - camera.getOffsetY(),
                            i * cellWidth - camera.getOffsetX(), cHeight - camera.getOffsetY(), paintForColumn);
                    paintForColumn.setColor(Color.BLACK);
                }
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
            if (camera.getOffsetX() >= 0 && camera.getOffsetX() <= cWidth - getWidth()) {
                camera.addOffsetX(newOffsetX);
            }

            Log.d("OFFSET", "OFFSEtY = " + camera.getOffsetY());

            x = event.getX();
            y = event.getY();
            invalidate();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
            if (clickDuration < MAX_CLICK_DURATION) {
                int column = (int) ((camera.getOffsetX() + event.getX()) / cellWidth);
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

    public int getOctave(int row) {

        return numColumns / 12;
    }
}