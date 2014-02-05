package com.gcsf.books.engine;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class EngineActivator extends AbstractUIPlugin {

  public static final String PLUGIN_ID = "com.gcsf.books.engine";

  // private static final Logger ourLogger = Logger
  // .getLogger(EngineActivator.class);

  // The shared instance.
  private static EngineActivator ourPlugin;

  private BundleContext myContext;

  /**
   * The constructor.
   */
  public EngineActivator() {
    ourPlugin = this;
  }

  /**
   * This method is called upon plug-in activation
   * 
   * @param aContext
   *          BundleContext
   * @throws Exception
   *           exception
   */
  public void start(BundleContext aContext) throws Exception {
    super.start(aContext);
    myContext = aContext;
  }

  /**
   * This method is called when the plug-in is stopped
   * 
   * @param aContext
   *          BundleContext
   * @throws Exception
   *           exception
   */
  public void stop(BundleContext aContext) throws Exception {
    super.stop(aContext);
    ourPlugin = null;
    myContext = null;
  }

  /**
   * Returns the shared instance.
   * 
   */
  public static EngineActivator getDefault() {
    return ourPlugin;
  }

  /**
   * Returns an image descriptor for the image file at the given plug-in
   * relative path.
   * 
   * @param aPath
   *          the path
   * @return the image descriptor
   */
  public static ImageDescriptor getImageDescriptor(String aPath) {
    return AbstractUIPlugin.imageDescriptorFromPlugin("com.gcsf.books.engine",
        aPath);
  }

  public Bundle[] getBundles() {
    if (myContext != null) {
      return myContext.getBundles();
    }
    return null;
  }

  /**
   * Log an Error Message.
   * 
   * @param msg
   *          The message to log as Error.
   * @param e
   *          The occuring Exception to log.
   */
  public void logError(String msg, Throwable e) {
    if (msg == null)
      msg = ""; //$NON-NLS-1$
    getLog().log(
        new Status(IStatus.ERROR, getBundle().getSymbolicName(), IStatus.ERROR,
            msg, e));
  }

  /**
   * Log an Info Message.
   * 
   * @param msg
   *          The message to log as Info.
   */
  public void logInfo(String msg) {
    if (msg == null)
      msg = ""; //$NON-NLS-1$
    getLog().log(
        new Status(IStatus.INFO, getBundle().getSymbolicName(), IStatus.OK,
            msg, null));
  }

  /**
   * Log an Error Message.
   * 
   * @param msg
   *          The message to log as Error.
   * @param e
   *          The occuring Exception to log.
   */
  public static void safeLogError(String msg, Throwable e) {
    if (ourPlugin != null)
      ourPlugin.logError(msg, e);
  }
}
