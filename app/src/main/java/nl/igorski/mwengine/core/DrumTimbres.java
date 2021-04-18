/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package nl.igorski.mwengine.core;

public class DrumTimbres {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected DrumTimbres(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(DrumTimbres obj) {
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
        MWEngineCoreJNI.delete_DrumTimbres(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public DrumTimbres() {
    this(MWEngineCoreJNI.new_DrumTimbres(), true);
  }

  public enum Timbres {
    LIGHT,
    GRAVEL;

    public final int swigValue() {
      return swigValue;
    }

    public static Timbres swigToEnum(int swigValue) {
      Timbres[] swigValues = Timbres.class.getEnumConstants();
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (Timbres swigEnum : swigValues)
        if (swigEnum.swigValue == swigValue)
          return swigEnum;
      throw new IllegalArgumentException("No enum " + Timbres.class + " with value " + swigValue);
    }

    @SuppressWarnings("unused")
    private Timbres() {
      this.swigValue = SwigNext.next++;
    }

    @SuppressWarnings("unused")
    private Timbres(int swigValue) {
      this.swigValue = swigValue;
      SwigNext.next = swigValue+1;
    }

    @SuppressWarnings("unused")
    private Timbres(Timbres swigEnum) {
      this.swigValue = swigEnum.swigValue;
      SwigNext.next = this.swigValue+1;
    }

    private final int swigValue;

    private static class SwigNext {
      private static int next = 0;
    }
  }

}