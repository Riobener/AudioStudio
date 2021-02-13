//
// Created by Asus on 13.02.2021.
//

#include "AudioEngine.h"
#include "Synth.h"

void AudioEngine::startAudio() {
    AudioStreamBuilder builder;
    builder.setCallback(this);
    builder.setFormat(oboe::AudioFormat::Float);
    builder.setChannelCount(Stereo);
    builder.setPerformanceMode(oboe::PerformanceMode::LowLatency);
    builder.setSharingMode(oboe::SharingMode::Exclusive);

    builder.openStream(&stream);
    stream->setBufferSizeInFrames(stream->getFramesPerBurst()*2);
    stream->requestStart();

}

DataCallbackResult
AudioEngine::onAudioReady(AudioStream *audioStream, void *audioData, int32_t numFrames) {
    synth.makeSound(static_cast<float *>(audioData),numFrames);
    return DataCallbackResult::Continue;
}

void AudioEngine::setWaveOn(bool i) {
    synth.setWaveOn(i);
}
