package com.gcsf.books.contributions;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.gcsf.books.BooksActivator;

public class ColumnsContributionItem extends ContributionItem {
  // private static ToolItem dropItem;
  //
  // private static Menu dropMenu;

  private static final String KEY_COLUMNS_DESCR = "k_s_descr";

  private final SelectionListener menuListener = new ColumnsMenuListener();

  // private void setDropMenu(Menu aMenu) {
  // dropMenu = aMenu;
  // }
  //
  // private static Menu getDropMenu() {
  // return dropMenu;
  // }
  //
  // private void setDropItem(ToolItem aItem) {
  // dropItem = aItem;
  // }
  //
  // private static ToolItem getDropItem() {
  // return dropItem;
  // }

  @Override
  public void fill(final ToolBar parent, int index) {
    final Menu menu = new Menu(parent.getShell(), SWT.POP_UP);
    int menuIndex = 0;
    MenuItem mItem = new MenuItem(menu, SWT.CHECK, menuIndex);
    menuIndex++;
    mItem.setData(KEY_COLUMNS_DESCR, "Just main book data");
    mItem.setText("Just main book data");
    mItem.addSelectionListener(menuListener);
    mItem.setSelection(true);
    MenuItem mItem3 = new MenuItem(menu, SWT.CHECK, menuIndex);
    menuIndex++;
    mItem3.setData(KEY_COLUMNS_DESCR, "More book details");
    mItem3.setText("More book details");
    mItem3.addSelectionListener(menuListener);
    MenuItem mItem8 = new MenuItem(menu, SWT.SEPARATOR, menuIndex);
    menuIndex++;
    MenuItem mItem10 = new MenuItem(menu, SWT.PUSH, menuIndex);
    menuIndex++;
    mItem10.setData(KEY_COLUMNS_DESCR, "Choose columns ...");
    mItem10.setText("Choose columns ...");
    mItem10.addSelectionListener(menuListener);
    // TODO add the real implementation of the dynamic menu
    // setDropMenu(menu);
    final ToolItem item = new ToolItem(parent, SWT.DROP_DOWN, index);
    item.setToolTipText("Columns");
    item.setImage(BooksActivator.getImageDescriptor(
        "resource/icons/table/table_edit.png").createImage());
    item.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        if (event.detail == SWT.ARROW || event.detail == SWT.NONE) {
          Rectangle rect = item.getBounds();
          Point pt = new Point(rect.x, rect.y + rect.height);
          pt = parent.toDisplay(pt);
          menu.setLocation(pt.x, pt.y);
          menu.setVisible(true);
        }
      }
    });
    // setDropItem(item);
    parent.pack(true);
  }

  // helping classes
  // ////////////////

  /**
   * applies the selected collection status filter
   */
  private static final class ColumnsMenuListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent e) {
      MenuItem mItem = (MenuItem) e.widget;

      if (mItem.getSelection()) {
        // TODO add the real implementation here
      }
    }
  }

  @Override
  public final boolean isDynamic() {
    return true;
  }
}
