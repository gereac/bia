package com.gcsf.books.engine.supervision;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.gcsf.books.engine.util.ApplicationControl;

/**
 * @author ls21fnt
 * 
 */
public class MemoryCheck {
  /**
   * Logger for this class
   */
  private static final Logger ourLogger = Logger.getLogger(MemoryCheck.class);

  private static MemoryCheck ourInstance = null;

  private static final Runtime OUR_RUNTIME = Runtime.getRuntime();

  Thread myThread = null;

  Map<String, Long> myMemoryPools = new HashMap<String, Long>();

  private long myMemUsage = 0;

  /**
   * @return ourInstance
   */
  synchronized public static MemoryCheck getInstance() {
    if (ourInstance == null) {
      ourInstance = new MemoryCheck();
      ourInstance.checkMemorySetup();
    }
    return ourInstance;
  }

  /**
   * check the memory setup of the VM and exit application if memory min size !=
   * max size
   */
  public void checkMemorySetup() {
    long totalMemory = Runtime.getRuntime().totalMemory();
    long maxMemory = Runtime.getRuntime().maxMemory();
    if (totalMemory != maxMemory) {
      ApplicationControl
          .notifyError(
              this,
              String
                  .format(
                      "application memory setup is invalid, allocated memory <%d> != max memory <%d>",
                      totalMemory, maxMemory));
    }
  }

  /**
   * log the actual memory usage of the VM
   */
  public void logMemoryUsage() {
    long memUsage = OUR_RUNTIME.totalMemory() - OUR_RUNTIME.freeMemory();
    ourLogger.info(String.format("used memory <%d>, delta <%d>", memUsage,
        memUsage - myMemUsage));
    myMemUsage = memUsage;
    // List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
    // for (MemoryPoolMXBean p : pools) {
    // ourLogger.info("Memory type=" + p.getType() + " Memory usage="
    // + p.getUsage());
    // Long storedUsage = myMemoryPools.get(p.getName());
    // if (null == storedUsage) {
    // myMemoryPools.put(p.getName(), p.getUsage().getUsed());
    // } else {
    // myMemoryPools.put(p.getName(), p.getUsage().getUsed());
    // ourLogger.info(String.format("<%s> Memory usage delta : <%d>",
    // p.getName(),
    // p.getUsage().getUsed() - storedUsage));
    //        
    // }
    // }
  }
}
