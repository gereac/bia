package com.gcsf.books.contributions;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

import com.gcsf.books.BooksActivator;

public class SortContributionItem extends ContributionItem {
  // private static ToolItem dropItem;
  //
  // private static Menu dropMenu;

  private static final String KEY_SORTORDER_DESCR = "k_s_descr";

  private final SelectionListener menuListener = new SortOrderMenuListener();

  private static Command chooseCommand;

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
    mItem.setData(KEY_SORTORDER_DESCR, "Author / Year (desc)");
    mItem.setText("Author / Year (desc)");
    mItem.addSelectionListener(menuListener);
    mItem.setSelection(true);
    MenuItem mItem3 = new MenuItem(menu, SWT.CHECK, menuIndex);
    menuIndex++;
    mItem3.setData(KEY_SORTORDER_DESCR, "Author / Title");
    mItem3.setText("Author / Title");
    mItem3.addSelectionListener(menuListener);
    MenuItem mItem4 = new MenuItem(menu, SWT.CHECK, menuIndex);
    menuIndex++;
    mItem4.setData(KEY_SORTORDER_DESCR, "Nr of Pages (desc)");
    mItem4.setText("Nr of Pages (desc)");
    mItem4.addSelectionListener(menuListener);
    MenuItem mItem5 = new MenuItem(menu, SWT.CHECK, menuIndex);
    menuIndex++;
    mItem5.setData(KEY_SORTORDER_DESCR, "Purchase Date (desc)");
    mItem5.setText("Purchase Date (desc)");
    mItem5.addSelectionListener(menuListener);
    MenuItem mItem8 = new MenuItem(menu, SWT.SEPARATOR, menuIndex);
    menuIndex++;
    MenuItem mItem10 = new MenuItem(menu, SWT.PUSH, menuIndex);
    menuIndex++;
    mItem10.setData(KEY_SORTORDER_DESCR, "Choose Sort Fields ...");
    mItem10.setText("Choose Sort Fields ...");
    mItem10.addSelectionListener(menuListener);
    // TODO add the real implementation of the dynamic menu
    // setDropMenu(menu);
    final ToolItem item = new ToolItem(parent, SWT.DROP_DOWN, index);
    item.setToolTipText("Sort Order");
    item.setImage(BooksActivator.getImageDescriptor(
        "resource/icons/table/sort.gif").createImage());
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
  private static final class SortOrderMenuListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent e) {
      MenuItem mItem = (MenuItem) e.widget;
      if (!mItem.getSelection()) {
        if (mItem.getStyle() == SWT.PUSH) {
          // the choose columns menu item
          ICommandService commandService = (ICommandService) PlatformUI
              .getWorkbench().getService(ICommandService.class);
          IHandlerService handlerService = (IHandlerService) PlatformUI
              .getWorkbench().getService(IHandlerService.class);

          chooseCommand = commandService
              .getCommand("com.gcsf.books.command.columnset.chooser");
          if (chooseCommand.isDefined()) {
            try {
              handlerService.executeCommand(chooseCommand.getId(), null);
            } catch (ExecutionException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            } catch (NotDefinedException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            } catch (NotEnabledException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            } catch (NotHandledException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }
          }
        }
      } else {
        // TODO add the implementation for the SWT.CHECK items
      }
    }
  }

  @Override
  public final boolean isDynamic() {
    return true;
  }
}
