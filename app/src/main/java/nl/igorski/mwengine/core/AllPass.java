/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package nl.igorski.mwengine.core;

public class AllPass {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected AllPass(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(AllPass obj) {
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
        MWEngineCoreJNI.delete_AllPass(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public AllPass() {
    this(MWEngineCoreJNI.new_AllPass(), true);
  }

  public void setBuffer(SWIGTYPE_p_SAMPLE_TYPE buf, int size) {
    MWEngineCoreJNI.AllPass_setBuffer(swigCPtr, this, SWIGTYPE_p_SAMPLE_TYPE.getCPtr(buf), size);
  }

  public SWIGTYPE_p_SAMPLE_TYPE process(SWIGTYPE_p_SAMPLE_TYPE input) {
    return new SWIGTYPE_p_SAMPLE_TYPE(MWEngineCoreJNI.AllPass_process(swigCPtr, this, SWIGTYPE_p_SAMPLE_TYPE.getCPtr(input)), true);
  }

  public void mute() {
    MWEngineCoreJNI.AllPass_mute(swigCPtr, this);
  }

  public SWIGTYPE_p_SAMPLE_TYPE getFeedback() {
    return new SWIGTYPE_p_SAMPLE_TYPE(MWEngineCoreJNI.AllPass_getFeedback(swigCPtr, this), true);
  }

  public void setFeedback(SWIGTYPE_p_SAMPLE_TYPE val) {
    MWEngineCoreJNI.AllPass_setFeedback(swigCPtr, this, SWIGTYPE_p_SAMPLE_TYPE.getCPtr(val));
  }

}