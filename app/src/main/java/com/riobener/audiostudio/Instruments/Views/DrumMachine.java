package com.riobener.audiostudio.Instruments.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;


import com.nex3z.flowlayout.FlowLayout;
import com.riobener.audiostudio.Grid.Note;
import com.riobener.audiostudio.Instruments.Controllers.Controller;
import com.riobener.audiostudio.Instruments.Controllers.DrumController;
import com.riobener.audiostudio.MainActivity;
import com.riobener.audiostudio.R;

import java.util.Random;
import java.util.regex.Pattern;


import nl.igorski.mwengine.core.BaseInstrument;
import nl.igorski.mwengine.core.SampleEvent;
import nl.igorski.mwengine.core.SampleManager;
import nl.igorski.mwengine.core.SampledInstrument;

import static android.app.Activity.RESULT_OK;
import static com.riobener.audiostudio.MainActivity.AMOUNT_OF_MEASURES;

//what a shit code here...
public class DrumMachine extends InstrumentView {
    LinearLayout.LayoutParams drumsParams;
    int pagerWidth;
    int pagerHeight;

    int instrumentColor;
    LinearLayout drumsView;
    Button loadDrumPatch;
    DrumController drumController;
    //Drum pads
    LinearLayout padsLayout;
    GridLayout padsView;
    Button pad1;
    Button pad2;
    Button pad3;
    Button pad4;
    Button pad5;
    Button pad6;
    Button pad7;
    Button pad8;
    Button pad9;

    boolean isEditorMode = false;

    public DrumMachine(int pagerWidth, int pagerHeight) {
        this.pagerHeight = pagerHeight;
        this.pagerWidth = pagerWidth;
        drumController = new DrumController();
        instrumentType = "Drums";
        Random rnd = new Random();
        instrumentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        drumsParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        drumsParams.gravity = Gravity.CENTER;
    }

    @Override
    public String getInstrumentType() {
        return this.instrumentType;
    }

