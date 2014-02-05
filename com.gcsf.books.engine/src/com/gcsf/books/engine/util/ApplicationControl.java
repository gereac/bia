package com.gcsf.books.engine.util;

import java.util.Map;

import org.apache.log4j.Logger;

import com.gcsf.books.engine.logging.LogHelper;
import com.gcsf.books.engine.supervision.ThreadSupervisor;

public class ApplicationControl {
  private static final Logger ourLogger = Logger
      .getLogger(ApplicationControl.class);

  public static final String UNIT_TEST = "UnitTest";

  private static boolean ourIsExitExpected = false;

  private static ApplicationControl ourInstance = null;

  boolean myIsUnitTest = false;

  boolean myStoppedState = false;

  /**
   * implementation of a private singleton class
   * 
   * @return the singleton instance
   */
  synchronized private static ApplicationControl getInstance() {
    if (null == ourInstance) {
      ShutdownHook hook = new ShutdownHook();
      Runtime.getRuntime().addShutdownHook(hook);
      ourInstance = new ApplicationControl();
    }
    return ourInstance;
  }

  /**
   * initialize this class to support catching of shutdown signal
   */
  public static void init() {
    ApplicationControl.getInstance();
  }

  /**
   * use this method only for unit testing. If Mode UNIT_TEST is given, the
   * reaction on an error is not to shutdown the application (Junit must not be
   * shutdown for result sampling
   * 
   * @param aMode
   */
  public static void init(String aMode) {
    ApplicationControl ctrl = ApplicationControl.getInstance();
    if (UNIT_TEST.equals(aMode)) {
      ctrl.myIsUnitTest = true;
    }
  }

  /**
   * private constructor to avoid instantiation
   */
  private ApplicationControl() {
  };

  /**
   * notify a problem to the applications context. ApplicationControl is
   * responsible to decide how to react on this.
   * 
   * @param aProvider
   *          object providing the error
   * @param aMessage
   *          string describing the error
   */
  public static void notifyError(Class<?> aProvider, String aMessage) {
    ourLogger.error(aMessage + LogHelper.LF + "forcing exception :"
        + LogHelper.LF + LogHelper.getCallerStackString());
    ApplicationControl.getInstance().reactOnError(aProvider, null);
  }

  /**
   * notify a problem to the applications context. ApplicationControl is
   * responsible to decide how to react on this.
   * 
   * @param aProvider
   *          object providing the error
   * @param aMessage
   *          string describing the error
   */
  public static void notifyError(Object aProvider, String aMessage) {
    ourLogger.error(aMessage + LogHelper.LF + "forcing exception :"
        + LogHelper.LF + LogHelper.getCallerStackString());
    ApplicationControl.getInstance().reactOnError(aProvider.getClass(),
        new Exception(aMessage));
  }

  /**
   * notify a problem to the applications context. ApplicationControl is
   * responsible to decide how to react on this.
   * 
   * @param aProvider
   *          object providing the error
   * @param aError
   *          a Throwable object forcing this error
   * @param aMessage
   *          string describing the error
   */
  public static void notifyError(Object aProvider, Throwable aError,
      String aMessage) {
    notifyError(aProvider.getClass(), aError, aMessage);
  }

  /**
   * notify a problem to the applications context. ApplicationControl is
   * responsible to decide how to react on this.
   * 
   * @param aProvider
   *          object providing the error
   * @param aError
   *          a Throwable object forcing this error
   */
  public static void notifyError(Object aProvider, Throwable aError) {
    notifyError(aProvider.getClass(), aError);
  }

  /**
   * notify a problem to the applications context. ApplicationControl is
   * responsible to decide how to react on this.
   * 
   * @param aClass
   *          class providing the error
   * @param aError
   *          a Throwable object forcing this error
   * @param aMessage
   *          string describing the error
   */
  public static void notifyError(Class<?> aClass, Throwable aError,
      String aMessage) {
    ourLogger.error(aMessage, aError);

    ApplicationControl.getInstance().reactOnError(aClass, aError);
  }

  /**
   * notify a problem to the applications context. ApplicationControl is
   * responsible to decide how to react on this.
   * 
   * @param aClass
   *          class providing the error
   * @param aError
   *          a Throwable object forcing this error
   * 
   */
  public static void notifyError(Class<?> aClass, Throwable aError) {
    ApplicationControl.getInstance().reactOnError(aClass, aError);
  }

  /* CHECK:OFF HMI-P11 */
  private void reactOnError(Class<?> aProvider, Throwable aError) {
    /* CHECK:ON HMI-P11 */
    logAllStackTraces();
    if (null != aError) {
      aError.printStackTrace();
    }
    // log deadlocked threads if there are any
    ThreadSupervisor.getInstance().logMonitorDeadlockedThreads();
    // if (null != aError && aError instanceof TimeoutException
    // && RuntimeConfiguration.getInstance().isDevelopmentMode()) {
    // ourLogger.error("application exit not done, due to developmentMode",
    // aError);
    // return;
    // }
    if (null == aError) {
      ourLogger
          .error(
              "application exit, due to unrecoverable error (see stack trace above)",
              new Exception("unrecoverable error"));
      // } else if (aError instanceof FontValidatorException
      // && RuntimeConfiguration.getInstance().isDevelopmentMode()) {
      // ourLogger.error("application exit not done, due to developmentMode",
      // aError);
      // return;
    } else {
      ourLogger
          .error(
              "application exit, due to unrecoverable error (see stack trace above)",
              aError);
    }
    System.err
        .println("application exit, due to unrecoverable error (see log file) : "
            + aError);
    myStoppedState = true;
    if (myIsUnitTest) {
      ourLogger.error("application exit proxy", aError);
      return;
    } else {
      ourIsExitExpected = true;
      System.exit(-1);
    }
  }

  /**
   * For unit testing it meaningful get get the system stopped state
   * 
   * @return the stopped state.
   */
  static boolean isStopped() {
    return getInstance().getStoppedState();
  }

  /**
   * For unit testing it meaningful get get the system stopped state
   * 
   * @return the stopped state.
   */
  private boolean getStoppedState() {
    return myStoppedState;
  }

  /**
   * @return true if system exit is forced by ApplicationControl
   */
  static boolean isExitExpected() {
    return ourIsExitExpected;
  }

  /**
   * log all strack traces of all currently running threads (for dubugging
   * purpose)
   */
  public static void logAllStackTraces() {

    Map<Thread, StackTraceElement[]> allStackTraces = Thread
        .getAllStackTraces();
    StringBuffer sb = new StringBuffer();
    for (Thread thread : allStackTraces.keySet()) {
      StackTraceElement[] stackArray = allStackTraces.get(thread);

      sb.append("Thread <" + thread.getName() + ">" + "Prio = "
          + thread.getPriority() + "State = " + thread.getState() + ":\n");
      for (int i = 0; i < stackArray.length; i++) {
        sb.append("    ");
        sb.append(stackArray[i].toString());
        sb.append("\n");
      }
    }
    ourLogger.error("AllStackTraces:\n" + sb.toString());
  }
}

/**
 * @author lv12369 this thread has be called if the VM exits
 * 
 */
class ShutdownHook extends Thread {
  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Thread#run()
   */
  public void run() {
    if (!ApplicationControl.isExitExpected()) {
      ApplicationControl.logAllStackTraces();
      // log deadlocked threads if there are any
      ThreadSupervisor.getInstance().logMonitorDeadlockedThreads();
    }
  }
}
