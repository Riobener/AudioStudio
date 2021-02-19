
#include "Synth.h"
#include <android/log.h>



void Synth::setFrequency(float freq) {
    kFrequency = freq;

}

void Synth::setAmplitude(float amp) {
    kAmplitude = amp;
}

void Synth::makeSound(float *audioData, int32_t frames, int32_t i) {

    //__android_log_print(ANDROID_LOG_INFO, "MyTag", "The value of frame %d", audioStream->getFramesPerBurst());
    if (isWaveOn) {
        updatePhaseInc();
        float sampleValue;
       //square
        switch (waveType) {
            case 1:
                sampleValue = kAmplitude * sinf(mPhase);
                break;
            case 2:
                if (mPhase <= kPI) {
                    sampleValue = -kAmplitude;
                } else {
                    sampleValue = kAmplitude;
                }
                break;
        }

        for (int j = 0; j < kChannelCount; j++) {
            audioData[i * kChannelCount + j] = sampleValue;
        }

        mPhase += mPhaseIncrement;
        if (mPhase >= kTwoPi) mPhase -= kTwoPi;

    } else {
        memset(audioData, 0, frames * sizeof(float) * kChannelCount);
    }

}

void Synth::setWaveOn(bool state) {
    isWaveOn = state;
}

void Synth::setWaveType(int i) {
        waveType=i;
}

void Synth::updatePhaseInc() {
    mPhaseIncrement = kFrequency * kTwoPi / (double) kSampleRate;
}

bool Synth::getWaveStatus() {
    return isWaveOn;
}

