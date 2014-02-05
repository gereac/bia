package com.gcsf.books.engine.supervision;

public class TimeoutException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 8251804617398439190L;

  /**
   * Constructs a new runtime exception with null as its detail message.
   */
  public TimeoutException() {
    super();
  }

  /**
   * Constructs a new runtime exception with the specified detail message.
   * 
   * @param aMessage
   * @param aThrowable
   */
  public TimeoutException(String aMessage, Throwable aCause) {
    super(aMessage, aCause);
  }

  /**
   * Constructs a new runtime exception with the specified detail message and
   * cause.
   * 
   * @param aMessage
   */
  public TimeoutException(String aMessage) {
    super(aMessage);
  }

  /**
   * Constructs a new runtime exception with the specified cause and a detail
   * message of (cause==null ? null : cause.toString()) (which typically
   * contains the class and detail message of cause).
   * 
   * @param aCause
   */
  public TimeoutException(Throwable aCause) {
    super(aCause);
  }
}