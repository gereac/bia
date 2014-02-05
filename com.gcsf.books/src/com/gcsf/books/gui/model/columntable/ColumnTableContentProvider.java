package com.gcsf.books.gui.model.columntable;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ColumnTableContentProvider implements IStructuredContentProvider {

  public void inputChanged(Viewer v, Object oldInput, Object newInput) {
  }

  public void dispose() {
  }

  public Object[] getElements(Object inputElement) {
    if (inputElement instanceof Object[]) {
      return (Object[]) inputElement;
    }
    if (inputElement instanceof Collection<?>) {
      return ((Collection<?>) inputElement).toArray();
    }
    return new Object[0];
  }
}