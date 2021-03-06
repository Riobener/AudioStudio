/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package nl.igorski.mwengine.core;

public class Flanger extends BaseProcessor {
  private transient long swigCPtr;

  protected Flanger(long cPtr, boolean cMemoryOwn) {
    super(MWEngineCoreJNI.Flanger_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Flanger obj) {
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
        MWEngineCoreJNI.delete_Flanger(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public Flanger(float rate, float width, float feedback, float delay, float mix) {
    this(MWEngineCoreJNI.new_Flanger__SWIG_0(rate, width, feedback, delay, mix), true);
  }

  public Flanger() {
    this(MWEngineCoreJNI.new_Flanger__SWIG_1(), true);
  }

  public String getType() {
    return MWEngineCoreJNI.Flanger_getType(swigCPtr, this);
  }

  public float getRate() {
    return MWEngineCoreJNI.Flanger_getRate(swigCPtr, this);
  }

  public void setRate(float value) {
    MWEngineCoreJNI.Flanger_setRate(swigCPtr, this, value);
  }

  public float getWidth() {
    return MWEngineCoreJNI.Flanger_getWidth(swigCPtr, this);
  }

  public void setWidth(float value) {
    MWEngineCoreJNI.Flanger_setWidth(swigCPtr, this, value);
  }

  public float getFeedback() {
    return MWEngineCoreJNI.Flanger_getFeedback(swigCPtr, this);
  }

  public void setFeedback(float value) {
    MWEngineCoreJNI.Flanger_setFeedback(swigCPtr, this, value);
  }

  public float getDelay() {
    return MWEngineCoreJNI.Flanger_getDelay(swigCPtr, this);
  }

  public void setDelay(float value) {
    MWEngineCoreJNI.Flanger_setDelay(swigCPtr, this, value);
  }

  public float getMix() {
    return MWEngineCoreJNI.Flanger_getMix(swigCPtr, this);
  }

  public void setMix(float value) {
    MWEngineCoreJNI.Flanger_setMix(swigCPtr, this, value);
  }

  public void setChannelMix(int channel, float wet) {
    MWEngineCoreJNI.Flanger_setChannelMix(swigCPtr, this, channel, wet);
  }

}
