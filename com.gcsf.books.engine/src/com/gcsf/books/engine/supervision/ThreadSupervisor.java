package com.gcsf.books.engine.supervision;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class ThreadSupervisor {
  /**
   * Logger for this class
   */
  private static final Logger ourLogger = Logger
      .getLogger(ThreadSupervisor.class);

  private static ThreadSupervisor ourInstance = null;

  Thread myThread = null;

  Map<Long, Long> myThreadCpuTimeMap = new HashMap<Long, Long>();

  /**
   * @return ourInstance
   */
  synchronized public static ThreadSupervisor getInstance() {
    if (ourInstance == null) {
      ourInstance = new ThreadSupervisor();
    }
    return ourInstance;
  }

  public void logThreadInformation() {

    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    long[] threadIds = threadMXBean.getAllThreadIds();
    StringBuffer sb = new StringBuffer();
    sb.append("Thread supervision of " + threadMXBean.getThreadCount()
        + " numbers of thread:\n");
    for (int threadIdx = 0; threadIdx < threadIds.length; threadIdx++) {
      ThreadInfo threadInfo = threadMXBean.getThreadInfo(threadIds[threadIdx]);
      if (null == threadInfo) {
        continue;
      }
      long cpuTime = threadMXBean.getThreadCpuTime(threadIds[threadIdx]);
      long lastCpuTime = 0;

      if (null != myThreadCpuTimeMap.get(threadIds[threadIdx])) {
        lastCpuTime = myThreadCpuTimeMap.get(threadIds[threadIdx]).longValue();
      }
      sb.append(String.format("    Thread - "));
      sb.append(String.format("CpuTime:%9d ms ", (cpuTime / 1000000)));
      sb.append(String.format("delta:%9d ms ",
          (cpuTime - lastCpuTime) / 1000000));
      sb.append(String.format("status:%-15s ", threadInfo.getThreadState()));
      sb.append(String.format("name:%-50s", threadInfo.getThreadName()));
      sb.append("\n");
      myThreadCpuTimeMap.put(threadIds[threadIdx], cpuTime);
    }
    ourLogger.info(sb.toString());
  }

  /**
   * Finds cycles of threads that are in deadlock waiting to acquire object
   * monitors and do a error log
   */
  public void logMonitorDeadlockedThreads() {
    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    StringBuffer sb = new StringBuffer();
    sb.append("monitor deadlocked threads :\n");
    long[] threadIds = threadMXBean.findMonitorDeadlockedThreads();
    if (null == threadIds || threadIds.length == 0) {
      return;
    }
    for (int threadIdx = 0; threadIdx < threadIds.length; threadIdx++) {
      ThreadInfo threadInfo = threadMXBean.getThreadInfo(threadIds[threadIdx]);
      if (null == threadInfo) {
        continue;
      }
      sb.append(String.format("    Thread - "));
      sb.append(String.format("status:%-15s ", threadInfo.getThreadState()));
      sb.append(String.format("name:%-50s", threadInfo.getThreadName()));
      StackTraceElement[] stackArray = threadInfo.getStackTrace();
      for (int i = 0; i < stackArray.length; i++) {
        sb.append("         ");
        sb.append(stackArray[i].toString());
        sb.append("\n");
      }
      sb.append("\n");
    }
    ourLogger.error(sb.toString());
  }
}
