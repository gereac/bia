package com.gcsf.codingrules;

import java.util.HashMap;
import java.util.Map;

//------------------------------------------------------------------------
public class StatisticsStorage {
  private Map<String, Integer> mySeverityCounter = null;

  // ----------------------------------------------------------------------
  public StatisticsStorage() {
    mySeverityCounter = new HashMap<String, Integer>();
  }

  // ----------------------------------------------------------------------
  public void countSeverity(String aSeverity) {
    Integer counter = mySeverityCounter.get(aSeverity);

    if (null == counter) {
      counter = new Integer(1);
    } else {
      counter++;
    }

    mySeverityCounter.put(aSeverity, counter);
  }

  // ----------------------------------------------------------------------
  public Map<String, Integer> getSeverityCounter() {
    return mySeverityCounter;
  }

  // ----------------------------------------------------------------------
  public String toString() {
    StringBuffer retval = new StringBuffer();

    for (String severity : mySeverityCounter.keySet()) {
      retval.append(severity);
      retval.append(": ");
      retval.append(mySeverityCounter.get(severity));
      retval.append("\n");
    }

    return retval.toString();
  }
}
