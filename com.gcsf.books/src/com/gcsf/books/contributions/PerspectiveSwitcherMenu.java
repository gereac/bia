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
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.gcsf.books.BooksActivator;
import com.gcsf.books.preferences.IPreferenceConstants;

public class PerspectiveSwitcherMenu extends ContributionItem {
  private static final String KEY_PERSPECTIVE_DESCR = "k_p_descr";

  private static final String PERSPECTIVE_PATERN_DESCR = "com.gcsf.books.perspective.layout";

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

  private static String getPerspectiveId() {
    String result = null;
    IWorkbenchPage page = getActivePage();
    if (page != null) {
      IPerspectiveDescriptor descriptor = page.getPerspective();
      if (descriptor != null) {
        result = descriptor.getId();
      }
    }
    return result;
  }

  public PerspectiveSwitcherMenu() {
  }

  public PerspectiveSwitcherMenu(String id) {
    super(id);
  }

  @Override
  public final boolean isDynamic() {
    return true;
  }

  /**
   * Fills a drop-down menu with all available perspectives. The current one is
   * selected.
   */
  @Override
  public void fill(Menu menu, int index) {

    MenuItem screenLayoutItem = new MenuItem(menu, SWT.CASCADE, index);
    screenLayoutItem.setText("Screen Layout");
    screenLayoutItem.setEnabled(true);
    Menu perspectiveMenu = new Menu(screenLayoutItem);
    perspectiveMenu.setVisible(true);
    screenLayoutItem.setMenu(perspectiveMenu);

    String activePerspective = getPerspectiveId();

    IPerspectiveDescriptor[] perspectives = PlatformUI.getWorkbench()
        .getPerspectiveRegistry().getPerspectives();
    int menuIndex = 0;
    for (int i = 0; i < perspectives.length; i++) {
      IPerspectiveDescriptor descriptor = perspectives[i];
      // i is used as an item index; 0-n will add items to the top of the menu
      if (descriptor.getId().startsWith(PERSPECTIVE_PATERN_DESCR)) {
        final MenuItem item = new MenuItem(perspectiveMenu, SWT.CHECK,
            menuIndex);
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
        if (descriptor.getId().equals(activePerspective)) {
          item.setSelection(true);
          screenLayoutItem.setImage(image);
          screenLayoutItem.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
              image.dispose();
            }
          });
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
          IPerspectiveDescriptor descriptor = (IPerspectiveDescriptor) item
              .getData(KEY_PERSPECTIVE_DESCR);
          page.setPerspective(descriptor);
          BooksActivator.getDefault().getPreferenceStore()
              .setValue(IPreferenceConstants.P_CURRENT_PERSPECTIVE_ID,
                  descriptor.getId());
        }
      }
    }

  }
}
