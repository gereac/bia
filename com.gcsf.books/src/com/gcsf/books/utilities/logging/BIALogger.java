package com.gcsf.books.utilities.logging;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.SimpleLayout;

import com.gcsf.books.engine.logging.LogHelper;
import com.gcsf.books.engine.logging.OutputStreamLogger;
import com.gcsf.books.engine.logging.TeePrintStream;
import com.gcsf.books.engine.util.TimeDate;

/**
 * This class initializes the log4j PropertyConfigurator. If no log4j Property
 * file can be found, a console logger and a file logger are initialized.
 * 
 */
public class BIALogger {

  private static final String LOG4J_LOG_FORMAT = "%-5p [%t] %c{1} - %m - (%M:%L)%n";

  private static Logger ourLogger = Logger.getLogger(BIALogger.class);

  private static final long REREAD_PERIOD = 10 * 1000; // in ms

  private static boolean ourInit = false;

  private static String ourApplicationName = null;

  /**
   * private constructor
   * */
  private BIALogger() {
    // nothing to do
  }

  /**
   * initialize the log4j logging API, using the given property file
   * "tashmilog4j.properties".
   * 
   * The internal policy is: <br>
   * 1. try to get the log4j property file in the user home directory <br>
   * 2. try to get the log4j property file in the ../workspace/Utils/resource/ <br>
   * 3. try to initialize log4j without a log4j property file, by
   * programatically initialize log4j
   */
  public static void initialize() {
    // initialize("BIA");
    simpleDefaultInit();
    return;
  }

  /**
   * initialize the log4j logging API, using the given property file
   * "tashmilog4j.properties". Where the application is started explicitly as
   * slave a property file with the name "tashmislavelog4j.properties" is
   * expected.
   * 
   * The internal policy is: <br>
   * 1. try to get the log4j property file in the loggingdirectory environment
   * variable <br>
   * 2. try to get the log4j property file in the user home directory <br>
   * 3. try to get the log4j property file in the ../workspace/Utils/resource/ <br>
   * 4. try to initialize log4j without a log4j property file, by
   * programatically initialize log4j
   * 
   * @param aApplicationName
   *          String
   */
  public static synchronized void initialize(String aApplicationName) {
    if (!ourInit) {
      ourInit = true;
      if (null == aApplicationName) {
        throw new NullPointerException("aApplicationName is null");
      }

      ourApplicationName = aApplicationName;

      String propertyFileName = null;
      URL url = null;

      try {

        propertyFileName = ourApplicationName + "log4j.properties";
        String log4jFilename = "bialog4j.properties";
        if (log4jFilename != null) {
          url = new URL("file:" + log4jFilename);
        }
      } catch (Throwable t) {
        // intentionally left blank
      }

      String dir = null;
      String propertyFilePath = null;
      File propertyFile = null;

      // 1. try to get the log4j property file in the user home directory
      dir = System.getProperty("user.home");
      propertyFilePath = dir + File.separator + propertyFileName;
      propertyFile = new File(propertyFilePath);

      if (propertyFile.exists()) {
        PropertyConfigurator.configureAndWatch(propertyFile.getPath(),
            REREAD_PERIOD);
        ourLogger.info(String.format(
            "using log4j property file in the user <%s> home directoy <%s> ",
            System.getProperty("user.name"), propertyFilePath));
      } else {
        // 2. try to get the log4j property file in the relative workspace
        // directory
        // if (null != url) {
        // PropertyConfigurator.configure(url);
        // ourLogger.info(String.format(
        // "using log4j property file in the working directoy <%s>",
        // propertyFilePath));
        // } else {
        // 3. try to initialize log4j without a log4j property file, by
        // programatically initialize log4j
        simpleDefaultInit();
        ourLogger
            .info(String
                .format(
                    "using log4j internal log4j settings (no user/local/class property file <%s> found)",
                    aApplicationName));
        // }
      }

      // print system out/err to stdin/stderr and to log4j
      System.setErr(new TeePrintStream(System.err, new PrintStream(
          new OutputStreamLogger(ourLogger, Level.ERROR, true), true)));
      System.setOut(new TeePrintStream(System.out, new PrintStream(
          new OutputStreamLogger(ourLogger, Level.WARN, true), true)));
    }
  }

  /**
   * @param aOutputFileName
   *          filename for log4j logging
   */
  private static void simpleDefaultInit() {
    try {
      SimpleLayout consoleLayout = new SimpleLayout();
      ConsoleAppender consoleAppender = new ConsoleAppender(consoleLayout);
      consoleAppender.activateOptions();

      PatternLayout fileLayout = new PatternLayout(LOG4J_LOG_FORMAT);
      FileAppender fileAppender = new FileAppender(fileLayout,
          ourApplicationName + ".log");
      fileAppender.setAppend(false);
      fileAppender.activateOptions();

      RollingFileAppender tashmiDRFileAppender = null;
      try {
        tashmiDRFileAppender = new RollingFileAppender(fileLayout, System
            .getProperty("user.home")
            + "/log/" + ourApplicationName + "d.log");
        // PathResolver.getInstance().getLoggingFolder() + "/"
        // + ourApplicationName + "d.log");
        tashmiDRFileAppender.setMaxBackupIndex(17);

        tashmiDRFileAppender.setMaxFileSize("500MB");
        tashmiDRFileAppender.setAppend(true);
        tashmiDRFileAppender.activateOptions();
      } catch (RuntimeException e) {
        // intentionally left blank
      }

      Logger rootLogger = Logger.getRootLogger();
      rootLogger.setLevel(Level.INFO);
      rootLogger.removeAllAppenders();
      rootLogger.addAppender(consoleAppender);
      rootLogger.addAppender(fileAppender);
      if (null != tashmiDRFileAppender) {
        rootLogger.addAppender(tashmiDRFileAppender);
      }
    } catch (Exception aException) {
      System.err.println("error initializing logging :"
          + LogHelper.getStackString(aException));
      ourLogger.error(LogHelper.getStackString(aException), aException);
      throw new RuntimeException(aException.toString());
    }
  }

  /**
   * stop logging completely
   */
  public static void stopLogging() {
    Logger rootLogger = Logger.getRootLogger();
    rootLogger.removeAllAppenders();
  }

  /**
   * Set the root level to the given level.
   * 
   * @param aLevel
   *          is the level to be used as root level
   */
  public static void setRootLevel(Level aLevel) {
    Logger rootLogger = Logger.getRootLogger();
    rootLogger.setLevel(aLevel);
  }

  /**
   * @param aApplicationName
   *          String
   */
  public static void logEnv(String aApplicationName) {
    String hostname = "";
    try {
      hostname = java.net.InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      hostname = "Unknown Host";
    }

    ourLogger.fatal(String.format("starting application <%s> " + "\n at <%s>"
        + "\n on <%s/%s/%s/%s>" + "\n by <%s>" + "\n in <%s> ",
        aApplicationName, TimeDate.getTimeAndDate(), hostname, System
            .getProperty("os.arch"), System.getProperty("os.name"), System
            .getProperty("os.version"), System.getProperty("user.name"), System
            .getProperty("user.dir")));
  }
}