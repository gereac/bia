package com.gcsf.books.engine.util;

import java.lang.Thread.UncaughtExceptionHandler;

public class DefaultUncaughtExceptionHandler implements
    UncaughtExceptionHandler {
  private static final DefaultUncaughtExceptionHandler ourInstance = new DefaultUncaughtExceptionHandler();

  private DefaultUncaughtExceptionHandler() {
  }

  synchronized public static final UncaughtExceptionHandler getInstance() {
    return ourInstance;
  }

  /**
   * @param aThread
   *          Thread
   * @param aThrowable
   *          Throwable
   */
  public void uncaughtException(Thread aThread, Throwable aThrowable) {
    ApplicationControl.notifyError(DefaultUncaughtExceptionHandler.class,
        aThrowable, "Uncaught Exception in thread " + aThread.getName());
  }
}
