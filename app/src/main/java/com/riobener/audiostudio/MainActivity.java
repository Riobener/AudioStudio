package com.riobener.audiostudio;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;

import com.convergencelabstfx.pianoview.PianoTouchListener;
import com.convergencelabstfx.pianoview.PianoView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import com.riobener.audiostudio.Grid.Note;
import com.riobener.audiostudio.Grid.PianoRoll;
import com.riobener.audiostudio.Instruments.Controllers.Controller;
import com.riobener.audiostudio.Instruments.Controllers.DrumController;
import com.riobener.audiostudio.Instruments.Controllers.SynthController;
import com.riobener.audiostudio.Instruments.Views.InstrumentView;
import com.riobener.audiostudio.Midi.MidiController;
import com.riobener.audiostudio.R;

import nl.igorski.mwengine.MWEngine;
import nl.igorski.mwengine.core.*;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Level;
import java.util.regex.Pattern;

import static java.lang.Math.log10;
import static nl.igorski.mwengine.core.MWEngineCoreJNI.LevelUtility_RMS;
import static nl.igorski.mwengine.core.MWEngineCoreJNI.new_LevelUtility;

public class MainActivity extends Activity implements PianoTouchListener {
    /**
     * IMPORTANT : when creating native layer objects through JNI it
     * is important to remember that when the Java references go out of scope
     * (and thus are finalized by the garbage collector), the SWIG interface
     * will invoke the native layer destructors. As such we hold strong
     * references to JNI Objects during the application lifetime
     */
    private Limiter _limiter;
    private LPFHPFilter _lpfhpf;
    private SynthInstrument _synth1;
    private SynthInstrument _synth2;
    private SampledInstrument _sampler;
    private Filter _filter;
    private Phaser _phaser;
    private Delay _delay;
    private MWEngine _engine;
    private SequencerController _sequencerController;
    private Vector<SynthEvent> _synth1Events;
    private Vector<SynthEvent> _synth2Events;
    private Vector<SampleEvent> _drumEvents;
    private SynthEvent _liveEvent;
    private boolean _sequencerPlaying = false;
    private boolean _isRecording = false;
    private boolean _inited = false;
    Button muteCurrent;
    Button muteAll;

    // AAudio is only supported from Android 8/Oreo onwards.
    private boolean _supportsAAudio = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O;
    private Drivers.types _audioDriver = _supportsAAudio ? Drivers.types.AAUDIO : Drivers.types.OPENSL;

    private int SAMPLE_RATE;
    private int BUFFER_SIZE;
    private int OUTPUT_CHANNELS = 2; // 1 = mono, 2 = stereo

    private float minFilterCutoff = 50.0f;
    private float maxFilterCutoff;

    public static int STEPS_PER_MEASURE = 16;  // amount of subdivisions within a single measure
    private static String LOG_TAG = "MWENGINE"; // logcat identifier
    private static int PERMISSIONS_CODE = 8081981;
    private int oldBPM = 120;
    public static int AMOUNT_OF_MEASURES = 16;
    public static int TOTAL_COUNT_OF_INSTRUMENTS = 1;
    /**
     * Called when the activity is created. This also fires
     * on screen orientation changes.
     */

    BottomSheetBehavior pianoSheetBehavior;
    BottomSheetBehavior pianoRollSheetBehavior;
    BottomSheetBehavior settingsSheetBehavior;
    PianoRoll pianoRoll;
    InstrumentsManager manager = new InstrumentsManager();
    LayoutInflater inflater;
    private ViewPager pager = null;
    private InstrumentPager pagerAdapter = null;
    PianoView pianoView;
    SynthInstrument instrument = new SynthInstrument();
    static Vector<SynthEvent> notes = new Vector<SynthEvent>();
    static int BASE_OCTAVE = 3;
    private List<String> noteNames = Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");
    Button expandMeasures;
    Button stopButton;
    Button playButton;
    Button newInstrument;
    MidiController midiController;
    EditText nameChanger;
    /*Button deletePage;*/
    public static boolean isSynth = true;
    public static boolean MIDI_IS_AVAILABLE = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        inflater = getLayoutInflater();
        // these may not necessarily all be required for your use case (e.g. if you're not recording
        // from device audio inputs or reading/writing files) but are here for self-documentation

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            String[] PERMISSIONS = {
                    Manifest.permission.RECORD_AUDIO, // RECORD_AUDIO must be granted prior to engine.start()
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            // Check if we have all the necessary permissions, if not: prompt user
            int permission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (permission == PackageManager.PERMISSION_GRANTED)
                init();
            else
                requestPermissions(PERMISSIONS, PERMISSIONS_CODE);
        }

