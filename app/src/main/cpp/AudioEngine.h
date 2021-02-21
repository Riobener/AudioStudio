//
// Created by Asus on 13.02.2021.
//

#ifndef AUDIOSTUDIO_AUDIOENGINE_H
#define AUDIOSTUDIO_AUDIOENGINE_H
#include <oboe/Oboe.h>
#include <jni.h>
#include "Synth.h"
#include <android/asset_manager.h>
using namespace oboe;

class AudioEngine: AudioStreamCallback {
public:
    void startAudio();
    void setWaveOn(bool);
    void resetSongPos(bool);
    void setWaveType(int i);
    void setBpm(int i);
    void loadSample(AAssetManager* manager);

private:
    AAssetManager *assetManager{nullptr};
    AAsset* mAsset;
    AudioStreamBuilder builder;
    DataCallbackResult
    onAudioReady(AudioStream *audioStream, void *audioData, int32_t numFrames) override;
    Synth synth;
    AudioStream* stream;
};


#endif //AUDIOSTUDIO_AUDIOENGINE_H
