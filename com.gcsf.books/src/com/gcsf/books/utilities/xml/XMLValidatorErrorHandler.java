package com.gcsf.books.utilities.xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

//------------------------------------------------------------------------
/**
 * The <CODE>XMLValidatorErrorHandler</CODE> class is designed as a individual
 * error handler class for XML/XSD validation. This class is used by the
 * XMLModelValidator class.
 * 
 * @see com.alcatel.tas.hmi.utils.XMLValidator
 */
public class XMLValidatorErrorHandler implements ErrorHandler {
  private StringBuffer myErrorText = null;

  private boolean myErrorState = false;

  // ----------------------------------------------------------------------
  /**
   * The <CODE>XMLValidatorErrorHandler</CODE> default constructor is provided
   * to create a new instance of this class.
   */
  public XMLValidatorErrorHandler() {
    myErrorText = new StringBuffer();
  }

  // ----------------------------------------------------------------------
  /**
   * The <CODE>warning</CODE> method is provided to indicate a warning state
   * occured during XML/XSD parsing.
   * 
   * @param aWarnException
   *          is the warning exception.
   */
  public void warning(SAXParseException aWarnException) {
    myErrorState = true;

    if (null != aWarnException) {
      myErrorText.append("WARNING: ");
      myErrorText.append("line ");
      myErrorText.append(aWarnException.getLineNumber());
      myErrorText.append("\n");
      myErrorText.append(aWarnException.getMessage());
      myErrorText.append("\n");
    }
  }

  // ----------------------------------------------------------------------
  /**
   * The <CODE>error</CODE> method is provided to indicate a error state occured
   * during XML/XSD parsing.
   * 
   * @param aErrorException
   *          is the error exception.
   */
  public void error(SAXParseException aErrorException) {
    myErrorState = true;

    if (null != aErrorException) {
      myErrorText.append("ERROR: ");
      myErrorText.append("line ");
      myErrorText.append(aErrorException.getLineNumber());
      myErrorText.append("\n");
      myErrorText.append(aErrorException.getMessage());
      myErrorText.append("\n");
    }
  }

  // ----------------------------------------------------------------------
  /**
   * The <CODE>error</CODE> method is provided to indicate a fatal state occured
   * during XML/XSD parsing.
   * 
   * @param aFatalException
   *          is the fatal exception.
   */
  public void fatalError(SAXParseException aFatalException) {
    myErrorState = true;

    if (null != aFatalException) {
      myErrorText.append("FATAL ERROR: ");
      myErrorText.append("line ");
      myErrorText.append(aFatalException.getLineNumber());
      myErrorText.append("\n");
      myErrorText.append(aFatalException.getMessage());
      myErrorText.append("\n");
    }
  }

  // ----------------------------------------------------------------------
  /**
   * The <CODE>getErrorOccured</CODE> method is provided to return the state of
   * the problem.
   * 
   * @return the state of the problem.
   */
  public boolean getErrorOccured() {
    return myErrorState;
  }

  // ----------------------------------------------------------------------
  /**
   * The <CODE>getErrorText</CODE> method is provided to return the text of the
   * problem.
   * 
   * @return the text of the problem.
   */
  public String getErrorText() {
    return myErrorText.toString();
  }
}
