package com.gcsf.codingrules;

//------------------------------------------------------------------------
public interface ICodingRules {
  public static final String[] CHECKSTYLE_SEVERITY_ORDER = { "ignore", "info",
      "warning", "error", };

  public static final String OVERVIEW_FILENAME = "index.html";

  public static final String CSS_FILENAME = "../../checkstyle/styles.css";

  public static final String XSL_PARAMETER_CSS_FILENAME = "css.filename";

  public static final String XSL_PARAMETER_REPORT_DATE = "report.date";

  public static final String XSL_PARAMETER_HIS_COMPONENTNAME = "his.componentname";
}
