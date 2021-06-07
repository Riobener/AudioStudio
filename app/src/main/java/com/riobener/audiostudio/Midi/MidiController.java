package com.riobener.audiostudio.Midi;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;
import android.media.midi.MidiReceiver;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.riobener.audiostudio.MainActivity;

import java.io.IOException;

public class MidiController {
    MidiManager manager;
    Context context;
    MidiDeviceInfo[] infos;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public MidiController(Context context){
        this.context = context;
        manager = (MidiManager)context.getSystemService(Context.MIDI_SERVICE);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setupMidi(){
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            //Toast.makeText(MainActivity.this,"Устройство "+" добавлено!",Toast.LENGTH_LONG).show();
            infos = manager.getDevices();
            manager.registerDeviceCallback(new MidiManager.DeviceCallback() {
                public void onDeviceAdded( MidiDeviceInfo info ) {
                    int numInputs = info.getInputPortCount();
                    int numOutputs = info.getOutputPortCount();
                    Bundle properties = info.getProperties();
                    String manufacturer = properties
                            .getString(MidiDeviceInfo.PROPERTY_MANUFACTURER);
                    Toast.makeText(context,"Устройство "+manufacturer+" добавлено!",Toast.LENGTH_LONG).show();
                    manager.openDevice(info, device -> {
                        if (device == null) {
                            Toast.makeText(context,"MIDI " +info,Toast.LENGTH_SHORT).show();
                        } else {
                            MidiOutputPort outputPort = device.openOutputPort(0);
                            outputPort.connect(new MyReceiver(context));
                        }
                    }, new Handler(Looper.getMainLooper()));
                }
                public void onDeviceRemoved( MidiDeviceInfo info ) {

                }
            }, new Handler(Looper.getMainLooper()));

        }
    }

    public MidiManager getManager() {
        return manager;
    }

    public MidiDeviceInfo[] getInfos() {
        return infos;
    }
}
