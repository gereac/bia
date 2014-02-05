package com.gcsf.books.engine.logging;

import java.io.FilterOutputStream;
import java.util.Enumeration;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * A java.io.OutputStream implementation that forwards the stream to a Log4j
 * logger. This allows the use of Log4j with third-party APIs that can only
 * write log messages to a stream. E.g. redirect all output to System.out or
 * System.err to the log4j logger. We can forward these log messages to log4j
 * using <code>
 *   system.setOut
 *     (new PrinterStream
 *      (new OutputStreamLogger(Logger.getLogger(),Level.WARN,true)));
 * </code>. How do we decide on the boundaries of messages in the stream of
 * characters? We rely on the <code>flush()</code> method.
 * 
 */
public class OutputStreamLogger extends FilterOutputStream {
  protected final Logger myLogger;

  protected final Level myPriority;

  protected final boolean myChopTerminatingNewLine;

  protected StringBuffer myBuffer = new StringBuffer();

  /**
   * Constructor.
   * 
   * @param aToLogger
   *          log4j category under which you wish messages to be logged
   * @param aLevel
   *          under which messages should be logged
   * @param aChopTerminatingNewLine
   *          if set to true, terminating new line chars of a log message are
   *          chopped (or chomp()ed in perl speak). Most log4j conversion
   *          patterns add their own new lines (%n) as part of the layout. As
   *          layouts are set for appenders and not for categories, it's
   *          convenient to have this ability.
   */
  public OutputStreamLogger(Logger aToLogger, Level aLevel,
      boolean aChopTerminatingNewLine) {
    // FilterWriter, by default, writes to the stream passed
    // to its constructor. As we'll be overriding all of its methods,
    // we'll simply pass System.out but won't use it.
    super(System.out);
    if (aToLogger == null) {
      throw new IllegalArgumentException("logger == null");
    }

    if (aLevel == null) {
      throw new IllegalArgumentException("level == null");
    }

    myLogger = aToLogger;
    this.myPriority = aLevel;
    this.myChopTerminatingNewLine = aChopTerminatingNewLine;
  }

  /**
   * Appends the given character to the next log message.
   * 
   * @param aCharacter
   *          character
   */
  public synchronized void write(int aCharacter) {
    myBuffer.append((char) aCharacter);
  }

  /**
   * Appends the given range of characters to the next log message.
   * 
   * @param aCharacterBuffer
   *          the character buffer
   */
  public synchronized void write(byte[] aCharacterBuffer) {
    write(aCharacterBuffer, 0, aCharacterBuffer.length);
  }

  /**
   * Appends the given range of characters to the next log message.
   * 
   * @param aCharacterBuffer
   *          the character buffer
   * @param aOffset
   *          the offset
   * @param aLength
   *          the length
   */
  public synchronized void write(byte[] aCharacterBuffer, int aOffset,
      int aLength) {
    for (int i = aOffset; i < aLength; i++) {
      myBuffer.append((char) aCharacterBuffer[i]);
    }
  }

  /** Flushes the buffer to log4j. */
  public synchronized void flush() {
    log();
  }

  /** Flushes buffer to log4j if it is non-empty. */
  public synchronized void close() {
    if (myBuffer.length() != 0) {
      log();
    }
  }

  /** Flushes buffer to log4j if it is non-empty. */
  protected void finalize() throws Throwable {
    if (myBuffer.length() != 0) {
      log();
    }
    super.finalize();
  }

  /**
   * Forwards the contents of the buffer to the logger and clears the buffer.
   */
  protected void log() {
    for (Enumeration<?> appenders = Logger.getRootLogger().getAllAppenders(); appenders
        .hasMoreElements();) {
      if (appenders.nextElement() instanceof ConsoleAppender) {
        // if there is a ConsoleAppender configured, avoid a endless recusion !
        return;
      }
    }

    if (myChopTerminatingNewLine) {
      chomp();
    }

    if (myBuffer.length() != 0) {
      myLogger.log(myPriority, myBuffer.toString());
      myBuffer.delete(0, myBuffer.length());
    }
  }

  /**
   * chops the terminating new lines chars in the buffer, if there are any.
   */
  protected final void chomp() {
    int length = myBuffer.length();
    switch (length) {
      case 0:
        break;
      case 1: {
        char last = myBuffer.charAt(0);
        if ((last == '\r') || (last == '\n')) {
          myBuffer.deleteCharAt(0);
        }
        break;
      }
      default: {
        char last = myBuffer.charAt(length - 1);
        if (last == '\r') {
          myBuffer.deleteCharAt(length - 1);
        } else if (last == '\n') {
          myBuffer.deleteCharAt(length - 1);
          if (myBuffer.charAt(length - 2) == '\r') {
            myBuffer.deleteCharAt(length - 2);
          }
        }
        // fall-out of the switch block
      }
    }
  }
}
