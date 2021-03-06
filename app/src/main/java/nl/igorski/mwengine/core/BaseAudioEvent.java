/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package nl.igorski.mwengine.core;

public class BaseAudioEvent {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected BaseAudioEvent(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(BaseAudioEvent obj) {
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
        MWEngineCoreJNI.delete_BaseAudioEvent(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public BaseAudioEvent(BaseInstrument instrument) {
    this(MWEngineCoreJNI.new_BaseAudioEvent__SWIG_0(BaseInstrument.getCPtr(instrument), instrument), true);
  }

  public BaseAudioEvent() {
    this(MWEngineCoreJNI.new_BaseAudioEvent__SWIG_1(), true);
  }

  public float getVolume() {
    return MWEngineCoreJNI.BaseAudioEvent_getVolume(swigCPtr, this);
  }

  public float getVolumeLogarithmic() {
    return MWEngineCoreJNI.BaseAudioEvent_getVolumeLogarithmic(swigCPtr, this);
  }

  public void setVolume(float value) {
    MWEngineCoreJNI.BaseAudioEvent_setVolume(swigCPtr, this, value);
  }

  public BaseInstrument getInstrument() {
    long cPtr = MWEngineCoreJNI.BaseAudioEvent_getInstrument(swigCPtr, this);
    return (cPtr == 0) ? null : new BaseInstrument(cPtr, false);
  }

  public void setInstrument(BaseInstrument aInstrument) {
    MWEngineCoreJNI.BaseAudioEvent_setInstrument(swigCPtr, this, BaseInstrument.getCPtr(aInstrument), aInstrument);
  }

  public void play() {
    MWEngineCoreJNI.BaseAudioEvent_play(swigCPtr, this);
  }

  public void stop() {
    MWEngineCoreJNI.BaseAudioEvent_stop(swigCPtr, this);
  }

  public void resetPlayState() {
    MWEngineCoreJNI.BaseAudioEvent_resetPlayState(swigCPtr, this);
  }

  public void addToSequencer() {
    MWEngineCoreJNI.BaseAudioEvent_addToSequencer(swigCPtr, this);
  }

  public void removeFromSequencer() {
    MWEngineCoreJNI.BaseAudioEvent_removeFromSequencer(swigCPtr, this);
  }

  public void setIsSequenced(boolean value) {
    MWEngineCoreJNI.BaseAudioEvent_isSequenced_set(swigCPtr, this, value);
  }

  public boolean getIsSequenced() {
    return MWEngineCoreJNI.BaseAudioEvent_isSequenced_get(swigCPtr, this);
  }

  public void setEventLength(int value) {
    MWEngineCoreJNI.BaseAudioEvent_setEventLength(swigCPtr, this, value);
  }

  public void setEventStart(int value) {
    MWEngineCoreJNI.BaseAudioEvent_setEventStart(swigCPtr, this, value);
  }

  public void setEventEnd(int value) {
    MWEngineCoreJNI.BaseAudioEvent_setEventEnd(swigCPtr, this, value);
  }

  public int getEventLength() {
    return MWEngineCoreJNI.BaseAudioEvent_getEventLength(swigCPtr, this);
  }

  public int getEventStart() {
    return MWEngineCoreJNI.BaseAudioEvent_getEventStart(swigCPtr, this);
  }

  public int getEventEnd() {
    return MWEngineCoreJNI.BaseAudioEvent_getEventEnd(swigCPtr, this);
  }

  public void setStartPosition(float value) {
    MWEngineCoreJNI.BaseAudioEvent_setStartPosition(swigCPtr, this, value);
  }

  public void setEndPosition(float value) {
    MWEngineCoreJNI.BaseAudioEvent_setEndPosition(swigCPtr, this, value);
  }

  public void setDuration(float value) {
    MWEngineCoreJNI.BaseAudioEvent_setDuration(swigCPtr, this, value);
  }

  public float getStartPosition() {
    return MWEngineCoreJNI.BaseAudioEvent_getStartPosition(swigCPtr, this);
  }

  public float getEndPosition() {
    return MWEngineCoreJNI.BaseAudioEvent_getEndPosition(swigCPtr, this);
  }

  public float getDuration() {
    return MWEngineCoreJNI.BaseAudioEvent_getDuration(swigCPtr, this);
  }

  public void positionEvent(int startMeasure, int subdivisions, int offset) {
    MWEngineCoreJNI.BaseAudioEvent_positionEvent(swigCPtr, this, startMeasure, subdivisions, offset);
  }

  public void repositionToTempoChange(float ratio) {
    MWEngineCoreJNI.BaseAudioEvent_repositionToTempoChange(swigCPtr, this, ratio);
  }

  public boolean isDeletable() {
    return MWEngineCoreJNI.BaseAudioEvent_isDeletable(swigCPtr, this);
  }

  public void setDeletable(boolean value) {
    MWEngineCoreJNI.BaseAudioEvent_setDeletable(swigCPtr, this, value);
  }

  public boolean isEnabled() {
    return MWEngineCoreJNI.BaseAudioEvent_isEnabled(swigCPtr, this);
  }

  public void setEnabled(boolean value) {
    MWEngineCoreJNI.BaseAudioEvent_setEnabled(swigCPtr, this, value);
  }

  public void lock() {
    MWEngineCoreJNI.BaseAudioEvent_lock(swigCPtr, this);
  }

  public void unlock() {
    MWEngineCoreJNI.BaseAudioEvent_unlock(swigCPtr, this);
  }

  public boolean isLocked() {
    return MWEngineCoreJNI.BaseAudioEvent_isLocked(swigCPtr, this);
  }

}
