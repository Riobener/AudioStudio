#include <jni.h>
#include <oboe/Oboe.h>
#include "AudioEngine.h"
#include "Synth.h"

using namespace oboe;

AudioEngine audioEngine;
extern "C"
JNIEXPORT void JNICALL
Java_com_riobener_audiostudio_MainActivity_initialize(JNIEnv *env, jobject thiz) {
    audioEngine.startAudio();

}extern "C"
JNIEXPORT void JNICALL
Java_com_riobener_audiostudio_MainActivity_tap(JNIEnv *env, jobject thiz, jboolean i) {
    audioEngine.setWaveOn(i);
}