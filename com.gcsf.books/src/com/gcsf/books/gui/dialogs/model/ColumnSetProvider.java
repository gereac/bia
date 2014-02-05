package com.gcsf.books.gui.dialogs.model;

import java.util.ArrayList;
import java.util.List;

public class ColumnSetProvider {

  private static ColumnSetProvider content;

  private List<ColumnSet> columnSets;

  private ColumnSetProvider() {
    columnSets = new ArrayList<ColumnSet>();
    // Image here some fancy database access to read the persons and to
    // put them into the model
    ColumnSet columnSet;
    columnSet = new ColumnSet("Rainer", null);
    columnSets.add(columnSet);
    columnSet = new ColumnSet("Stransky", null);
    columnSets.add(columnSet);
    columnSet = new ColumnSet("Marie", null);
    columnSets.add(columnSet);
    columnSet = new ColumnSet("Holger", null);
    columnSets.add(columnSet);
    columnSet = new ColumnSet("Juliane", null);
    columnSets.add(columnSet);

  }

  public static synchronized ColumnSetProvider getInstance() {
    if (content == null) {
      content = new ColumnSetProvider();
    }
    return content;
  }

  public List<ColumnSet> getColumnSets() {
    return columnSets;
  }

}
