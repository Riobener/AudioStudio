/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package nl.igorski.mwengine.core;

public class CachedProperties {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CachedProperties(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CachedProperties obj) {
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
        MWEngineCoreJNI.delete_CachedProperties(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setEnvelope(double value) {
    MWEngineCoreJNI.CachedProperties_envelope_set(swigCPtr, this, value);
  }

  public double getEnvelope() {
    return MWEngineCoreJNI.CachedProperties_envelope_get(swigCPtr, this);
  }

  public void setReleaseLevel(double value) {
    MWEngineCoreJNI.CachedProperties_releaseLevel_set(swigCPtr, this, value);
  }

  public double getReleaseLevel() {
    return MWEngineCoreJNI.CachedProperties_releaseLevel_get(swigCPtr, this);
  }

  public void setEnvelopeOffset(int value) {
    MWEngineCoreJNI.CachedProperties_envelopeOffset_set(swigCPtr, this, value);
  }

  public int getEnvelopeOffset() {
    return MWEngineCoreJNI.CachedProperties_envelopeOffset_get(swigCPtr, this);
  }

  public void setPhaseIncr(double value) {
    MWEngineCoreJNI.CachedProperties_phaseIncr_set(swigCPtr, this, value);
  }

  public double getPhaseIncr() {
    return MWEngineCoreJNI.CachedProperties_phaseIncr_get(swigCPtr, this);
  }

  public void setArpeggioPosition(int value) {
    MWEngineCoreJNI.CachedProperties_arpeggioPosition_set(swigCPtr, this, value);
  }

  public int getArpeggioPosition() {
    return MWEngineCoreJNI.CachedProperties_arpeggioPosition_get(swigCPtr, this);
  }

  public void setArpeggioStep(int value) {
    MWEngineCoreJNI.CachedProperties_arpeggioStep_set(swigCPtr, this, value);
  }

  public int getArpeggioStep() {
    return MWEngineCoreJNI.CachedProperties_arpeggioStep_get(swigCPtr, this);
  }

  public void setOscillatorPhases(SWIGTYPE_p_std__vectorT_double_t value) {
    MWEngineCoreJNI.CachedProperties_oscillatorPhases_set(swigCPtr, this, SWIGTYPE_p_std__vectorT_double_t.getCPtr(value));
  }

  public SWIGTYPE_p_std__vectorT_double_t getOscillatorPhases() {
    return new SWIGTYPE_p_std__vectorT_double_t(MWEngineCoreJNI.CachedProperties_oscillatorPhases_get(swigCPtr, this), true);
  }

  public CachedProperties() {
    this(MWEngineCoreJNI.new_CachedProperties(), true);
  }

}