    public View createView(Context context) {
        drumsView = new LinearLayout(context);
        drumsView.setLayoutParams(drumsParams);
        drumsView.setOrientation(LinearLayout.VERTICAL);
        int backgroundColor = ContextCompat.getColor(context, R.color.backgroundColor);
        drumsView.setBackgroundColor(backgroundColor);
        LinearLayout settingsLayout = new LinearLayout(context);
        settingsLayout.setLayoutParams(drumsParams);
        settingsLayout.setOrientation(LinearLayout.HORIZONTAL);
        settingsLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        //settingsLayout.setBackground(createColorShape());
        loadDrumPatch = new Button(context);
        loadDrumPatch.setText("Редактировать");
        loadDrumPatch.setAllCaps(false);
        loadDrumPatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditorMode = !isEditorMode;
            }
        });
        settingsLayout.addView(loadDrumPatch);
        drumsView.addView(settingsLayout);
        drumsView.addView(createDrumPads(context));
        return drumsView;
    }

    @Override
    public BaseInstrument getInstrument() {
        return drumController.getSampler();
    }

    @Override
    public Controller getController() {
        return drumController;
    }

    public View createDrumPads(final Context context) {
        LinearLayout.LayoutParams padsParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        padsParams.setMargins(50, 0, 50, 0);
        int width = pagerWidth / 3 - 50;
        int height = pagerHeight/3-50;
        LinearLayout.LayoutParams rowsParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        padsLayout = new LinearLayout(context);
        padsLayout.setLayoutParams(padsParams);
        padsLayout.setBackground(createColorShape());
        padsLayout.setOrientation(LinearLayout.VERTICAL);
        padsLayout.setGravity(Gravity.CENTER);

        LinearLayout row1 = new LinearLayout(context);
        LinearLayout row2 = new LinearLayout(context);
        LinearLayout row3 = new LinearLayout(context);
        row1.setLayoutParams(rowsParams);
        row2.setLayoutParams(rowsParams);
        row3.setLayoutParams(rowsParams);
        row1.setOrientation(LinearLayout.HORIZONTAL);
        row2.setOrientation(LinearLayout.HORIZONTAL);
        row3.setOrientation(LinearLayout.HORIZONTAL);
        row1.setPadding(7, 7, 7, 7);
        row2.setPadding(7, 7, 7, 7);
        row3.setPadding(7, 7, 7, 7);

        pad1 = new Button(context);
        pad1.setText("PAD 1");
        pad1.setTextSize(15);
        pad1.setWidth(width);
        pad1.setHeight(height);
        pad1.setBackground(createColorShape());
        pad1.setTextColor(Color.WHITE);
        pad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEditorMode){
                    Note[][] map = drumController.getNoteMap();
                    if(map[0][0].getEvent()!=null)
                    map[0][0].getEvent().play();
                }else{
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("audio/x-wav");
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                    Activity activity = (Activity)context;
                    activity.startActivityForResult(chooseFile, 0);
                }



            }
        });
        pad2 = new Button(context);
        pad2.setText("PAD 2");
        pad2.setTextSize(15);
        pad2.setWidth(width);
        pad2.setHeight(height);
        pad2.setBackground(createColorShape());
        pad2.setTextColor(Color.WHITE);
        pad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("audio/x-wav");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                Activity activity = (Activity)context;
                activity.startActivityForResult(chooseFile, 1);
                new SampleEvent().play();
            }
        });
        pad3 = new Button(context);
        pad3.setText("PAD 3");
        pad3.setTextSize(15);
        pad3.setWidth(width);
        pad3.setHeight(height);
        pad3.setBackground(createColorShape());
        pad3.setTextColor(Color.WHITE);
        pad3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("audio/x-wav");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                Activity activity = (Activity)context;
                activity.startActivityForResult(chooseFile, 2);
            }
        });
        pad4 = new Button(context);
        pad4.setText("PAD 4");
        pad4.setTextSize(15);
        pad4.setWidth(width);
        pad4.setHeight(height);
        pad4.setBackground(createColorShape());
        pad4.setTextColor(Color.WHITE);
        pad4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("audio/x-wav");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                Activity activity = (Activity)context;
                activity.startActivityForResult(chooseFile, 3);
            }
        });
        pad5 = new Button(context);
        pad5.setText("PAD 5");
        pad5.setTextSize(15);
        pad5.setWidth(width);
        pad5.setHeight(height);
        pad5.setBackground(createColorShape());
        pad5.setTextColor(Color.WHITE);
        pad5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("audio/x-wav");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                Activity activity = (Activity)context;
                activity.startActivityForResult(chooseFile, 4);
            }
        });
        pad6 = new Button(context);
        pad6.setText("PAD 6");
        pad6.setTextSize(15);
        pad6.setWidth(width);
        pad6.setHeight(height);
        pad6.setBackground(createColorShape());
        pad6.setTextColor(Color.WHITE);
        pad6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("audio/x-wav");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                Activity activity = (Activity)context;
                activity.startActivityForResult(chooseFile, 5);
            }
        });
        pad7 = new Button(context);
        pad7.setText("PAD 7");
        pad7.setTextSize(15);
        pad7.setWidth(width);
        pad7.setHeight(height);
        pad7.setBackground(createColorShape());
        pad7.setTextColor(Color.WHITE);
        pad7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("audio/x-wav");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                Activity activity = (Activity)context;
                activity.startActivityForResult(chooseFile, 6);
            }
        });
        pad8 = new Button(context);
        pad8.setText("PAD 8");
        pad8.setTextSize(15);
        pad8.setWidth(width);
        pad8.setHeight(height);
        pad8.setBackground(createColorShape());
        pad8.setTextColor(Color.WHITE);
        pad8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("audio/x-wav");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                Activity activity = (Activity)context;
                activity.startActivityForResult(chooseFile, 7);
            }
        });
        pad9 = new Button(context);
        pad9.setText("PAD 9");
        pad9.setTextSize(15);
        pad9.setWidth(width);
        pad9.setHeight(height);
        pad9.setBackground(createColorShape());
        pad9.setTextColor(Color.WHITE);
        pad9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("audio/x-wav");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                Activity activity = (Activity)context;
                activity.startActivityForResult(chooseFile, 8);
            }
        });
        row1.addView(pad1);
        row1.addView(pad2);
        row1.addView(pad3);
        row2.addView(pad4);
        row2.addView(pad5);
        row2.addView(pad6);
        row3.addView(pad7);
        row3.addView(pad8);
        row3.addView(pad9);
        padsLayout.addView(row1);
        padsLayout.addView(row2);
        padsLayout.addView(row3);
        //Log.v("SIZES", "WIDTH  "+padsView.getWidth() +"   " + padsView.getHeight());
        return padsLayout;
    }

    private GradientDrawable createColorShape() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(new float[]{80, 80, 80, 80,
                    80, 80, 80, 80});
            shape.setColor(Color.BLACK);

            shape.setStroke(5, instrumentColor);
            return shape;
        }
        return null;
    }
}
