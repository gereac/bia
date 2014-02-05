package com.gcsf.books.gui.dialogs;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;

public class SecondClickColumnViewerEditorActivationStrategy extends
    ColumnViewerEditorActivationStrategy implements ISelectionChangedListener {
  private Object selectedElement;

  public SecondClickColumnViewerEditorActivationStrategy(ColumnViewer viewer) {
    super(viewer);
    viewer.addSelectionChangedListener(this);
  }

  @Override
  protected boolean isEditorActivationEvent(
      ColumnViewerEditorActivationEvent event) {
    IStructuredSelection selection = (IStructuredSelection) getViewer()
        .getSelection();

    return (selection.size() == 1 && super.isEditorActivationEvent(event) && selectedElement == selection
        .getFirstElement())
        || (event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC)
        || (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.F2);
  }

  public void selectionChanged(SelectionChangedEvent event) {
    IStructuredSelection ss = (IStructuredSelection) event.getSelection();

    if (ss.size() == 1) {
      selectedElement = ss.getFirstElement();
      return;
    }

    selectedElement = null;
  }
}
