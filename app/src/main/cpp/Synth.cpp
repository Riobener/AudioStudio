
#include "Synth.h"

void Synth::setFrequency(float freq) {
    kFrequency = freq;
}

void Synth::setAmplitude(float amp) {
    kAmplitude = amp;
}

void Synth::makeSound(float *audioData, int32_t frames) {
    if(isWaveOn){
        for (int i = 0; i < frames; ++i) {
            float sampleValue = kAmplitude * sinf(mPhase);
            for (int j = 0; j < kChannelCount; j++) {
                audioData[i * kChannelCount + j] = sampleValue;
            }
            mPhase += mPhaseIncrement;
            if (mPhase >= kTwoPi) mPhase -= kTwoPi;
        }
    }else{
        memset(audioData,0,frames*sizeof(float));
    }

}

void Synth::setWaveOn(bool state) {
     isWaveOn = state;
}
