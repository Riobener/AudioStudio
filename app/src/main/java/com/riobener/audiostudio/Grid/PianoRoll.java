package com.riobener.audiostudio.Grid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;
import java.util.Calendar;

import static com.riobener.audiostudio.MainActivity.AMOUNT_OF_MEASURES;


public class PianoRoll extends View {

    private int numRows;
    private int cellWidth, cellHeight;
    private int cWidth, cHeight;
    private Paint blackPaint = new Paint();
    private Paint paintBorder = new Paint();
    private Paint blackKeyRow = new Paint();
    private Paint paintForColumn = new Paint();
    private Paint drawingText = new Paint();
    private Paint drumsText = new Paint();
    private Paint expandedColor = new Paint();
    private Paint playingPaint = new Paint();
    private Camera camera;
    private Note[][] noteMap;
    private boolean isEdited;

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
        expandedColor.setColor(Color.BLACK);
        expandedColor.setAlpha(150);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setColor(Color.rgb(255, 94, 19));
        paintBorder.setStrokeWidth(10);
        drawingText.setColor(Color.BLACK);
        drawingText.setStrokeWidth(2);
        drumsText.setColor(Color.GRAY);
        drumsText.setStrokeWidth(1);
        isEdited = false;
        playingPaint.setColor(Color.rgb(102, 102, 255));
        playingPaint.setStrokeWidth(3);
        playingPaint.setStyle(Paint.Style.STROKE);
    }

    public Note[][] getNoteMap() {
        return noteMap;
    }

    public void setNumRows(int rows) {
        this.numRows = rows;
    }

    public void loadNoteMap(Note[][] noteMap) {
        if (numRows == 73) {//synth
            this.noteMap = new Note[AMOUNT_OF_MEASURES][73];
            if (noteMap != null) {
                for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                    for (int j = 0; j < 73; j++) {
                        this.noteMap[i][j] = noteMap[i][j];
                    }

                }
            }
        } else {//drums
            this.noteMap = new Note[AMOUNT_OF_MEASURES][9];
            if (noteMap != null) {
                for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                    for (int j = 0; j < 9; j++) {
                        this.noteMap[i][j] = noteMap[i][j];
                    }

                }
            }
        }


    }

    public int getNumRows() {
        return numRows;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    public void initNoteMap() {
        this.noteMap = new Note[AMOUNT_OF_MEASURES][numRows];
        for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
            for (int j = 0; j < numRows; j++) {
                this.noteMap[i][j] = new Note();
            }
        }
    }

    public void calculateDimensions() {
        if (numRows == 9) {
            cellWidth = getWidth() / 16;
            cellHeight = getHeight() / 9;
            cHeight = cellHeight * numRows;
            cWidth = cellWidth * AMOUNT_OF_MEASURES;

            camera = new Camera(0, 0);
            drumsText.setTextSize(cellHeight);
            initNoteMap();
            invalidate();
        } else {
            cellWidth = getWidth() / 16;
            cellHeight = getHeight() / 16;
            cHeight = cellHeight * numRows;
            cWidth = cellWidth * AMOUNT_OF_MEASURES;

            camera = new Camera(0, cellHeight * 37);
            drawingText.setTextSize(cellHeight);
            initNoteMap();
            invalidate();
        }

    }

    final int RECTANGLE_PADDING = 10;
    final int TEXTY = 10;

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        cWidth = cellWidth * AMOUNT_OF_MEASURES;

        //Log.d("WI", "WIDTH = " + cWidth);

        if (camera.getOffsetY() < 0) {
            camera.setOffsetY(0);
        }
        if (camera.getOffsetY() > cHeight - getHeight()) {
            camera.setOffsetY(cHeight - getHeight());
        }
        if (camera.getOffsetX() < 0) {
            camera.setOffsetX(0);
        }
        if (camera.getOffsetX() > cWidth - getWidth()) {
            camera.setOffsetX(cWidth - getWidth());
        }

        //drawing black keys
        if (numRows != 9) {
            for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                for (int j = 0; j < numRows; j++) {
                    if (j == 2 || j == 4 || j == 6 || j == 9 || j == 11 //C7
                            || j == 14 || j == 16 || j == 18 || j == 21 || j == 23
                            || j == 26 || j == 28 || j == 30 || j == 33 || j == 35
                            || j == 38 || j == 40 || j == 42 || j == 45 || j == 47
                            || j == 50 || j == 52 || j == 54 || j == 57 || j == 59
                            || j == 62 || j == 64 || j == 66 || j == 69 || j == 71) //C1
                        canvas.drawRect(i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY(),
                                (i + 1) * cellWidth - camera.getOffsetX(), (j + 1) * cellHeight - camera.getOffsetY(),
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
                        } else if (j == 72) {
                            canvas.drawText("C1", i * cellWidth - camera.getOffsetX(), j * cellHeight + cellHeight - camera.getOffsetY() - TEXTY, drawingText);
                        }
                    }

                }
            }
        } else {
            for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                for (int j = 0; j < numRows; j++) {
                    if (i == 0) {
                        if (j == 1) {
                            canvas.drawText("pad " + j, i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY() - TEXTY, drumsText);
                        } else if (j == 2) {
                            canvas.drawText("pad " + j, i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY() - TEXTY, drumsText);
                        } else if (j == 3) {
                            canvas.drawText("pad " + j, i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY() - TEXTY, drumsText);
                        } else if (j == 4) {
                            canvas.drawText("pad " + j, i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY() - TEXTY, drumsText);
                        } else if (j == 5) {
                            canvas.drawText("pad " + j, i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY() - TEXTY, drumsText);
                        } else if (j == 6) {
                            canvas.drawText("pad " + j, i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY() - TEXTY, drumsText);
                        } else if (j == 7) {
                            canvas.drawText("pad " + j, i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY() - TEXTY, drumsText);
                        } else if (j == 8) {
                            canvas.drawText("pad " + j, i * cellWidth - camera.getOffsetX(), j * cellHeight - camera.getOffsetY() - TEXTY, drumsText);
                            canvas.drawText("pad " + (j+1), i * cellWidth - camera.getOffsetX(), j * cellHeight+cellHeight - camera.getOffsetY() - TEXTY, drumsText);
                        }


                    }

                }
            }
        }

        //fill cells by click event
        for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {

            for (int j = 0; j < numRows; j++) {
                if (isHiglightMode) {
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
                } else {
                    if (noteMap[i][j].isDrawable())
                        canvas.drawRect(i * cellWidth + RECTANGLE_PADDING - camera.getOffsetX(), j * cellHeight + RECTANGLE_PADDING - camera.getOffsetY(),
                                (i + 1) * cellWidth - RECTANGLE_PADDING - camera.getOffsetX(), (j + 1) * cellHeight - RECTANGLE_PADDING - camera.getOffsetY(),
                                blackPaint);
                    noteMap[i][j].setHighlighted(false);
                }
            }
        }
        for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
            for (int j = 0; j < numRows; j++) {
                    if (noteMap[i][j].isPlaying()) {
                        canvas.drawRect(i * cellWidth + RECTANGLE_PADDING - camera.getOffsetX(), j * cellHeight + RECTANGLE_PADDING - camera.getOffsetY(),
                                (i + 1) * cellWidth - RECTANGLE_PADDING - camera.getOffsetX(), (j + 1) * cellHeight - RECTANGLE_PADDING - camera.getOffsetY(),
                                playingPaint);
                    }
            }
        }
        for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
            for (int j = 0; j < numRows; j++) {
                if (noteMap[i][j].getDuration() != 1 && noteMap[i][j].isDrawable()) {
                    canvas.drawRect(i * cellWidth + RECTANGLE_PADDING - camera.getOffsetX(), j * cellHeight + RECTANGLE_PADDING - camera.getOffsetY(),
                            (i + noteMap[i][j].getDuration()) * cellWidth - RECTANGLE_PADDING - camera.getOffsetX(), (j + 1) * cellHeight - RECTANGLE_PADDING - camera.getOffsetY(),
                            expandedColor);
                }
            }
        }

        for (int i = 1; i < AMOUNT_OF_MEASURES; i++) {
            if (i % 4 == 0) {

                canvas.drawLine(i * cellWidth - camera.getOffsetX(), 0 - camera.getOffsetY(),
                        i * cellWidth - camera.getOffsetX(), cHeight - camera.getOffsetY(), paintForColumn);
                if (i % 16 == 0) {
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
                    i * cWidth - camera.getOffsetX(), i * cellHeight - camera.getOffsetY(), blackPaint);
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
                    if (noteMap[column][row].isHighlighted()) {
                        noteMap[column][row].setHighlighted(false);
                    }
                } else if (isHiglightMode) {
                    if (noteMap[column][row].isDrawable()) {
                        if (noteMap[column][row].isHighlighted()) {
                            noteMap[column][row].setHighlighted(false);
                        } else {
                            noteMap[column][row].setHighlighted(true);
                        }
                    }
                }
                isEdited = true;
                invalidate();
            }

        }
        invalidate();
        return true;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public void setHighLightMode(boolean mode) {
        isHiglightMode = mode;
    }

    public boolean isHiglightMode() {
        return isHiglightMode;
    }


}
