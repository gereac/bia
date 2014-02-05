package com.gcsf.books.gui.model.foldersview;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

import com.gcsf.books.model.FolderItem;
import com.gcsf.books.model.Item;

/**
 * The content provider class is responsible for providing objects to the view.
 * It can wrap existing objects in adapters or simply return objects as-is.
 * These objects may be sensitive to the current input of the view, or ignore it
 * and always show the same content (like Task List, for example).
 */
public class FoldersViewContentProvider extends ArrayContentProvider implements
    ITreeContentProvider {

  @Override
  public Object[] getChildren(Object parentElement) {
    return ((FolderItem) parentElement).getChildren().toArray();
  }

  @Override
  public Object getParent(Object element) {
    return ((Item) element).getParent();
  }

  @Override
  public boolean hasChildren(Object element) {
    if (element instanceof FolderItem) {
      return ((FolderItem) element).getChildren().size() > 0;
    } else {
      return false;
    }
  }

}