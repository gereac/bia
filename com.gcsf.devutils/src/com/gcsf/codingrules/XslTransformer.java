package com.gcsf.codingrules;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

//------------------------------------------------------------------------
public class XslTransformer implements ICodingRules {
  private static XslTransformer ourInstance = null;

  static {
    if (null == ourInstance) {
      ourInstance = new XslTransformer();
    }
  }

  private static final SimpleDateFormat ourDateFormatter = new SimpleDateFormat(
      "dd.MM.yyyy");

  private static final SimpleDateFormat ourTimeFormatter = new SimpleDateFormat(
      "HH:mm");

  private DirectoryReader myReader = null;

  private String myOutputDirectory = null;

  private String myStyleFilename = null;

  private Map<String, StatisticsStorage> myStatisticsMap = null;

  // ----------------------------------------------------------------------
  public static XslTransformer getInstance() {
    return ourInstance;
  }

  // ----------------------------------------------------------------------
  public String getDateAndTime() {
    return String.format("%s - %s", ourDateFormatter.format(Calendar
        .getInstance().getTime()), ourTimeFormatter.format(Calendar
        .getInstance().getTime()));
  }

  // ----------------------------------------------------------------------
  public void setOutputDirectory(String aOutputDirectory) {
    myOutputDirectory = aOutputDirectory;
  }

  // ----------------------------------------------------------------------
  public void setStyleFilename(String aStyleFilename) {
    myStyleFilename = aStyleFilename;
  }

  // ----------------------------------------------------------------------
  public void countSeverity(String aComponent, String aSeverity) {
    StatisticsStorage storage = myStatisticsMap.get(aComponent);

    if (null == storage) {
      storage = new StatisticsStorage();
      myStatisticsMap.put(aComponent, storage);
    }

    storage.countSeverity(aSeverity);
  }

  // ----------------------------------------------------------------------
  public void doTransformation() {
    try {
      TransformerFactory factory = TransformerFactory.newInstance();
      Transformer transformer = factory.newTransformer(new StreamSource(
          myStyleFilename));

      List<File> xmlFileList = myReader.readFiles(myOutputDirectory, "xml");

      for (File xmlFile : xmlFileList) {
        File htmlFile = new File(xmlFile.getAbsolutePath().replaceAll(
            "\\.[xX][mM][lL]$", ".html"));
        transformer.setParameter(XSL_PARAMETER_CSS_FILENAME, CSS_FILENAME);
        transformer.setParameter(XSL_PARAMETER_REPORT_DATE, getDateAndTime());
        transformer.setParameter(XSL_PARAMETER_HIS_COMPONENTNAME, htmlFile
            .getName().replaceAll("\\.html$", ""));
        transformer.transform(new StreamSource(xmlFile), new StreamResult(
            htmlFile));
      }
    } catch (Exception e) {
      StackTraceElement[] stacks = e.getStackTrace();
      for (StackTraceElement stack : stacks) {
        System.out.println(stack);
      }
      throw new RuntimeException(e);
    }
  }

  // ----------------------------------------------------------------------
  public Map<String, StatisticsStorage> getStatistics() {
    return myStatisticsMap;
  }

  // ----------------------------------------------------------------------
  public void writeStatistics() {
    HtmlWriter writer = new HtmlWriter();
    writer.write(myOutputDirectory);
  }

  // ----------------------------------------------------------------------
  public String toString() {
    StringBuffer retval = new StringBuffer();

    for (String component : myStatisticsMap.keySet()) {
      retval.append(component);
      retval.append(":\n");
      retval.append(myStatisticsMap.get(component).toString());
      retval.append("\n");
    }

    return retval.toString();
  }

  // ----------------------------------------------------------------------
  private XslTransformer() {
    myReader = new DirectoryReader();

    myStatisticsMap = new HashMap<String, StatisticsStorage>();
  }

  // ----------------------------------------------------------------------
  public static void main(String[] args) {
    String outputDirectoryname = null;
    String styleFilename = null;

    for (String arg : args) {
      if (arg.startsWith("-outdir=")) {
        outputDirectoryname = XslTransformer.getArgumentValue(arg);
      } else if (arg.startsWith("-style=")) {
        styleFilename = XslTransformer.getArgumentValue(arg);
      }
    }

    System.out.println("Output Directory: -outdir=" + outputDirectoryname);
    System.out.println("XSL Style File:   -style=" + styleFilename);

    if (null != outputDirectoryname && null != styleFilename) {
      XslTransformer.getInstance().setOutputDirectory(outputDirectoryname);
      XslTransformer.getInstance().setStyleFilename(styleFilename);

      XslTransformer.getInstance().doTransformation();
      XslTransformer.getInstance().writeStatistics();

      System.out.println();
      System.out.println("... Transformation executed successfully ...");

      System.out.println(XslTransformer.getInstance().toString());
    } else {
      System.err.println();
      System.err
          .println("... no Transformation executed - parameter missing ...");
    }

    System.exit(0);
  }

  // ----------------------------------------------------------------------
  static String getArgumentValue(String aArgument) {
    return aArgument.substring(aArgument.indexOf('=') + 1);
  }
}
