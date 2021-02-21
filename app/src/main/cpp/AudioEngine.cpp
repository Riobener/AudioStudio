
#include "AudioEngine.h"
#include "Synth.h"
#include <android/log.h>
static constexpr int64_t kMillisecondsInSecond = 1000;
std::atomic<int64_t> mCurrentFrame { 0 };
std::atomic<int64_t> mSongPositionMs { 0 };
bool isPlaying = false;
int32_t bpm = 120;
double_t metronomeInterval = 60000/bpm;
const int noteLen = 8;
double melody[noteLen] = {
        261.63, 293.66, 329.63, 349.23, 392.00, 440.00, 493.88, 523.25
};
int index = 0;
void updateInterval(){
    metronomeInterval+=60000/bpm;
    index++;

}
void zeroInterval(){
    mCurrentFrame=0;
    mSongPositionMs = 0;
    index = 0;
    metronomeInterval=60000/bpm;
}
constexpr int64_t convertFramesToMillis(const int64_t frames, const int sampleRate){
    return static_cast<int64_t>((static_cast<double>(frames)/ sampleRate) * kMillisecondsInSecond);
}


void AudioEngine::startAudio() {
    builder.setCallback(this);
    builder.setSampleRate(48000);
    builder.setFormat(oboe::AudioFormat::Float);
    builder.setChannelCount(Stereo);
    builder.setPerformanceMode(oboe::PerformanceMode::LowLatency);
    builder.setSharingMode(oboe::SharingMode::Exclusive);
    builder.openStream(&stream);
    stream->setBufferSizeInFrames(stream->getFramesPerBurst()*2);
}
int32_t mChannelCount = 2; // TODO: move this into a konstant and maybe add as parameter to ctor
int32_t mReadFrameIndex = 0;

int32_t numF;
int32_t mTotalFrames = numF;

int16_t * audioBuff;

DataCallbackResult
AudioEngine::onAudioReady(AudioStream *audioStream, void *audioData, int32_t numFrames) {




    synth.makeSound(static_cast<float *>(audioData), numFrames, numF,
                    audioBuff);

    /*  for (int i = 0; i < numFrames; ++i) {
      /*ongPositionMs = convertFramesToMillis(
              mCurrentFrame,
              audioStream->getSampleRate());
      if(mSongPositionMs>=metronomeInterval&&index<noteLen-1){
          __android_log_print(ANDROID_LOG_INFO, "MyTag", "NORM %d", index);
          synth.setFrequency(melody[index]);
          updateInterval();
      }else if(mSongPositionMs>=metronomeInterval&&index==noteLen-1){
          __android_log_print(ANDROID_LOG_INFO, "MyTag", "LAST %d", index);
          synth.setFrequency(melody[index]);
          zeroInterval();
      }

      mCurrentFrame++;
      synth.makeSound(static_cast<float *>(audioData), numFrames, i);
       }*/

    return DataCallbackResult::Continue;
}

void AudioEngine::setWaveOn(bool i) {
    synth.setWaveOn(i);
}

void AudioEngine::resetSongPos(bool state) {
    if(!state){
        zeroInterval();
        stream->stop();
    }else{
        zeroInterval();
        stream->start();
    }

}

void AudioEngine::setWaveType(int i) {
    synth.setWaveType(i);
}

void AudioEngine::setBpm(int i) {
    bpm=i;
}

void AudioEngine::loadSample(AAssetManager *manager) {

    // Load the backing track
    AAsset* asset = AAssetManager_open(manager, "Sound.wav", AASSET_MODE_BUFFER);
    // Get the length of the track (we assume it is stereo 48kHz)
    off_t trackLength = AAsset_getLength(asset);
    // Load it into memory
    audioBuff = (int16_t *) AAsset_getBuffer(asset);
// There are 4 bytes per frame because
    // each sample is 2 bytes and
    // it's a stereo recording which has 2 samples per frame.
    numF = static_cast<int32_t>(trackLength / 4);
}
