package com.gcsf.books.engine.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.osgi.framework.Bundle;

import com.gcsf.books.engine.EngineActivator;

/**
 * some useful methods for logging information.
 * 
 */
public class LogHelper {

  /**
   * The linefeed character
   */
  public static final String LF = System.getProperty("line.separator");

  /**
   * Constructor
   * 
   */
  private LogHelper() {
  };

  /**
   * return the stack as string of the given exception
   * 
   * @param aException
   *          Exception
   * @return stack trace as multiple line string
   */
  public static String getStackString(Throwable aException) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    aException.printStackTrace(pw);
    return sw.toString();
  }

  /**
   * get the message and stack trace of the given exception as one logable
   * string
   * 
   * @param aException
   *          Exception
   * @return String Log Message
   */
  public static String getLogMessage(Throwable aException) {
    return LF + aException.getMessage() + LF + "forcing exception :" + LF
        + getStackString(aException);
  }

  /**
   * Extracts the name of the calling method from the stack trace
   * 
   * @return Returns the name of the calling method.
   */
  public static String getCallingMethod() {
    StackTraceElement[] stackTraceElement = (new Exception()).getStackTrace();
    int stackTraceElementIndex = 2;
    return stackTraceElement[stackTraceElementIndex].getMethodName();
  }

  /**
   * gets the direct caller stack of the method as a string
   * 
   * @return calling method
   */
  public static String getCallerStackString() {
    StackTraceElement[] stackTraceElement = (new Exception()).getStackTrace();

    // skip 2 stack lines:
    // stack line 1 = getCaller()
    // stack line 2 = info(), warn(), error(), fatal()
    int stackTraceElementIndex = 2;

    StringBuffer caller = new StringBuffer();
    caller.append("(");
    // caller.append(stackTraceElement[stackTraceElementIndex].getFileName());
    caller.append(stackTraceElement[stackTraceElementIndex].getClassName());
    caller.append(":");
    caller.append(stackTraceElement[stackTraceElementIndex].getLineNumber());
    caller.append(")");
    return caller.toString();
  }

  /**
   * @param aIndent
   *          an indentation
   * @return Returns a string containing the plugin details
   */
  public static String getPluginDetails(String aIndent) {
    StringBuffer details = new StringBuffer();
    EngineActivator corePlugin = EngineActivator.getDefault();
    if (null != corePlugin) {
      Bundle[] bundles = corePlugin.getBundles();
      for (int i = 0; i < bundles.length; i++) {
        // AboutBundleData bundleData = new AboutBundleData(bundles[i]);
        if (aIndent != null) {
          details.append(aIndent);
        }
        details.append(bundles[i].getHeaders().get(
            org.osgi.framework.Constants.BUNDLE_VENDOR));
        details.append(" : ");
        details.append(bundles[i].getHeaders().get(
            org.osgi.framework.Constants.BUNDLE_NAME));
        details.append(" : ");
        details.append((String) bundles[i].getHeaders().get(
            org.osgi.framework.Constants.BUNDLE_VERSION));
        // details.append(" : ");
        // details.append(bundleData.getId());
        // details.append(" : ");
        // details.append(bundleData.getStateName());
        details.append(LF);
      }
    }
    return details.toString();
  }
}
