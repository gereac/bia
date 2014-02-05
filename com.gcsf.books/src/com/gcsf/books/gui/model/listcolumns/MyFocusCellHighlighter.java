package com.gcsf.books.gui.model.listcolumns;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.FocusCellHighlighter;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class MyFocusCellHighlighter extends FocusCellHighlighter {

  public MyFocusCellHighlighter(ColumnViewer viewer) {
    super(viewer);
    hookListener(viewer);
  }

  private void markFocusedCell(Event event, ViewerCell cell) {
    Color background = (cell.getControl().isFocusControl()) ? getSelectedCellBackgroundColor(cell)
        : getSelectedCellBackgroundColorNoFocus(cell);
    Color foreground = (cell.getControl().isFocusControl()) ? getSelectedCellForegroundColor(cell)
        : getSelectedCellForegroundColorNoFocus(cell);

    if (foreground != null || background != null || onlyTextHighlighting(cell)) {
      GC gc = event.gc;

      if (background == null) {

        background = cell.getItem().getDisplay().getSystemColor(
            SWT.COLOR_LIST_SELECTION);
      }

      if (foreground == null) {
        foreground = cell.getItem().getDisplay().getSystemColor(
            SWT.COLOR_LIST_SELECTION_TEXT);
      }

      gc.setBackground(background);
      gc.setForeground(foreground);

      if (onlyTextHighlighting(cell)) {
        Rectangle area = event.getBounds();
        Rectangle rect = cell.getTextBounds();
        if (rect != null) {
          area.x = rect.x;
        }
        gc.fillRectangle(area);
      } else {
        gc.fillRectangle(event.getBounds());
      }

      event.detail &= ~SWT.SELECTED;
    }
  }

  private void removeSelectionInformation(Event event, ViewerCell cell) {
    System.out.println("2. events is " + event.toString() + " || " + "cell is "
        + cell.getText());
    GC gc = event.gc;
    gc.setBackground(cell.getViewerRow().getBackground(cell.getColumnIndex()));
    gc.setForeground(cell.getViewerRow().getForeground(cell.getColumnIndex()));
    gc.fillRectangle(cell.getBounds());
    event.detail &= ~SWT.SELECTED;
  }

  private void hookListener(final ColumnViewer viewer) {
    Listener listener = new Listener() {
      public void handleEvent(Event event) {
        if ((event.detail & SWT.SELECTED) > 0) {
          ViewerCell focusCell = getFocusCell();
          // ViewerRow row = viewer.getViewerRowFromItem(event.item);
          ViewerRow row = focusCell.getViewerRow();

          Assert
              .isNotNull(row,
                  "Internal structure invalid. Item without associated row is not possible."); //$NON-NLS-1$

          ViewerCell cell = row.getCell(event.index);

          if (focusCell == null || !cell.equals(focusCell)) {
            removeSelectionInformation(event, cell);
          } else {
            markFocusedCell(event, cell);
          }
        }
      }

    };
    viewer.getControl().addListener(SWT.EraseItem, listener);
  }

  /**
   * The color to use when rendering the background of the selected cell when
   * the control has the input focus
   * 
   * @param cell
   *          the cell which is colored
   * @return the color or <code>null</code> to use the default
   */
  protected Color getSelectedCellBackgroundColor(ViewerCell cell) {
    return null;
  }

  /**
   * The color to use when rendering the foreground (=text) of the selected cell
   * when the control has the input focus
   * 
   * @param cell
   *          the cell which is colored
   * @return the color or <code>null</code> to use the default
   */
  protected Color getSelectedCellForegroundColor(ViewerCell cell) {
    return null;
  }

  /**
   * The color to use when rendering the foreground (=text) of the selected cell
   * when the control has <b>no</b> input focus
   * 
   * @param cell
   *          the cell which is colored
   * @return the color or <code>null</code> to use the same used when control
   *         has focus
   * @since 3.4
   */
  protected Color getSelectedCellForegroundColorNoFocus(ViewerCell cell) {
    return null;
  }

  /**
   * The color to use when rendering the background of the selected cell when
   * the control has <b>no</b> input focus
   * 
   * @param cell
   *          the cell which is colored
   * @return the color or <code>null</code> to use the same used when control
   *         has focus
   * @since 3.4
   */
  protected Color getSelectedCellBackgroundColorNoFocus(ViewerCell cell) {
    return null;
  }

  /**
   * Controls whether the whole cell or only the text-area is highlighted
   * 
   * @param cell
   *          the cell which is highlighted
   * @return <code>true</code> if only the text area should be highlighted
   * @since 3.4
   */
  protected boolean onlyTextHighlighting(ViewerCell cell) {
    return true;
  }

  protected void focusCellChanged(ViewerCell newCell, ViewerCell oldCell) {
    super.focusCellChanged(newCell, oldCell);

    // Redraw new area
    if (newCell != null) {
      Rectangle rect = newCell.getBounds();
      int x = newCell.getColumnIndex() == 0 ? 0 : rect.x;
      int width = newCell.getColumnIndex() == 0 ? rect.x + rect.width
          : rect.width;
      // 1 is a fix for Linux-GTK
      newCell.getControl().redraw(x, rect.y - 1, width, rect.height + 1, true);
    }

    if (oldCell != null) {
      Rectangle rect = oldCell.getBounds();
      int x = oldCell.getColumnIndex() == 0 ? 0 : rect.x;
      int width = oldCell.getColumnIndex() == 0 ? rect.x + rect.width
          : rect.width;
      // 1 is a fix for Linux-GTK
      oldCell.getControl().redraw(x, rect.y - 1, width, rect.height + 1, true);
    }
  }

  // private ViewerCell oldCell;
  //
  // /**
  // * @param viewer
  // * the viewer
  // */
  // public EditableFocusCellHighlighter(ColumnViewer viewer) {
  // super(viewer);
  // this.hookListener(viewer);
  // }
  //
  // private void markFocusedCell(Event event, ViewerCell cell) {
  // final GC gc = event.gc;
  //
  // gc.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(
  // SWT.COLOR_LIST_BACKGROUND));
  // gc.setForeground(PlatformUI.getWorkbench().getDisplay().getSystemColor(
  // SWT.COLOR_LIST_FOREGROUND));
  // gc.setLineWidth(3);
  // final int currentCol = event.index;
  // final Rectangle rect = ((TableItem) event.item).getBounds(currentCol);
  // // gc.drawRectangle(rect);
  // gc.drawRoundRectangle(rect.x, rect.y, rect.width, rect.height, 5, 5);
  //
  // event.detail &= ~SWT.FOCUSED;
  // event.detail &= SWT.FocusOut;
  // event.detail &= ~SWT.SELECTED;
  // }
  //
  // private void removeSelectionInformation(Event event, ViewerCell cell) {
  // }
  //
  // private void hookListener(final ColumnViewer viewer) {
  // final Listener listener = new Listener() {
  //
  // public void handleEvent(Event event) {
  // if ((event.detail & SWT.SELECTED) > 0) {
  // ViewerCell focusCell = EditableFocusCellHighlighter.this
  // .getFocusCell();
  // ViewerRow row = focusCell.getViewerRow();
  //
  // Assert
  // .isNotNull(row,
  //                  "Internal structure invalid. Item without associated row is not possible."); //$NON-NLS-1$
  //
  // ViewerCell cell = row.getCell(event.index);
  //
  // if (focusCell == null || !cell.equals(focusCell)) {
  // EditableFocusCellHighlighter.this.removeSelectionInformation(event,
  // cell);
  // } else {
  // EditableFocusCellHighlighter.this.markFocusedCell(event, cell);
  // }
  // }
  // }
  //
  // };
  // viewer.getControl().addListener(SWT.EraseItem, listener);
  // }
  //
  // /**
  // * @param cell
  // * the cell which is colored
  // * @return the color
  // */
  // protected Color getSelectedCellBackgroundColor(ViewerCell cell) {
  // return null;
  // }
  //
  // /**
  // * @param cell
  // * the cell which is colored
  // * @return the color
  // */
  // protected Color getSelectedCellForegroundColor(ViewerCell cell) {
  // return null;
  // }
  //
  // /*
  // * (non-Javadoc)
  // *
  // * @see
  // *
  // org.eclipse.jface.viewers.FocusCellHighlighter#focusCellChanged(org.eclipse
  // * .jface.viewers.ViewerCell)
  // */
  // @Override
  // protected void focusCellChanged(ViewerCell cell) {
  // super.focusCellChanged(cell);
  //
  // // Redraw new area
  // if (cell != null) {
  // final Rectangle rect = cell.getBounds();
  // final int x = cell.getColumnIndex() == 0 ? 0 : rect.x;
  // final int width = cell.getColumnIndex() == 0 ? rect.x + rect.width
  // : rect.width;
  // // 1 is a fix for Linux-GTK
  // cell.getControl().redraw(x, rect.y - 1, width, rect.height + 1, true);
  // }
  //
  // if (oldCell != null) {
  // final Rectangle rect = oldCell.getBounds();
  // final int x = oldCell.getColumnIndex() == 0 ? 0 : rect.x;
  // final int width = oldCell.getColumnIndex() == 0 ? rect.x + rect.width
  // : rect.width;
  // // 1 is a fix for Linux-GTK
  // oldCell.getControl().redraw(x, rect.y - 1, width, rect.height + 1, true);
  // }
  //
  // oldCell = cell;
  // }

}
