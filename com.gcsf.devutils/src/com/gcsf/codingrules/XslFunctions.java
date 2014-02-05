package com.gcsf.codingrules;

//------------------------------------------------------------------------
public class XslFunctions {
  // ----------------------------------------------------------------------
  public static String replace(String aSourceString, String aRegularExpression,
      String aReplacement) {
    return aSourceString.replaceAll(aRegularExpression, aReplacement);
  }

  // ----------------------------------------------------------------------
  public static String countSeverity(String aComponent, String aSeverity) {
    XslTransformer.getInstance().countSeverity(aComponent, aSeverity);
    return aSeverity;
  }
}
