/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package nl.igorski.mwengine.core;

public class cachedSample {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected cachedSample(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(cachedSample obj) {
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
        MWEngineCoreJNI.delete_cachedSample(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setSampleLength(int value) {
    MWEngineCoreJNI.cachedSample_sampleLength_set(swigCPtr, this, value);
  }

  public int getSampleLength() {
    return MWEngineCoreJNI.cachedSample_sampleLength_get(swigCPtr, this);
  }

  public void setSampleRate(long value) {
    MWEngineCoreJNI.cachedSample_sampleRate_set(swigCPtr, this, value);
  }

  public long getSampleRate() {
    return MWEngineCoreJNI.cachedSample_sampleRate_get(swigCPtr, this);
  }

  public void setSampleBuffer(SWIGTYPE_p_AudioBuffer value) {
    MWEngineCoreJNI.cachedSample_sampleBuffer_set(swigCPtr, this, SWIGTYPE_p_AudioBuffer.getCPtr(value));
  }

  public SWIGTYPE_p_AudioBuffer getSampleBuffer() {
    long cPtr = MWEngineCoreJNI.cachedSample_sampleBuffer_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_AudioBuffer(cPtr, false);
  }

  public cachedSample() {
    this(MWEngineCoreJNI.new_cachedSample(), true);
  }

}
