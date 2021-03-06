/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package nl.igorski.mwengine.core;

public class Notifications {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected Notifications(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Notifications obj) {
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
        MWEngineCoreJNI.delete_Notifications(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public Notifications() {
    this(MWEngineCoreJNI.new_Notifications(), true);
  }

  public enum ids {
    SEQUENCER_POSITION_UPDATED,
    MARKER_POSITION_REACHED,
    SEQUENCER_TEMPO_UPDATED,
    RECORDED_SNIPPET_READY,
    RECORDED_SNIPPET_SAVED,
    RECORDING_COMPLETED,
    BOUNCE_COMPLETE,
    STATUS_BRIDGE_CONNECTED,
    ERROR_HARDWARE_UNAVAILABLE,
    ERROR_THREAD_START;

    public final int swigValue() {
      return swigValue;
    }

    public static ids swigToEnum(int swigValue) {
      ids[] swigValues = ids.class.getEnumConstants();
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (ids swigEnum : swigValues)
        if (swigEnum.swigValue == swigValue)
          return swigEnum;
      throw new IllegalArgumentException("No enum " + ids.class + " with value " + swigValue);
    }

    @SuppressWarnings("unused")
    private ids() {
      this.swigValue = SwigNext.next++;
    }

    @SuppressWarnings("unused")
    private ids(int swigValue) {
      this.swigValue = swigValue;
      SwigNext.next = swigValue+1;
    }

    @SuppressWarnings("unused")
    private ids(ids swigEnum) {
      this.swigValue = swigEnum.swigValue;
      SwigNext.next = this.swigValue+1;
    }

    private final int swigValue;

    private static class SwigNext {
      private static int next = 0;
    }
  }

}
