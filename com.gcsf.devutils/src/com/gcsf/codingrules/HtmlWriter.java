package com.gcsf.codingrules;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//------------------------------------------------------------------------
public class HtmlWriter implements ICodingRules {

  // ----------------------------------------------------------------------
  public HtmlWriter() {
  }

  // ----------------------------------------------------------------------
  public void write(String aOutputDirectory) {
    if (null == aOutputDirectory) {
      throw new IllegalArgumentException(
          "aOutputDirectory is not allowed to be null ...");
    }

    File outFile = new File(aOutputDirectory + "/" + OVERVIEW_FILENAME);

    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));

      Map<String, StatisticsStorage> statisticMap = XslTransformer
          .getInstance().getStatistics();
      Map<String, Integer> totalMap = new HashMap<String, Integer>();

      writeHeader(writer);
      writer.append("<table>\n");
      writer.append("<tr>\n");
      writer.append("<th class=\"component\">Component</th>\n");
      writer.append("<th class=\"ignore\">ignore</th>\n");
      writer.append("<th class=\"info\">info</th>\n");
      writer.append("<th class=\"warning\">warning</th>\n");
      writer.append("<th class=\"error\">error</th>\n");
      writer.append("</tr>\n");

      for (String component : statisticMap.keySet()) {
        writer.append("<tr>");
        writer.append("<td class=\"component\">");
        writer.append("<a href=\"").append(component).append(".html\">");
        writer.append(component);
        writer.append("</a>");
        writer.append("</td>");

        StatisticsStorage storage = statisticMap.get(component);
        Map<String, Integer> severityCounter = storage.getSeverityCounter();

        for (String severity : CHECKSTYLE_SEVERITY_ORDER) {
          Integer counter = severityCounter.get(severity);

          if (null == counter) {
            counter = new Integer(0);
          }

          writer.append("<td class=\"").append(severity).append("\">");
          writer.append(counter.toString());
          writer.append("</td>");

          Integer totalCounter = totalMap.get(severity);

          if (null == totalCounter) {
            totalCounter = new Integer(0);
          }

          totalCounter += counter;

          totalMap.put(severity, totalCounter);
        }

        writer.append("</tr>\n");
      }

      writer.append("<tr>");
      writer.append("<td class=\"total\">Total</td>");

      for (String severity : CHECKSTYLE_SEVERITY_ORDER) {
        writer.append("<td class=\"total").append(severity).append("\">");
        writer.append(totalMap.get(severity).toString());
        writer.append("</td>");
      }

      writer.append("</tr>\n");
      writer.append("</table>\n");
      writeFooter(writer);

      writer.close();

    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  // ----------------------------------------------------------------------
  private void writeHeader(BufferedWriter aWriter) throws IOException {
    String dateString = XslTransformer.getInstance().getDateAndTime();

    aWriter.append("<html>\n");
    aWriter.append("<head>\n");
    aWriter
        .append("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
    aWriter.append("<title>Overview: GCSF Codingrules Report (").append(
        dateString).append(")</title>\n");
    aWriter.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"")
        .append(CSS_FILENAME).append("\" title=\"styles\">\n");
    aWriter.append("</head>\n");
    aWriter.append("<body>\n");

    aWriter.append("<h1>Checkstyle Report</h1>\n");
    aWriter.append("<p>Date: ").append(dateString).append("</p>\n");
  }

  // ----------------------------------------------------------------------
  private void writeFooter(BufferedWriter aWriter) throws IOException {
    aWriter.append("</body>\n");
    aWriter.append("</html>\n");
  }
}
