package com.gcsf.books.gui.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class ErrorViewPerspective implements IPerspectiveFactory {

  /**
   * Creates the initial layout for a page.
   */
  public void createInitialLayout(IPageLayout layout) {
    String editorArea = layout.getEditorArea();
    addFastViews(layout);
    addViewShortcuts(layout);
    addPerspectiveShortcuts(layout);
  }

  /**
   * Add fast views to the perspective.
   */
  private void addFastViews(IPageLayout layout) {
  }

  /**
   * Add view shortcuts to the perspective.
   */
  private void addViewShortcuts(IPageLayout layout) {
  }

  /**
   * Add perspective shortcuts to the perspective.
   */
  private void addPerspectiveShortcuts(IPageLayout layout) {
  }

}
