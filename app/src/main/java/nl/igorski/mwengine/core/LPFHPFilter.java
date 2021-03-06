/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package nl.igorski.mwengine.core;

public class LPFHPFilter extends BaseProcessor {
  private transient long swigCPtr;

  protected LPFHPFilter(long cPtr, boolean cMemoryOwn) {
    super(MWEngineCoreJNI.LPFHPFilter_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(LPFHPFilter obj) {
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
        MWEngineCoreJNI.delete_LPFHPFilter(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public LPFHPFilter(float aLPCutoff, float aHPCutoff, int amountOfChannels) {
    this(MWEngineCoreJNI.new_LPFHPFilter(aLPCutoff, aHPCutoff, amountOfChannels), true);
  }

  public String getType() {
    return MWEngineCoreJNI.LPFHPFilter_getType(swigCPtr, this);
  }

  public void setLPF(float aCutOffFrequency, int aSampleRate) {
    MWEngineCoreJNI.LPFHPFilter_setLPF(swigCPtr, this, aCutOffFrequency, aSampleRate);
  }

  public void setHPF(float aCutOffFrequency, int aSampleRate) {
    MWEngineCoreJNI.LPFHPFilter_setHPF(swigCPtr, this, aCutOffFrequency, aSampleRate);
  }

}
