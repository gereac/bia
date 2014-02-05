package com.gcsf.books;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.gcsf.books.engine.EngineActivator;
import com.gcsf.books.engine.logging.LogHelper;
import com.gcsf.books.engine.startup.ICollectionEngineStartup;
import com.gcsf.books.engine.util.ApplicationControl;
import com.gcsf.books.engine.util.DefaultUncaughtExceptionHandler;
import com.gcsf.books.engine.util.StartupConfigElementComparator;
import com.gcsf.books.engine.util.TimeDate;
import com.gcsf.books.utilities.logging.BIALogger;

/**
 * This class controls all aspects of the application's execution
 */
public class BooksApplication implements IApplication {

  private static final Logger ourLogger = Logger
      .getLogger(BooksApplication.class);

  /** Constant for the application being run on Windows or not */
  public static final boolean IS_WINDOWS = "win32".equals(SWT.getPlatform()); //$NON-NLS-1$

  /** Constant for the application being run on Linux or not */
  public static final boolean IS_LINUX = "gtk".equals(SWT.getPlatform()); //$NON-NLS-1$

  /** Constant for the application being run on Mac or not */
  public static final boolean IS_MAC = "carbon".equals(SWT.getPlatform()); //$NON-NLS-1$

  public static final String DEFAULT_APPLICATION = "bia";

  /**
   * public constructor that does nothing
   * */
  public BooksApplication() {
    // nothing to do here
  }

  /*
   * (non-Javadoc)
   * 
   * @seeorg.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
   * IApplicationContext)
   */
  public Object start(IApplicationContext aContext) throws Exception {

    Thread.setDefaultUncaughtExceptionHandler(DefaultUncaughtExceptionHandler
        .getInstance());
    Display display = PlatformUI.createDisplay();

    IConfigurationElement[] configElements = Platform.getExtensionRegistry()
        .getConfigurationElementsFor(
            EngineActivator.PLUGIN_ID + ".engineApplication");
    if (configElements.length != 1) {
      System.err
          .println("please have a look to .../workspace/.metadata/.log and search for the last unresolved plugin dependency !");
      throw new IllegalStateException(String.format(
          "illegal number of mandatory extension %s found : %d",
          EngineActivator.PLUGIN_ID + ".engineApplication",
          configElements.length));
    }
    try {

      BIALogger.initialize(DEFAULT_APPLICATION);
    } catch (Throwable t) {
      System.err.println("error running BIAApplication : " + t
          + LogHelper.getStackString(t));
    }

    try {
      ourLogger.info("Starting up Collection engine ...");

      // Initialize apache commons logging with log4j
      // System.getProperties().setProperty(LogFactory.class.getName(),
      // "org.apache.commons.logging.impl.Log4jFactory");
      // uncomment the above lines for common logging <= 1.0.3
      System.getProperties().setProperty(LogFactory.class.getName(),
          "org.apache.commons.logging.impl.LogFactoryImpl");

      IConfigurationElement[] configs = Platform.getExtensionRegistry()
          .getConfigurationElementsFor(
              EngineActivator.PLUGIN_ID + ".engineStartup");
      List<IConfigurationElement> configStartElements = new Vector<IConfigurationElement>();
      for (IConfigurationElement config : configs) {
        configStartElements.add(config);
      }

      Collections.sort(configStartElements,
          new StartupConfigElementComparator());

      StringBuffer buffer = new StringBuffer();
      for (IConfigurationElement config : configStartElements) {
        buffer.append("  [" + config.getAttribute("level") + "]: "
            + config.getContributor().getName() + "\n");
      }
      ourLogger.info("System startup order:\n" + buffer.toString());

      ourLogger.info("Plugin-Details:" + LogHelper.LF
          + LogHelper.getPluginDetails("  "));

      List<ICollectionEngineStartup> startups = new Vector<ICollectionEngineStartup>();
      for (IConfigurationElement configElement : configStartElements) {
        try {
          startups.add((ICollectionEngineStartup) configElement
              .createExecutableExtension("class"));
        } catch (Exception e) {
          ourLogger.fatal("Could not create executable extension: ", e);
          ApplicationControl.notifyError(BooksApplication.class, e);
        }
      }

      try {
        for (ICollectionEngineStartup startup : startups) {
          ourLogger.info(String.format("doStartup <%s>", startup.getClass()));
          startup.doStartup();
        }

        // do post startup
        for (ICollectionEngineStartup startup : startups) {
          ourLogger
              .info(String.format("doPostStartup <%s>", startup.getClass()));
          startup.doPostStartup();
        }
      } catch (Throwable t) {
        ApplicationControl.notifyError(BooksApplication.class, t,
            "Error in startup code");
      }
      ourLogger.info("Plugin-Details:" + LogHelper.LF
          + LogHelper.getPluginDetails("  "));
    } catch (Throwable t) {
      ApplicationControl.notifyError(BooksApplication.class, t,
          "Error in startup of application");
    }

    ourLogger.info(String.format("using virtual machine :" + "\n name <%s>"
        + "\n vendor <%s>" + "\n version <%s>",
        System.getProperty("java.vm.name"),
        System.getProperty("java.vm.vendor"),
        System.getProperty("java.vm.version")));

    ourLogger.info(String.format("starting application <%s>" + "\n at <%s>"
        + "\n on <%s/%s/%s/%s>" + "\n by <%s>" + "\n in <%s>",
        BooksApplication.class.getSimpleName(), TimeDate.getTimeAndDate(),
        java.net.InetAddress.getLocalHost().getHostName(),
        System.getProperty("os.arch"), System.getProperty("os.name"),
        System.getProperty("os.version"), System.getProperty("user.name"),
        System.getProperty("user.dir")));

    try {
      int returnCode = PlatformUI.createAndRunWorkbench(display,
          new ApplicationWorkbenchAdvisor());
      if (returnCode == PlatformUI.RETURN_RESTART) {
        return IApplication.EXIT_RESTART;
      }
      return IApplication.EXIT_OK;
    } finally {
      display.dispose();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.equinox.app.IApplication#stop()
   */
  public void stop() {
    if (!PlatformUI.isWorkbenchRunning()) {
      return;
    }
    final IWorkbench workbench = PlatformUI.getWorkbench();
    if (workbench == null) {
      return;
    }
    final Display display = workbench.getDisplay();
    display.syncExec(new Runnable() {
      public void run() {
        if (!display.isDisposed()) {
          workbench.close();
        }
      }
    });
  }
}
