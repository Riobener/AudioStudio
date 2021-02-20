#ifndef AUDIOSTUDIO_IRENDERABLEAUDIO_H
#define AUDIOSTUDIO_IRENDERABLEAUDIO_H


#include <cstdint>
#include <string>
#include "AudioEngine.h"

class IRenderableAudio {

public:
    virtual ~IRenderableAudio() = default;
    virtual void renderAudio(float *audioData, int32_t numFrames) = 0;
};


#endif //AUDIOSTUDIO_IRENDERABLEAUDIO_H
