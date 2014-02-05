package com.gcsf.books.gui.dialogs.model;

public class ColumnSet {

  private String columnSetName;

  private ColumnEntry[] columnSetContent;

  public ColumnSet(String columnSetName, ColumnEntry[] columnSetContent) {
    super();
    this.columnSetName = columnSetName;
    this.columnSetContent = columnSetContent;
  }

  public String getColumnSetName() {
    return columnSetName;
  }

  public void setColumnSetName(String columnSetName) {
    this.columnSetName = columnSetName;
  }

  public ColumnEntry[] getColumnSetContent() {
    return columnSetContent;
  }

  public void setColumnSetContent(ColumnEntry[] columnSetContent) {
    this.columnSetContent = columnSetContent;
  }

}
