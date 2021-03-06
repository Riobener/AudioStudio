/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package nl.igorski.mwengine.core;

public class Delay extends BaseProcessor {
  private transient long swigCPtr;

  protected Delay(long cPtr, boolean cMemoryOwn) {
    super(MWEngineCoreJNI.Delay_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Delay obj) {
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
        MWEngineCoreJNI.delete_Delay(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public Delay(int aDelayTime, int aMaxDelayTime, float aMix, float aFeedback, int amountOfChannels) {
    this(MWEngineCoreJNI.new_Delay(aDelayTime, aMaxDelayTime, aMix, aFeedback, amountOfChannels), true);
  }

  public String getType() {
    return MWEngineCoreJNI.Delay_getType(swigCPtr, this);
  }

  public int getDelayTime() {
    return MWEngineCoreJNI.Delay_getDelayTime(swigCPtr, this);
  }

  public void setDelayTime(int aValue) {
    MWEngineCoreJNI.Delay_setDelayTime(swigCPtr, this, aValue);
  }

  public float getMix() {
    return MWEngineCoreJNI.Delay_getMix(swigCPtr, this);
  }

  public void setMix(float aValue) {
    MWEngineCoreJNI.Delay_setMix(swigCPtr, this, aValue);
  }

  public float getFeedback() {
    return MWEngineCoreJNI.Delay_getFeedback(swigCPtr, this);
  }

  public void setFeedback(float aValue) {
    MWEngineCoreJNI.Delay_setFeedback(swigCPtr, this, aValue);
  }

  public void reset() {
    MWEngineCoreJNI.Delay_reset(swigCPtr, this);
  }

}
