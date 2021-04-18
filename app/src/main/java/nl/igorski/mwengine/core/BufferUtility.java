/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package nl.igorski.mwengine.core;

public class BufferUtility {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected BufferUtility(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(BufferUtility obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        MWEngineCoreJNI.delete_BufferUtility(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static int bufferToMilliseconds(int bufferSize, int sampleRate) {
    return MWEngineCoreJNI.BufferUtility_bufferToMilliseconds(bufferSize, sampleRate);
  }

  public static int millisecondsToBuffer(int milliSeconds, int sampleRate) {
    return MWEngineCoreJNI.BufferUtility_millisecondsToBuffer(milliSeconds, sampleRate);
  }

  public static float bufferToSeconds(int bufferSize, int sampleRate) {
    return MWEngineCoreJNI.BufferUtility_bufferToSeconds(bufferSize, sampleRate);
  }

  public static int secondsToBuffer(float seconds, int sampleRate) {
    return MWEngineCoreJNI.BufferUtility_secondsToBuffer(seconds, sampleRate);
  }

  public static int getBitRate(int sampleRate, int bitDepth, int channels) {
    return MWEngineCoreJNI.BufferUtility_getBitRate(sampleRate, bitDepth, channels);
  }

  public static int calculateBufferLength(SWIGTYPE_p_SAMPLE_TYPE aMinRate) {
    return MWEngineCoreJNI.BufferUtility_calculateBufferLength__SWIG_0(SWIGTYPE_p_SAMPLE_TYPE.getCPtr(aMinRate));
  }

  public static int calculateBufferLength(int milliSeconds) {
    return MWEngineCoreJNI.BufferUtility_calculateBufferLength__SWIG_1(milliSeconds);
  }

  public static int calculateSamplesPerBeatDivision(int sampleRate, double tempo, int subdivision) {
    return MWEngineCoreJNI.BufferUtility_calculateSamplesPerBeatDivision(sampleRate, tempo, subdivision);
  }

  public static int getSamplesPerBeat(int sampleRate, double tempo) {
    return MWEngineCoreJNI.BufferUtility_getSamplesPerBeat(sampleRate, tempo);
  }

  public static int getSamplesPerBar(int sampleRate, double tempo, int beatAmount, int beatUnit) {
    return MWEngineCoreJNI.BufferUtility_getSamplesPerBar(sampleRate, tempo, beatAmount, beatUnit);
  }

  public static double getBPMbyLength(double length, int amountOfBars) {
    return MWEngineCoreJNI.BufferUtility_getBPMbyLength(length, amountOfBars);
  }

  public static double getBPMbySamples(int length, int amountOfBars, int sampleRate) {
    return MWEngineCoreJNI.BufferUtility_getBPMbySamples(length, amountOfBars, sampleRate);
  }

  public static SWIGTYPE_p_std__vectorT_SAMPLE_TYPE_p_t createSampleBuffers(int amountOfChannels, int bufferSize) {
    long cPtr = MWEngineCoreJNI.BufferUtility_createSampleBuffers(amountOfChannels, bufferSize);
    return (cPtr == 0) ? null : new SWIGTYPE_p_std__vectorT_SAMPLE_TYPE_p_t(cPtr, false);
  }

  public static SWIGTYPE_p_SAMPLE_TYPE generateSilentBuffer(int bufferSize) {
    long cPtr = MWEngineCoreJNI.BufferUtility_generateSilentBuffer(bufferSize);
    return (cPtr == 0) ? null : new SWIGTYPE_p_SAMPLE_TYPE(cPtr, false);
  }

  public static void bufferToFile(String filename, SWIGTYPE_p_SAMPLE_TYPE buffer, int bufferLength) {
    MWEngineCoreJNI.BufferUtility_bufferToFile(filename, SWIGTYPE_p_SAMPLE_TYPE.getCPtr(buffer), bufferLength);
  }

  public BufferUtility() {
    this(MWEngineCoreJNI.new_BufferUtility(), true);
  }

}