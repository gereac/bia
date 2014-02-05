package com.gcsf.books.gui.model.listcolumns;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import com.gcsf.books.gui.dialogs.model.ColumnSet;

public class ListColumnsContentProvider implements IStructuredContentProvider {

  private TableViewer listViewer;

  public ListColumnsContentProvider(TableViewer lViewer) {
    this.listViewer = lViewer;
  }

  public void inputChanged(Viewer v, Object oldInput, Object newInput) {
    // if (newInput != null) {
    // ((ListInputListColumns) newInput).addChangeListener(this);
    // }
    // if (oldInput != null) {
    // ((ListInputListColumns) oldInput).removeChangeListener(this);
    // }
  }

  public void dispose() {
  }

  @SuppressWarnings("unchecked")
  public Object[] getElements(Object inputElement) {
    // if (inputElement instanceof Object[]) {
    // return (Object[]) inputElement;
    // }
    // if (inputElement instanceof Collection<?>) {
    // return ((Collection<?>) inputElement).toArray();
    // }
    // return new Object[0];
    List<ColumnSet> columnSets = (List<ColumnSet>) inputElement;
    return columnSets.toArray();

  }
}
