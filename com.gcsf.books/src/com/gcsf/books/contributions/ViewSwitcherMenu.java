package com.gcsf.books.contributions;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;

public class ViewSwitcherMenu extends ContributionItem {
  private static final String KEY_PERSPECTIVE_DESCR = "k_p_descr";

  private static final String VIEW_PATERN_DESCR = "com.gcsf.books.view.stack";

  private final SelectionListener menuListener = new SwitchPerspectiveMenuListener();

  private static IWorkbenchPage getActivePage() {
    IWorkbenchPage result = null;
    IWorkbenchWindow window = PlatformUI.getWorkbench()
        .getActiveWorkbenchWindow();
    if (window != null) {
      result = window.getActivePage();
    }
    return result;
  }

  private static IViewReference getViewReference(String viewId) {
    IViewReference result = null;
    IWorkbenchPage page = getActivePage();
    if (page != null) {
      IViewReference[] refs = page.getViewReferences();
      for (int j = 0; j < refs.length; j++) {
        if (refs[j].getId().equals(viewId)) {
          result = refs[j];
        }
      }
    }
    return result;
  }

  public ViewSwitcherMenu() {
  }

  public ViewSwitcherMenu(String id) {
    super(id);
  }

  @Override
  public final boolean isDynamic() {
    return true;
  }

  /**
   * Fills a drop-down menu with stacked views. The visible one is selected.
   */
  @Override
  public void fill(Menu menu, int index) {

    IViewDescriptor[] views = PlatformUI.getWorkbench().getViewRegistry()
        .getViews();
    int menuIndex = 3;
    for (int i = 0; i < views.length; i++) {
      IViewDescriptor descriptor = views[i];
      // i is used as an item index; 0-n will add items to the top of the menu
      if (descriptor.getId().startsWith(VIEW_PATERN_DESCR)) {
        MenuItem item = new MenuItem(menu, SWT.CHECK, menuIndex);
        menuIndex++;
        item.setData(KEY_PERSPECTIVE_DESCR, descriptor);
        item.setText(descriptor.getLabel());
        final Image image = descriptor.getImageDescriptor().createImage();
        item.setImage(image);
        item.addDisposeListener(new DisposeListener() {
          public void widgetDisposed(DisposeEvent e) {
            image.dispose();
          }
        });
        item.addSelectionListener(menuListener);
        IWorkbenchPage page = getActivePage();
        if (page != null && getViewReference(descriptor.getId()) != null) {
          IWorkbenchPart aWPart = getViewReference(descriptor.getId()).getPart(
              true);
          if (aWPart != null) {
            if (page.isPartVisible(aWPart)) {
              item.setSelection(true);
            }
          }
        }

      }
    }
  }

  // helping classes
  // ////////////////

  /**
   * Switch perspective in the active page
   */
  private static final class SwitchPerspectiveMenuListener extends
      SelectionAdapter {
    public void widgetSelected(SelectionEvent e) {
      MenuItem item = (MenuItem) e.widget;
      if (item.getSelection()) {
        IWorkbenchPage page = getActivePage();
        if (page != null) {
          IViewDescriptor descriptor = (IViewDescriptor) item
              .getData(KEY_PERSPECTIVE_DESCR);
          try {
            page.showView(descriptor.getId());
          } catch (PartInitException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        }
      }
    }
  }
}
