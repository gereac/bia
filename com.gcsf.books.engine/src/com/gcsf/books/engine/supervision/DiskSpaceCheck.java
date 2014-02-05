package com.gcsf.books.engine.supervision;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * @author ls21fnt
 * 
 */
public class DiskSpaceCheck {
  /**
   * Logger for this class
   */
  private static final Logger ourLogger = Logger
      .getLogger(DiskSpaceCheck.class);

  private static DiskSpaceCheck ourInstance = null;

  File myBaseDirectory = null;

  private long myMinimumDiskSpace = 100000000;

  private long myFreeSpace;

  public DiskSpaceCheck() {
    // try {
    // myBaseDirectory = new File(PropertyReader.getInstance().getValue(
    // PropertyReaderEnum.Global_Application_Path));
    // myMinimumDiskSpace = Long.parseLong(PropertyReader.getInstance()
    // .getValue(CoreProperty.Min_Application_Diskspace, "10000000"));
    // } catch (Throwable t) {
    // }
  }

  /**
   * @return ourInstance
   */
  synchronized public static DiskSpaceCheck getInstance() {
    if (ourInstance == null) {
      ourInstance = new DiskSpaceCheck();
    }
    return ourInstance;
  }

  /**
   * check the available disk space
   * 
   * @return true if system is in a valid state, else false
   */
  public boolean checkDiskSpace() {
    boolean retVal = true;
    if (null != myBaseDirectory) {
      myFreeSpace = myBaseDirectory.getUsableSpace();
      ourLogger.info(String.format("available diskspace <%d>", myFreeSpace));
      if (myFreeSpace < myMinimumDiskSpace) {
        ourLogger.error(String.format(
            "diskspace <%d> reached mininum value <%d>", myFreeSpace,
            myMinimumDiskSpace));
        retVal = false;
      }
    }
    return retVal;
  }

}
