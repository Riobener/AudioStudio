
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

    //__android_log_print(ANDROID_LOG_INFO, "MyTag", "The value of frame %d", audioStream->getFramesPerBurst());
    if(isWaveOn){

        for (int i = 0; i < frames; ++i) {
            /*float sampleValue;
            if (mPhase <= kPI){
                sampleValue = -kAmplitude;
            } else {
                sampleValue= kAmplitude;
            }*/

            float sampleValue = kAmplitude * sinf(mPhase);
            for (int j = 0; j < kChannelCount; j++) {
                audioData[i * kChannelCount + j] = sampleValue;
            }
            mPhase += mPhaseIncrement;

            if (mPhase >= kTwoPi) mPhase -= kTwoPi;
            /*if(++metronomeCounter>=metronomeInterval){
                __android_log_print(ANDROID_LOG_INFO, "MyTag", "The value of cp %d", metronomeCounter);
                metronomeCounter = 0;
                play = true;
            }*/
            /*if(play){
                float sampleValue;
                if (mPhase <= kPI){
                    sampleValue = -kAmplitude;
                } else {
                    sampleValue= kAmplitude;
                }

                //float sampleValue = kAmplitude * sinf(mPhase);
                for (int j = 0; j < kChannelCount; j++) {
                    audioData[i * kChannelCount + j] = sampleValue;
                }
                mPhase += mPhaseIncrement;

                if (mPhase >= kTwoPi) mPhase -= kTwoPi;
            }else{
                for (int j = 0; j < kChannelCount; j++) {
                    audioData[i * kChannelCount + j] = 0;
                }
            }*/
        }

    }else{
        memset(audioData,0,frames*sizeof(float)*kChannelCount);
    }

}

void Synth::setWaveOn(bool state) {
     isWaveOn = state;
}
