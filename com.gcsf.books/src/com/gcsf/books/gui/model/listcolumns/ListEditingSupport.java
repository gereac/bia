package com.gcsf.books.gui.model.listcolumns;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.gcsf.books.gui.dialogs.model.ColumnSet;

public class ListEditingSupport extends EditingSupport {

  private CellEditor editor;

  private int column;

  public ListEditingSupport(ColumnViewer viewer, int column) {
    super(viewer);
    // Create the correct editor based on the column index
    switch (column) {
      default:
        editor = new TextCellEditor(((TableViewer) viewer).getTable());
        // editor = new MyStyledTextCellEditor(((TableViewer)
        // viewer).getTable());
    }
    this.column = column;
  }

  @Override
  protected boolean canEdit(Object element) {
    return true;
  }

  @Override
  protected CellEditor getCellEditor(Object element) {
    return editor;
  }

  @Override
  protected Object getValue(Object element) {
    ColumnSet columnSet = (ColumnSet) element;
    switch (this.column) {
      case 0:
        return columnSet.getColumnSetName();
      default:
        break;
    }
    return null;
  }

  @Override
  protected void setValue(Object element, Object value) {
    ColumnSet columnSet = (ColumnSet) element;
    switch (this.column) {
      case 0:
        columnSet.setColumnSetName(String.valueOf(value));
        break;
      default:
        break;
    }
    getViewer().update(element, null);
  }

}
