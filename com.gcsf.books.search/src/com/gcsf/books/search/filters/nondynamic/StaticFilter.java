package com.gcsf.books.search.filters.nondynamic;

public class StaticFilter {

  private String filterLabel;

  private String filterImagePath;

  private String filterOverImagePath;

  private boolean isSeparator;

  private Object filterCriteria;

  public StaticFilter(String filterLabel, String filterImagePath,
      String filterOverImagePath, boolean isSeparator, Object filterCriteria) {
    super();
    this.filterLabel = filterLabel;
    this.filterImagePath = filterImagePath;
    this.filterOverImagePath = filterOverImagePath;
    this.isSeparator = isSeparator;
    this.filterCriteria = filterCriteria;
  }

  public String getFilterLabel() {
    return filterLabel;
  }

  public void setFilterLabel(String filterLabel) {
    this.filterLabel = filterLabel;
  }

  public String getFilterImagePath() {
    return filterImagePath;
  }

  public void setFilterImagePath(String filterImagePath) {
    this.filterImagePath = filterImagePath;
  }

  public Object getFilterCriteria() {
    return filterCriteria;
  }

  public void setFilterCriteria(Object filterCriteria) {
    this.filterCriteria = filterCriteria;
  }

  public boolean isSeparator() {
    return isSeparator;
  }

  public void setSeparator(boolean isSeparator) {
    this.isSeparator = isSeparator;
  }

  public String getFilterOverImagePath() {
    return filterOverImagePath;
  }

  public void setFilterOverImagePath(String filterOverImagePath) {
    this.filterOverImagePath = filterOverImagePath;
  }

}
