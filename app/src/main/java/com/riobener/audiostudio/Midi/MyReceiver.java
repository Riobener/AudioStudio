package com.riobener.audiostudio.Midi;

import android.app.Activity;
import android.content.Context;
import android.media.midi.MidiReceiver;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.riobener.audiostudio.MainActivity;

import java.io.IOException;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MyReceiver extends MidiReceiver {
    Context context;
    public MyReceiver(Context context){
        this.context = context;
    }
    public void onSend(byte[] data, int offset,
                       int count, long timestamp) throws IOException {
        Activity activity = (Activity)context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(data[1]==-112){
                    MainActivity.playNote(data[2]+1-36);
                }else if(data[1]==-128){
                    MainActivity.stopNote(data[2]+1-36);
                }
        }
        });

    }
}