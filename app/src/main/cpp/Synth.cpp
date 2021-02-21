
#include "Synth.h"
#include <android/log.h>



void Synth::setFrequency(float freq) {
    kFrequency = freq;

}

void Synth::setAmplitude(float amp) {
    kAmplitude = amp;
}



void Synth::makeSound(float *targetData, int32_t numFrames, int32_t mTotalFrames, int16_t* mData ){
    if (isWaveOn){

        for (int i = 0; i < numFrames; ++i) {
            for (int j = 0; j < mChannelCount; ++j) {
                targetData[(i*mChannelCount)+j] = mData[(mReadFrameIndex*mChannelCount)+j];
            }

            // Increment and handle wraparound
            if (++mReadFrameIndex >= mTotalFrames) mReadFrameIndex = 0;

        }

    } /*else {
        // fill with zeros to output silence
        for (int i = 0; i < numFrames * mChannelCount; ++i) {
            targetData[i] = 0;
        }
    }*/
    /*//__android_log_print(ANDROID_LOG_INFO, "MyTag", "The value of frame %d", audioStream->getFramesPerBurst());
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
    }*/

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