        pagerAdapter = new InstrumentPager();
        pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Toast.makeText(MainActivity.this, pager.getCurrentItem()+" ТЕКУЩИЙ", Toast.LENGTH_SHORT).show();
                // Toast.makeText(MainActivity.this, pager.getCurrentItem()+" ТЕКУЩИЙ", Toast.LENGTH_SHORT).show();
                //
                    /*newInstrument.setEnabled(false);
                    deletePage.setEnabled(false);*/

            }

            @Override
            public void onPageSelected(int position) {
                updatePageInfo();
                //pianoRoll.loadNoteMap(manager.getController(pager.getCurrentItem()).getNoteMap());
                //Toast.makeText(MainActivity.this, pager.getCurrentItem()+" ТЕКУЩИЙ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
        // Create an initial view to display; must be a subclass of FrameLayout.

        pagerAdapter.addView(manager.createSynthView(MainActivity.this));
        pagerAdapter.notifyDataSetChanged();
        newInstrument = findViewById(R.id.createPage);
        newInstrument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] instruments = {"Синтезатор", "Драм машина"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Выберите вид инструмента");
                builder.setItems(instruments, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            addView(manager.createSynthView(MainActivity.this));
                            TOTAL_COUNT_OF_INSTRUMENTS = manager.size();

                        } else {
                            addView(manager.createDrumMachine(MainActivity.this, pager.getWidth(), pager.getHeight()));
                            TOTAL_COUNT_OF_INSTRUMENTS = manager.size();
                        }
                        for (int i = 0; i < manager.size(); i++) {
                            Log.d("MANAGER", manager.getController(i).getName() + " ");
                        }
                        for (int i = 0; i < pagerAdapter.getCount(); i++) {
                            Log.d("PAGER", pagerAdapter.getView(i) + " ");
                        }

                    }
                });
                builder.show();

            }
        });
       /* deletePage = findViewById(R.id.deletePage);
        deletePage.setEnabled(false);
        deletePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = pager.getCurrentItem();
                removeView(manager.getInstrumentView(index));
                pagerAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, " УДАЛЕННЫЙ "+index+" "+manager.getController(index).getName(), Toast.LENGTH_SHORT).show();
                manager.removeInstrument(index);

                //nameChanger.setText(manager.getController(pager.getCurrentItem()).getName());
                TOTAL_COUNT_OF_INSTRUMENTS = manager.size();
                for(int i =0; i<manager.size();i++){
                    Log.d("MANAGER",manager.getController(i).getName()+" ");
                }
                for(int i =0; i<pagerAdapter.getCount();i++){
                    Log.d("PAGER",pagerAdapter.getView(i).getTransitionName()+" ");
                }



            }
        });*/
        /*newInstrument.setEnabled(true);
        deletePage.setEnabled(true);*/
        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSequencer();
            }
        });
        stopButton = findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastStep =_sequencerController.getStepPosition();
                Note[][]map = pianoRoll.getNoteMap();
                for(int i =0;i<pianoRoll.getNumRows();i++){
                    map[lastStep][i].setPlaying(false);
                }
                pianoRoll.invalidate();
                stopSequencer();
            }
        });
        expandMeasures = findViewById(R.id.expandMeasures);
        expandMeasures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopSequencer();
                if (AMOUNT_OF_MEASURES < 64) {
                    AMOUNT_OF_MEASURES *= 2;
                }
                for (int i = 0; i < manager.size(); i++) {
                    Controller controller = (Controller) manager.getController(i);
                    controller.updateMapMeasures();
                    pianoRoll.loadNoteMap(controller.getNoteMap());
                    pianoRoll.invalidate();
                }
                if (AMOUNT_OF_MEASURES == 16)
                    _sequencerController.updateMeasures(1, STEPS_PER_MEASURE);
                else if (AMOUNT_OF_MEASURES == 32) {
                    _sequencerController.updateMeasures(2, STEPS_PER_MEASURE);
                } else if (AMOUNT_OF_MEASURES == 64) {
                    _sequencerController.updateMeasures(4, STEPS_PER_MEASURE);
                }

            }
        });
        initPiano();
        initPianoRoll();
        midiController = new MidiController(MainActivity.this);
        midiController.setupMidi();
        initSettings();
        initPianoNotes();
        initInstrumentNameChanger();
        initMuteButtons();

    }

    /*@RequiresApi(api = Build.VERSION_CODES.M)
    public void refreshMidiStatus(){
        MidiDeviceInfo[] info = midiController.getInfos();
            if(info[0].getProperties().getString(MidiDeviceInfo.PROPERTY_NAME)!=null) {

            }
    }*/
    public void initInstrumentNameChanger() {
        nameChanger = findViewById(R.id.instrumentName);
        nameChanger.setText(manager.getController(pager.getCurrentItem()).getName());
        nameChanger.setOnKeyListener(new View.OnKeyListener() {
                                         public boolean onKey(View v, int keyCode, KeyEvent event) {
                                             if (event.getAction() == KeyEvent.ACTION_DOWN &&
                                                     (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                                 String nameGetter = nameChanger.getText().toString();
                                                 String oldName = manager.getController(pager.getCurrentItem()).getName();
                                                 if (nameGetter.length() >= 4 && nameGetter.length() <= 12) {
                                                     nameChanger.setText(nameGetter + "");
                                                     manager.getController(pager.getCurrentItem()).setName(nameGetter);
                                                 } else {
                                                     nameChanger.setText(oldName + "");
                                                     Toast.makeText(MainActivity.this, "Длина названия от 5 до 20!", Toast.LENGTH_LONG).show();
                                                 }

                                                 return true;
                                             }
                                             return false;
                                         }
                                     }
        );
    }

    public void initPianoNotes() {
        if (isSynth) {
            notes.clear();
            SynthInstrument synth = (SynthInstrument) manager.getInstrument(pager.getCurrentItem());

            for (int i = 0; i < 88; ++i) {
                int octave = (int) (BASE_OCTAVE + Math.ceil(i / 12));
                notes.add(new SynthEvent((float) Pitch.note(noteNames.get(i % 12), octave), synth));
            }
        }

    }

    public void startSequencer() {
        if (_sequencerPlaying != true) {
            refreshCurrentPattern();
            _sequencerPlaying = true;
            _engine.getSequencerController().setPlaying(_sequencerPlaying);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void stopSequencer() {
        if (_sequencerPlaying != false) {
            _sequencerPlaying = false;
            _engine.getSequencerController().setPlaying(_sequencerPlaying);
            _engine.getSequencerController().rewind();

        }
    }

    public void initSettings() {
        FrameLayout frameLayout = findViewById(R.id.settingsFrame);
        settingsSheetBehavior = BottomSheetBehavior.from(frameLayout);
        settingsSheetBehavior.setDraggable(false);
        settingsSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        Button startSettings = findViewById(R.id.settingsButton);

        startSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingsSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    settingsSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    settingsSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });
        EditText bpmChanger = findViewById(R.id.bpmChanger);

        bpmChanger.setOnKeyListener(new View.OnKeyListener() {
                                        public boolean onKey(View v, int keyCode, KeyEvent event) {

                                            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                                                    (keyCode == KeyEvent.KEYCODE_ENTER)) {

                                                // сохраняем текст, введённый до нажатия Enter в переменную
                                                String bpmCount = bpmChanger.getText().toString();
                                                int bpmInInt = Integer.parseInt(bpmCount);
                                                if (bpmInInt >= 60 && bpmInInt < 240) {
                                                    bpmChanger.setText(bpmInInt + "");
                                                    oldBPM = bpmInInt;
                                                    _engine.getSequencerController().setTempo(bpmInInt, 4, 4); // update to match new tempo in 4/4 time
                                                } else {
                                                    bpmChanger.setText(oldBPM + "");
                                                    Toast.makeText(MainActivity.this, "Только выше 60 и ниже 240!", Toast.LENGTH_LONG).show();
                                                }

                                                return true;
                                            }
                                            return false;
                                        }
                                    }
        );
    }

    //only for midi
    public static void playNote(int noteIndex) {
        if (isSynth)
            notes.get(noteIndex).play();
    }

    public static void stopNote(int noteIndex) {
        if (isSynth)
            notes.get(noteIndex).stop();
    }
    public void  initMuteButtons(){
        muteAll = findViewById(R.id.soundOnlyThis);
        muteCurrent = findViewById(R.id.muteInstrument);
        muteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controller controller = manager.getController(pager.getCurrentItem());
                if(controller.isMuted()){
                    controller.muteInstrument(false);
                    controller.setOnlyPlaying(true);
                    for(InstrumentView i:manager.getInstruments()){
                            if(controller!=i.getController())
                            i.getController().muteInstrument(true);
                    }
                }else if(controller.isOnlyPlaying()){
                    for(InstrumentView i:manager.getInstruments()){
                        if(controller!=i.getController()){
                            i.getController().muteInstrument(false);
                            i.getController().setOnlyPlaying(false);
                        }

                    }
                }else{
                    for(InstrumentView i:manager.getInstruments()){
                        if(controller!=i.getController())
                            i.getController().muteInstrument(true);
                    }
                    controller.setOnlyPlaying(true);
                }

            }
        });
        muteCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controller controller = manager.getController(pager.getCurrentItem());
                if(controller.isMuted()){
                    controller.muteInstrument(false);
                }else{
                    controller.muteInstrument(true);
                }
            }
        });
    }
    public void initPiano() {
        //Piano bottom sheet
        FrameLayout frameLayout = findViewById(R.id.pianoFrame);
        pianoSheetBehavior = BottomSheetBehavior.from(frameLayout);
        pianoView = findViewById(R.id.pianoView);
        pianoView.addPianoTouchListener(this);
        pianoSheetBehavior.setDraggable(false);
        pianoSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        Button pianoStart = findViewById(R.id.showPiano);
        pianoStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes.clear();
                if (pianoSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    initPianoNotes();

                    pianoSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    pianoSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });
    }


    private void refreshCurrentPattern() {
        if (pianoRoll.getNoteMap() != null) {
            if (pianoRoll.isEdited()) {
                Controller controller = manager.getController(pager.getCurrentItem());
                controller.updateNoteMap(pianoRoll.getNoteMap());
                controller.updateEvents();
                pianoRoll.setEdited(false);
            }

        }
    }

    private Button highlightCell;
    private Button enlargeDuration;
    private Button reduceDuration;
    private Button upButton;
    private Button downButton;
    private Button leftButton;
    private Button rightButton;
    private Button selectAll;
    private Button deleteNoteButton;

    private void initPianoRoll() {
        //Piano bottom sheet
        FrameLayout frameLayout = findViewById(R.id.pianoRollFrame);
        pianoRoll = findViewById(R.id.pianoRoll);
        pianoRollSheetBehavior = BottomSheetBehavior.from(frameLayout);
        pianoRollSheetBehavior.setDraggable(false);
        pianoRollSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        final Button pianoRollStart = findViewById(R.id.pianoRollButton);
        pianoRollStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //notes.clear();

                if (isSynth) {

                    pianoRoll.setNumRows(73);
                    pianoRoll.calculateDimensions();
                    pianoRoll.invalidate();
                } else {

                    pianoRoll.setNumRows(9);
                    pianoRoll.calculateDimensions();
                    pianoRoll.invalidate();
                }
                pianoRoll.initNoteMap();
                Controller controller = manager.getController(pager.getCurrentItem());
                if (pianoRollSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    expandMeasures.setEnabled(false);
                    pianoRoll.loadNoteMap(controller.getNoteMap());

                    pianoRoll.invalidate();
                    pianoRollSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    expandMeasures.setEnabled(true);
                    pianoRoll.setHighLightMode(false);
                    pianoRoll.loadNoteMap(controller.getNoteMap());
                    pianoRoll.invalidate();
                    pianoRollSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });

        enlargeDuration = findViewById(R.id.enlargeDuration);
        enlargeDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openFilePicker();
                if (pianoRoll.isHiglightMode() && isSynth) {
                    pianoRoll.setEdited(true);
                    Note[][] noteMap = pianoRoll.getNoteMap();
                    for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                        for (int j = 0; j < pianoRoll.getNumRows(); j++) {
                            if (noteMap[i][j].isHighlighted())
                                noteMap[i][j].enlargeDuration();
                        }
                    }
                }
                pianoRoll.invalidate();
            }
        });
        reduceDuration = findViewById(R.id.reduceDuration);
        reduceDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pianoRoll.isHiglightMode() && isSynth) {
                    pianoRoll.setEdited(true);
                    Note[][] noteMap = pianoRoll.getNoteMap();
                    for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                        for (int j = 0; j < pianoRoll.getNumRows(); j++) {
                            if (noteMap[i][j].isHighlighted())
                                noteMap[i][j].reduceDuration();
                        }
                    }
                }
                pianoRoll.invalidate();
            }
        });
        highlightCell = findViewById(R.id.highLightMode);
        highlightCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pianoRoll.isHiglightMode()) {
                    setEditorButtonsState(false);
                    pianoRoll.setHighLightMode(false);

                } else {
                    setEditorButtonsState(true);
                    pianoRoll.setHighLightMode(true);
                }
                pianoRoll.invalidate();
            }
        });
        upButton = findViewById(R.id.upNote);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveNotes("UP");
            }
        });
        downButton = findViewById(R.id.downNote);
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveNotes("DOWN");
            }
        });
        leftButton = findViewById(R.id.leftNote);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveNotes("LEFT");
            }
        });
        rightButton = findViewById(R.id.rightNote);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveNotes("RIGHT");
            }
        });
        selectAll = findViewById(R.id.selectAll);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pianoRoll.isHiglightMode()) {
                    pianoRoll.setEdited(true);
                    Note[][] noteMap = pianoRoll.getNoteMap();
                    for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                        for (int j = 0; j < pianoRoll.getNumRows(); j++) {
                            if (noteMap[i][j].isDrawable()) {
                                noteMap[i][j].setHighlighted(true);
                            }
                        }
                    }
                }
                pianoRoll.invalidate();
            }
        });
        deleteNoteButton = findViewById(R.id.deleteNote);
        deleteNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pianoRoll.isHiglightMode()) {
                    pianoRoll.setEdited(true);
                    Note[][] noteMap = pianoRoll.getNoteMap();
                    for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                        for (int j = 0; j < pianoRoll.getNumRows(); j++) {
                            if (noteMap[i][j].isHighlighted()) {
                                noteMap[i][j].setHighlighted(false);
                                noteMap[i][j].setDrawable(false);
                                noteMap[i][j].setDuration(1);
                            }
                        }
                    }
                }
                pianoRoll.invalidate();
            }
        });
    }

    private void updatePageInfo() {
        newInstrument.setEnabled(true);
        /*if (manager.size() > 1) {
            deletePage.setEnabled(true);
        } else {
            deletePage.setEnabled(false);
        }*/
        if (manager.getInstrumentType(pager.getCurrentItem()) == "Synth") {
            pianoRoll.setNumRows(73);
            pianoRoll.calculateDimensions();
            pianoRoll.invalidate();
            isSynth = true;
        } else {
            pianoRoll.setNumRows(9);
            pianoRoll.calculateDimensions();
            isSynth = false;
            pianoRoll.invalidate();
        }
        pianoRoll.initNoteMap();
        pianoRoll.loadNoteMap(manager.getController(pager.getCurrentItem()).getNoteMap());
        nameChanger.setText(manager.getController(pager.getCurrentItem()).getName());
        pianoRoll.invalidate();
        if (MIDI_IS_AVAILABLE && isSynth) {
            initPianoNotes();
        }
    }

    private void moveNotes(String state) {
        Stack<Integer> movableNotes = new Stack<>();
        if (pianoRoll.isHiglightMode()) {
            pianoRoll.setEdited(true);
            Note[][] noteMap = pianoRoll.getNoteMap();
            switch (state) {
                case "UP":
                    for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                        for (int j = 0; j < pianoRoll.getNumRows(); j++) {
                            if (noteMap[i][j].isHighlighted()) {
                                if (j - 1 > -1) {

                                    noteMap[i][j - 1].setDrawable(true);
                                    noteMap[i][j - 1].setHighlighted(true);
                                    noteMap[i][j - 1].setDuration(noteMap[i][j].getDuration());
                                    noteMap[i][j].setDrawable(false);
                                    noteMap[i][j].setHighlighted(false);
                                    noteMap[i][j].setDuration(1);
                                }
                            }
                        }
                    }
                    break;
                case "DOWN":
                    for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                        for (int j = 0; j < pianoRoll.getNumRows(); j++) {
                            if (noteMap[i][j].isHighlighted()) {
                                if (j + 1 < pianoRoll.getNumRows()) {
                                    movableNotes.push(noteMap[i][j].getDuration());
                                    movableNotes.push(j + 1);
                                    movableNotes.push(i);
                                    noteMap[i][j].setDrawable(false);
                                    noteMap[i][j].setHighlighted(false);
                                    noteMap[i][j].setDuration(1);
                                }
                            }
                        }
                    }
                    while (!movableNotes.isEmpty()) {

                        int i = movableNotes.pop();
                        int j = movableNotes.pop();
                        int duration = movableNotes.pop();
                        noteMap[i][j].setDrawable(true);
                        noteMap[i][j].setHighlighted(true);
                        noteMap[i][j].setDuration(duration);
                    }
                    break;
                case "LEFT":
                    for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                        for (int j = 0; j < pianoRoll.getNumRows(); j++) {
                            if (noteMap[i][j].isHighlighted()) {
                                if (i - 1 > -1) {

                                    noteMap[i - 1][j].setDrawable(true);
                                    noteMap[i - 1][j].setHighlighted(true);
                                    noteMap[i - 1][j].setDuration(noteMap[i][j].getDuration());
                                    noteMap[i][j].setDrawable(false);
                                    noteMap[i][j].setHighlighted(false);
                                    noteMap[i][j].setDuration(1);
                                }
                            }
                        }
                    }
                    break;
                case "RIGHT":

                    int newI = -2;
                    for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                        for (int j = 0; j < pianoRoll.getNumRows(); j++) {
                            if (noteMap[i][j].isHighlighted()) {

                                if (i + 1 < AMOUNT_OF_MEASURES) {
                                    movableNotes.push(noteMap[i][j].getDuration());
                                    movableNotes.push(j);
                                    movableNotes.push(i + 1);
                                    noteMap[i][j].setDrawable(false);
                                    noteMap[i][j].setHighlighted(false);
                                    noteMap[i][j].setDuration(1);
                                }
                            }
                        }
                    }
                    while (!movableNotes.isEmpty()) {

                        int i = movableNotes.pop();
                        int j = movableNotes.pop();
                        int duration = movableNotes.pop();
                        noteMap[i][j].setDrawable(true);
                        noteMap[i][j].setHighlighted(true);
                        noteMap[i][j].setDuration(duration);
                    }
                    break;
            }
        }
        pianoRoll.invalidate();
    }

    private void setEditorButtonsState(boolean state) {
        if (isSynth) {
            enlargeDuration.setEnabled(state);
            reduceDuration.setEnabled(state);
            upButton.setEnabled(state);
            downButton.setEnabled(state);
            leftButton.setEnabled(state);
            rightButton.setEnabled(state);
            selectAll.setEnabled(state);
            deleteNoteButton.setEnabled(state);
        } else {
            enlargeDuration.setEnabled(false);
            reduceDuration.setEnabled(false);
            upButton.setEnabled(state);
            downButton.setEnabled(state);
            leftButton.setEnabled(state);
            rightButton.setEnabled(state);
            selectAll.setEnabled(state);
            deleteNoteButton.setEnabled(state);
        }

    }

    @Override
    public void onKeyDown(@NonNull PianoView piano, int key) {
        Log.d(LOG_TAG, "KEY PRESSED IS " + key);
        if (notes.get(key) != null && isSynth)
            notes.get(key).play();
    }

    @Override
    public void onKeyUp(@NonNull PianoView piano, int key) {
        Log.d(LOG_TAG, "KEY UPED IS " + key);
        if (notes.get(key) != null && isSynth)
            notes.get(key).stop();
    }

    @Override
    public void onKeyClick(@NonNull PianoView piano, int key) {

    }

    public void addView(View newPage) {
        int pageIndex = pagerAdapter.addView(newPage);
        pagerAdapter.notifyDataSetChanged();
        // You might want to make "newPage" the currently displayed page:
        pager.setCurrentItem(pageIndex, true);
    }

    public void removeView(View defunctPage) {
        int pageIndex = pagerAdapter.removeView(pager, defunctPage);
        pagerAdapter.notifyDataSetChanged();
        // You might want to choose what page to display, if the current page was "defunctPage".
        if (pageIndex == pagerAdapter.getCount())
            pageIndex--;
        pager.setCurrentItem(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != PERMISSIONS_CODE) return;
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int grantResult = grantResults[i];
            if (permission.equals(Manifest.permission.RECORD_AUDIO) && grantResult == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_CODE);
            }
        }
    }

    /**
     * Called when the activity is destroyed. This also fires
     * on screen orientation changes, hence the override as we need
     * to watch the engines memory allocation outside of the Java environment
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        flushSong();        // free memory allocated by song
        _engine.dispose();  // dispose the engine
        Log.d(LOG_TAG, "MWEngineActivity destroyed");
    }

    private void init() {
        if (_inited)
            return;

        Log.d(LOG_TAG, "initing MWEngineActivity");

        // STEP 1 : preparing the native audio engine

        _engine = new MWEngine(getApplicationContext(), new StateObserver());
        MWEngine.optimizePerformance(this);

        // get the recommended buffer size for this device (NOTE : lower buffer sizes may
        // provide lower latency, but make sure all buffer sizes are powers of two of
        // the recommended buffer size (overcomes glitching in buffer callbacks )
        // getting the correct sample rate upfront will omit having audio going past the system
        // resampler reducing overall latency

        BUFFER_SIZE = MWEngine.getRecommendedBufferSize(getApplicationContext());
        SAMPLE_RATE = MWEngine.getRecommendedSampleRate(getApplicationContext());

        _engine.createOutput(SAMPLE_RATE, BUFFER_SIZE, OUTPUT_CHANNELS, _audioDriver);

        // STEP 2 : let's create some music !

        _synth1Events = new Vector<SynthEvent>();
        _synth2Events = new Vector<SynthEvent>();
        _drumEvents = new Vector<SampleEvent>();

        setupSong();

        _engine.start();
        //findViewById( R.id.RecordButton ).setOnClickListener( new RecordOutputHandler() );




        if (!_supportsAAudio) {
            findViewById(R.id.DriverSpinner).setVisibility(View.GONE);
        } else {
            ((Spinner) findViewById(R.id.DriverSpinner)).setOnItemSelectedListener(new DriverChangeHandler());
        }

        _inited = true;
    }


    /* protected methods */

    protected void setupSong() {
        _sequencerController = _engine.getSequencerController();
        _sequencerController.setTempoNow(120.0f, 4, 4); // 130 BPM in 4/4 time
        _sequencerController.updateMeasures(1, STEPS_PER_MEASURE); // we'll loop just a single measure with given subdivisions

        // cache some of the engines properties

        final ProcessingChain masterBus = _engine.getMasterBusProcessors();

        // Load some samples from the packaged assets folder into the SampleManager

        /*loadWAVAsset("hat.wav", "hat");
        loadWAVAsset("clap.wav", "clap");*/

        // create a lowpass filter to catch all low rumbling and a limiter to prevent clipping of output :)

        _lpfhpf = new LPFHPFilter((float) MWEngine.SAMPLE_RATE, 35, OUTPUT_CHANNELS);
        _limiter = new Limiter(50f, 500f, 0.5f);

        masterBus.addProcessor( _lpfhpf );
         masterBus.addProcessor( _limiter );

    }

    protected void flushSong() {
        // this ensures that Song resources currently in use by the engine are released

        _engine.pause();

        // calling 'delete()' on a BaseAudioEvent invokes the
        // native layer destructor (and removes it from the sequencer)

        for (final BaseAudioEvent event : _synth1Events)
            event.delete();
        for (final BaseAudioEvent event : _synth2Events)
            event.delete();
        for (final BaseAudioEvent event : _drumEvents)
            event.delete();

        // clear Vectors so all references to the events are broken

        _synth1Events.clear();
        _synth2Events.clear();
        _drumEvents.clear();

        // detach all processors from engine's master bus

        _engine.getMasterBusProcessors().reset();

        // calling 'delete()' on all instruments invokes the native layer destructor
        // (and frees memory allocated to their resources, e.g. AudioChannels, Processors)

        _synth1.delete();
        _synth2.delete();
        _sampler.delete();

        // allow these to be garbage collected

        _synth1 = null;
        _synth2 = null;
        _sampler = null;

        // and these (garbage collection invokes native layer destructors, so we'll let
        // these processors be cleared lazily)

        _filter = null;
        _phaser = null;
        _delay = null;
        _lpfhpf = null;

        // flush sample memory allocated in the SampleManager
        SampleManager.flushSamples();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d(LOG_TAG, "window focus changed for MWEngineActivity, has focus > " + hasFocus);

        if (!hasFocus) {
            // suspending the app - halt audio rendering in MWEngine Thread to save CPU cycles
            if (_engine != null)
                _engine.pause();
        } else {
            // returning to the app
            if (!_inited)
                init();            // initialize this example application
            else
                _engine.unpause(); // resumes existing audio rendering thread
        }
    }

    /* event handlers */

    private class DriverChangeHandler implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String selectedValue = parent.getItemAtPosition(pos).toString();
            _audioDriver = selectedValue.toLowerCase().equals("aaudio") ? Drivers.types.AAUDIO : Drivers.types.OPENSL;
            _engine.setAudioDriver(_audioDriver);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }



    private class RecordOutputHandler implements View.OnClickListener {
        @Override
        public void onClick( View v ) {
            _isRecording = !_isRecording;
            _engine.setRecordingState(
                    _isRecording, Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/mwengine_output.wav"
            );
            (( Button ) v ).setText( _isRecording ? R.string.rec_btn_off : R.string.rec_btn_on );
        }
    }


    /* state change message listener */

    private class StateObserver implements MWEngine.IObserver {
        private final Notifications.ids[] _notificationEnums = Notifications.ids.values(); // cache the enumerations (from native layer) as int Array

        public void handleNotification(final int aNotificationId) {
            switch (_notificationEnums[aNotificationId]) {
                case ERROR_HARDWARE_UNAVAILABLE:
                    Log.d(LOG_TAG, "ERROR : received driver error callback from native layer");
                    // re-initialize thread
                    if (_engine.canRestartEngine()) {
                        _engine.dispose();
                        _engine.createOutput(SAMPLE_RATE, BUFFER_SIZE, OUTPUT_CHANNELS, _audioDriver);
                        _engine.start();
                    } else {
                        Log.d(LOG_TAG, "exceeded maximum amount of retries. Cannot continue using audio engine");
                    }
                    break;
                case MARKER_POSITION_REACHED:
                    Log.d(LOG_TAG, "Marker position has been reached");
                    break;
                case RECORDING_COMPLETED:
                    Log.d(LOG_TAG, "Recording has completed");
                    break;
            }
        }

        public void handleNotification(final int aNotificationId, final int aNotificationValue) {
            switch (_notificationEnums[aNotificationId]) {
                case SEQUENCER_POSITION_UPDATED:

                    // for this notification id, the notification value describes the precise buffer offset of the
                    // engine when the notification fired (as a value in the range of 0 - BUFFER_SIZE). using this value
                    // we can calculate the amount of samples pending until the next step position is reached
                    // which in turn allows us to calculate the engine latency

                    int sequencerPosition = _sequencerController.getStepPosition();
                    int elapsedSamples = _sequencerController.getBufferPosition();

                    Log.d(LOG_TAG, "seq. position: " + sequencerPosition + ", buffer offset: " + aNotificationValue +
                            ", elapsed samples: " + elapsedSamples);
                    if (sequencerPosition == 15 ||
                            sequencerPosition == 31 ||
                            sequencerPosition == 45 ||
                            sequencerPosition == 63) {
                        refreshCurrentPattern();
                    }
                    Note[][] map = manager.getController(pager.getCurrentItem()).getNoteMap();


                        if(sequencerPosition>0){
                            for(int i = 0; i<pianoRoll.getNumRows();i++){
                                map[sequencerPosition-1][i].setPlaying(false);
                            }
                        }else if(sequencerPosition==0){
                            for(int i = 0; i<pianoRoll.getNumRows();i++){
                                map[AMOUNT_OF_MEASURES-1][i].setPlaying(false);
                            }
                        }
                        for(int i = 0; i<pianoRoll.getNumRows();i++){
                            map[sequencerPosition][i].setPlaying(true);
                        }

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            pianoRoll.invalidate();

                        }
                    });
                case RECORDED_SNIPPET_READY:
                    runOnUiThread(new Runnable() {
                        public void run() {
                            // we run the saving on a different thread to prevent buffer under runs while rendering audio
                            _engine.saveRecordedSnippet(aNotificationValue); // notification value == snippet buffer index
                        }
                    });
                    break;
                case RECORDED_SNIPPET_SAVED:
                    Log.d(LOG_TAG, "Recorded snippet " + aNotificationValue + " saved to storage");
                    break;
            }
        }
    }


    public void loadWAVPath(String keySample, String filePath) {
        final Context ctx = getApplicationContext();
        JavaUtilities.createSampleFromFile(keySample, filePath);
    }

    /*private void openFilePicker(){
        UnicornFilePicker.from(MainActivity.this)
                .addConfigBuilder()
                .selectMultipleFiles(false)
                .showOnlyDirectory(true)
                .setRootDirectory(Environment.getExternalStorageDirectory().getAbsolutePath())
                .showHiddenFiles(false)
                .setFilters(new String[]{"wav"})
                .addItemDivider(true)
                .theme(R.style.UnicornFilePicker_Dracula)
                .build()
                .forResult(Constants.REQ_UNICORN_FILE);

    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Note[][] map = manager.getController(pager.getCurrentItem()).getNoteMap();
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String file = uri.getPath();
            Log.v("FILE", file);

            String[] separated = file.split(":");

            Log.v("FILE", separated[0]);
            Log.v("FILE", separated[1]);
            file = "/storage/emulated/0/" + separated[1];

            for (int i = 0; i < AMOUNT_OF_MEASURES; i++) {
                map[i][requestCode].setFilePath(file);
                map[i][requestCode].setKeySample("pad" + requestCode + 1);
            }
            SampleManager.removeSample("pad" + requestCode + 1, true);
            loadWAVPath("pad" + requestCode + 1, file);
            manager.getController(pager.getCurrentItem()).updateEvents();
        }
    }

}