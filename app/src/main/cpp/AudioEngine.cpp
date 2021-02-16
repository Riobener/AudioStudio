
#include "AudioEngine.h"
#include "Synth.h"
#include <android/log.h>
static constexpr int64_t kMillisecondsInSecond = 1000;
static constexpr int64_t kNanosecondsInMillisecond = 1000000;
std::atomic<int64_t> mCurrentFrame { 0 };
int64_t mSongPositionMs { 0 };
constexpr int64_t convertFramesToMillis(const int64_t frames, const int sampleRate){
    return static_cast<int64_t>((static_cast<double>(frames)/ sampleRate) * kMillisecondsInSecond);
}
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
    for (int i = 0; i < numFrames; ++i) {

        mSongPositionMs = convertFramesToMillis(
                mCurrentFrame,
                audioStream->getSampleRate());
        if(mSongPositionMs>3000&&mSongPositionMs<6000){
            setWaveOn(true);
        }else{
            setWaveOn(false);
        }
        //__android_log_print(ANDROID_LOG_INFO, "MyTag", "The value of frame %d", mSongPositionMs);

        mCurrentFrame++;
        synth.makeSound(static_cast<float *>(audioData), numFrames);
    }

    return DataCallbackResult::Continue;
}

void AudioEngine::setWaveOn(bool i) {
    synth.setWaveOn(i);
}
