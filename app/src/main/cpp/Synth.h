
#ifndef AUDIOSTUDIO_SYNTH_H
#define AUDIOSTUDIO_SYNTH_H
#include <oboe/Oboe.h>
#include <math.h>
#include <cstdint>
#include <atomic>
#include <memory>

class Synth {
public:
    double sec(float frames);
    void makeSound(float* audioData, int32_t frames);
    void setAmplitude(float amp);
    void setFrequency(float freq);
    void setWaveOn(bool state);

private:


    // Stream params
    static int constexpr kChannelCount = 1;
    static int constexpr kSampleRate = 44100;
    static float constexpr kPI = M_PI;
    static float constexpr kTwoPi = kPI * 2;
    float kAmplitude = 0.9f;
    float kFrequency = 440;
    double  mPhaseIncrement = kFrequency * kTwoPi / (double) kSampleRate;
    float mPhase = 0.0;

    bool isWaveOn = false;
};

#endif //AUDIOSTUDIO_SYNTH_H
