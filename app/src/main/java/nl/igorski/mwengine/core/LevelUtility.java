/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package nl.igorski.mwengine.core;

public class LevelUtility {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected LevelUtility(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(LevelUtility obj) {
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
        MWEngineCoreJNI.delete_LevelUtility(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static SWIGTYPE_p_SAMPLE_TYPE RMS(AudioChannel audioChannel, int channelNum) {
    return new SWIGTYPE_p_SAMPLE_TYPE(MWEngineCoreJNI.LevelUtility_RMS(AudioChannel.getCPtr(audioChannel), audioChannel, channelNum), true);
  }

  public static SWIGTYPE_p_SAMPLE_TYPE dBSPL(AudioChannel audioChannel, int channelNum) {
    return new SWIGTYPE_p_SAMPLE_TYPE(MWEngineCoreJNI.LevelUtility_dBSPL(AudioChannel.getCPtr(audioChannel), audioChannel, channelNum), true);
  }

  public static SWIGTYPE_p_SAMPLE_TYPE linear(AudioChannel audioChannel, int channelNum) {
    return new SWIGTYPE_p_SAMPLE_TYPE(MWEngineCoreJNI.LevelUtility_linear(AudioChannel.getCPtr(audioChannel), audioChannel, channelNum), true);
  }

  public LevelUtility() {
    this(MWEngineCoreJNI.new_LevelUtility(), true);
  }

}