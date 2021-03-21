/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package nl.igorski.mwengine.core;

public class PitchShifter extends BaseProcessor {
  private transient long swigCPtr;

  protected PitchShifter(long cPtr, boolean cMemoryOwn) {
    super(MWEngineCoreJNI.PitchShifter_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(PitchShifter obj) {
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
        MWEngineCoreJNI.delete_PitchShifter(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public PitchShifter(float shiftAmount, int osampAmount) {
    this(MWEngineCoreJNI.new_PitchShifter(shiftAmount, osampAmount), true);
  }

  public String getType() {
    return MWEngineCoreJNI.PitchShifter_getType(swigCPtr, this);
  }

  public void setPitchShift(float value) {
    MWEngineCoreJNI.PitchShifter_pitchShift_set(swigCPtr, this, value);
  }

  public float getPitchShift() {
    return MWEngineCoreJNI.PitchShifter_pitchShift_get(swigCPtr, this);
  }

}
