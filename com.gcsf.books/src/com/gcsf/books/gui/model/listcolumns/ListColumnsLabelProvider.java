package com.gcsf.books.gui.model.listcolumns;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.gcsf.books.gui.dialogs.model.ColumnSet;

public class ListColumnsLabelProvider extends LabelProvider implements
    ITableLabelProvider {

  @Override
  public Image getColumnImage(Object element, int columnIndex) {
    return null;
  }

  @Override
  public String getColumnText(Object element, int columnIndex) {
    ColumnSet columnSet = (ColumnSet) element;
    switch (columnIndex) {
      case 0:
        return columnSet.getColumnSetName();
      default:
        throw new RuntimeException("Should not happen");
    }

  }

}
