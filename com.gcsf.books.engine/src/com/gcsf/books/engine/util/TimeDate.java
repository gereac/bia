package com.gcsf.books.engine.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;


/**
 * Time and Date
 *
 */
public class TimeDate {
  /**
   * @return String
   */
  public static String getTimeAndDate() {
    DateFormat df;
    GregorianCalendar cal = new GregorianCalendar();
    df = DateFormat.getDateTimeInstance();
    return df.format(cal.getTime());
  }
  
  /**
   * @param aSimpleDateFormat String
   * @return String
   */
  public static String getDate(String aSimpleDateFormat) {
    SimpleDateFormat df;
    GregorianCalendar cal = new GregorianCalendar();
    df = new SimpleDateFormat(aSimpleDateFormat);
    return df.format(cal.getTime());
  }
}
