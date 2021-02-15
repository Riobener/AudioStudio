
#include "Synth.h"
#include <android/log.h>
double Synth::sec(float a) {
    return a * kSampleRate;
}

void Synth::setFrequency(float freq) {
    kFrequency = freq;
}

void Synth::setAmplitude(float amp) {
    kAmplitude = amp;
}

void Synth::makeSound(float *audioData, int32_t frames) {

    play = false;
    //__android_log_print(ANDROID_LOG_INFO, "MyTag", "The value of frame %d", audioStream->getFramesPerBurst());
    if(isWaveOn){
        for (int i = 0; i < frames; ++i) {
            if(++metronomeCounter>=metronomeInterval){
                metronomeCounter = 0;
                play = true;
            }
            if(play){
                float sampleValue = kAmplitude * sinf(mPhase);
                for (int j = 0; j < kChannelCount; j++) {
                    audioData[i * kChannelCount + j] = sampleValue;
                }
                mPhase += mPhaseIncrement;

                if (mPhase >= kTwoPi) mPhase -= kTwoPi;
            }else{
                memset(audioData,0,frames*sizeof(float)*kChannelCount);memset(audioData,0,frames*sizeof(float)*kChannelCount);
            }
        }

    }else{

        memset(audioData,0,frames*sizeof(float)*kChannelCount);
    }

}

void Synth::setWaveOn(bool state) {
     isWaveOn = state;
}
