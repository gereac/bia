package com.gcsf.books.engine.logging;

import java.io.PrintStream;

/**
 * This printStream subclass redirects the outputs given to originalStream to
 * originalStream and the additionalStream. It works somehow as the unix tee
 * command.
 */
public class TeePrintStream extends PrintStream {
  private PrintStream myOrgStream = null;

  private PrintStream myAddStream = null;

  /**
   * Constructor
   * 
   * @param aOrigStream
   *          the original stream that was used
   * @param aAdditionalStream
   *          the additional output stream
   */
  public TeePrintStream(PrintStream aOrigStream, PrintStream aAdditionalStream) {
    this(aOrigStream, aAdditionalStream, false);
  }

  /**
   * Constructor
   * 
   * @param aOrigStream
   *          the original stream that was used
   * @param aAdditionalStream
   *          the additional output stream
   * @param aFlush
   *          the flush flag
   */
  public TeePrintStream(PrintStream aOrigStream, PrintStream aAdditionalStream,
      boolean aFlush) {
    super(aOrigStream, aFlush);
    myOrgStream = aOrigStream;
    myAddStream = aAdditionalStream;
  }

  /**
   * (non-Javadoc)
   * 
   * @see java.io.PrintStream#checkError() {@inheritDoc}
   */
  public boolean checkError() {
    return myOrgStream.checkError() || super.checkError();
  }

  /**
   * (non-Javadoc)
   * 
   * @see java.io.OutputStream#close() {@inheritDoc}
   */
  public void close() {
    myAddStream.close();
    super.close();
  }

  /**
   * (non-Javadoc)
   * 
   * @see java.io.OutputStream#write(byte[], int, int) {@inheritDoc}
   */
  public void write(byte[] aBuffer, int aOffset, int aLength) {
    myAddStream.write(aBuffer, aOffset, aLength);
    super.write(aBuffer, aOffset, aLength);
  }

  /**
   * (non-Javadoc)
   * 
   * @see java.io.OutputStream#write(int) {@inheritDoc}
   */
  public void write(int aValue) {
    myAddStream.write(aValue);
    super.write(aValue);
  }

  /**
   * (non-Javadoc)
   * 
   * @see java.io.OutputStream#flush() {@inheritDoc}
   */
  public void flush() {
    myAddStream.flush();
    super.flush();
  }
}
