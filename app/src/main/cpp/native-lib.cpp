
#include <jni.h>
#include <string>
#include <android/log.h>
#include <OpenSource/SuperpoweredAndroidAudioIO.h>
#include <Superpowered.h>
#include <SuperpoweredAdvancedAudioPlayer.h>
#include <SuperpoweredSimple.h>
#include <SuperpoweredCPU.h>
#include <malloc.h>
#include <SLES/OpenSLES_AndroidConfiguration.h>
#include <SLES/OpenSLES.h>

#define log_print __android_log_print

static SuperpoweredAndroidAudioIO *audioIO;
static Superpowered::AdvancedAudioPlayer *player;

// This is called periodically by the audio engine.
static bool audioProcessing (
        void * __unused clientdata, // custom pointer
        short int *audio,           // output buffer
        int numberOfFrames,         // number of frames to process
        int samplerate              // current sample rate in Hz
) {
    player->outputSamplerate = (unsigned int)samplerate;
    float playerOutput[numberOfFrames * 2];

    if (player->processStereo(playerOutput, false, (unsigned int)numberOfFrames)) {
        Superpowered::FloatToShortInt(playerOutput, audio, (unsigned int)numberOfFrames);
        return true;
    } else return false;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_riobener_audiostudio_MainActivity_NativeInit(JNIEnv *env, jobject thiz, jint samplerate,
                                                      jint buffersize, jstring temp_path) {
    Superpowered::Initialize(
            "ExampleLicenseKey-WillExpire-OnNextUpdate",
            false, // enableAudioAnalysis (using SuperpoweredAnalyzer, SuperpoweredLiveAnalyzer, SuperpoweredWaveform or SuperpoweredBandpassFilterbank)
            false, // enableFFTAndFrequencyDomain (using SuperpoweredFrequencyDomain, SuperpoweredFFTComplex, SuperpoweredFFTReal or SuperpoweredPolarFFT)
            false, // enableAudioTimeStretching (using SuperpoweredTimeStretching)
            false, // enableAudioEffects (using any SuperpoweredFX class)
            true,  // enableAudioPlayerAndDecoder (using SuperpoweredAdvancedAudioPlayer or SuperpoweredDecoder)
            false, // enableCryptographics (using Superpowered::RSAPublicKey, Superpowered::RSAPrivateKey, Superpowered::hasher or Superpowered::AES)
            false  // enableNetworking (using Superpowered::httpRequest)
    );

    // setting the temp folder for progressive downloads or HLS playback
    // not needed for local file playback
    const char *str = env->GetStringUTFChars(temp_path, 0);
    Superpowered::AdvancedAudioPlayer::setTempFolder(str);
    env->ReleaseStringUTFChars(temp_path, str);

    // creating the player
    player = new Superpowered::AdvancedAudioPlayer((unsigned int)samplerate, 0);

    audioIO = new SuperpoweredAndroidAudioIO (
            samplerate,                     // device native sampling rate
            buffersize,                     // device native buffer size
            false,                          // enableInput
            true,                           // enableOutput
            audioProcessing,                // process callback function
            NULL,                           // clientData
            -1,                             // inputStreamType (-1 = default)
            SL_ANDROID_STREAM_MEDIA         // outputStreamType (-1 = default)
    );
}extern "C"
JNIEXPORT void JNICALL
Java_com_riobener_audiostudio_MainActivity_OpenFileFromAPK(JNIEnv *env, jobject thiz, jstring path,
                                                           jint offset, jint length) {
    const char *str = env->GetStringUTFChars(path, 0);
    player->open(str, offset, length);
    env->ReleaseStringUTFChars(path, str);

    // open file from any path: player->open("file system path to file");
    // open file from network (progressive download): player->open("http://example.com/music.mp3");
    // open HLS stream: player->openHLS("http://example.com/stream");
}extern "C"
JNIEXPORT jboolean JNICALL
Java_com_riobener_audiostudio_MainActivity_onUserInterfaceUpdate(JNIEnv *env, jobject thiz) {
    switch (player->getLatestEvent()) {
        case Superpowered::PlayerEvent_None:
        case Superpowered::PlayerEvent_Opening: break; // do nothing
        case Superpowered::PlayerEvent_Opened: player->play(); break;
        case Superpowered::PlayerEvent_OpenFailed:
        {
            int openError = player->getOpenErrorCode();
            log_print(ANDROID_LOG_ERROR, "PlayerExample", "Open error %i: %s", openError, Superpowered::AdvancedAudioPlayer::statusCodeToString(openError));
        }
            break;
        case Superpowered::PlayerEvent_ConnectionLost:
            log_print(ANDROID_LOG_ERROR, "PlayerExample", "Network download failed."); break;
        case Superpowered::PlayerEvent_ProgressiveDownloadFinished:
            log_print(ANDROID_LOG_ERROR, "PlayerExample", "Download finished. Path: %s", player->getFullyDownloadedFilePath()); break;
    }

    if (player->eofRecently()) player->setPosition(0, false, false);
    return (jboolean)player->isPlaying();
}extern "C"
JNIEXPORT void JNICALL
Java_com_riobener_audiostudio_MainActivity_TogglePlayback(JNIEnv *env, jobject thiz) {
    player->togglePlayback();
    Superpowered::CPU::setSustainedPerformanceMode(player->isPlaying()); // prevent dropouts
}extern "C"
JNIEXPORT void JNICALL
Java_com_riobener_audiostudio_MainActivity_onForeground(JNIEnv *env, jobject thiz) {
    audioIO->onForeground();
}extern "C"
JNIEXPORT void JNICALL
Java_com_riobener_audiostudio_MainActivity_onBackground(JNIEnv *env, jobject thiz) {
    audioIO->onBackground();
}extern "C"
JNIEXPORT void JNICALL
Java_com_riobener_audiostudio_MainActivity_Cleanup(JNIEnv *env, jobject thiz) {
    delete audioIO;
    delete player;
}