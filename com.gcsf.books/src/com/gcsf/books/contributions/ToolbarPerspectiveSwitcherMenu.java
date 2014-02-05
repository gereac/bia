package com.gcsf.books.contributions;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.gcsf.books.BooksActivator;
import com.gcsf.books.preferences.IPreferenceConstants;

public class ToolbarPerspectiveSwitcherMenu extends ContributionItem {

  private static ToolItem dropItem;

  private static Menu dropMenu;

  private static final String PERSPECTIVE_PATERN_DESCR = "com.gcsf.books.perspective.layout";

  private static final String KEY_PERSPECTIVE_DESCR = "k_p_descr";

  private static final IWindowListener WINDOW_LISTENER = new WindowListener();

  private static final PerspectiveListener PERSPECTIVE_LISTENER = new PerspectiveListener();

  private final SelectionListener menuListener = new SwitchPerspectiveMenuListener();

  static {
    PlatformUI.getWorkbench().addWindowListener(WINDOW_LISTENER);
  }

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

  private void setDropMenu(Menu aMenu) {
    dropMenu = aMenu;
  }

  private static Menu getDropMenu() {
    return dropMenu;
  }

  private void setDropItem(ToolItem aItem) {
    dropItem = aItem;
  }

  private static ToolItem getDropItem() {
    return dropItem;
  }

  @Override
  public void fill(final ToolBar parent, int index) {
    final Menu menu = new Menu(parent.getShell(), SWT.POP_UP);
    IPerspectiveDescriptor[] perspectives = PlatformUI.getWorkbench()
        .getPerspectiveRegistry().getPerspectives();
    int menuIndex = 0;
    for (int i = 0; i < perspectives.length; i++) {
      IPerspectiveDescriptor descriptor = perspectives[i];
      // i is used as an item index; 0-n will add items to the top of the menu
      if (descriptor.getId().startsWith(PERSPECTIVE_PATERN_DESCR)) {
        MenuItem mItem = new MenuItem(menu, SWT.CHECK, menuIndex);
        menuIndex++;
        mItem.setData(KEY_PERSPECTIVE_DESCR, descriptor);
        mItem.setText(descriptor.getLabel());
        final Image image = descriptor.getImageDescriptor().createImage();
        mItem.setImage(image);
        mItem.addDisposeListener(new DisposeListener() {
          public void widgetDisposed(DisposeEvent e) {
            image.dispose();
          }
        });
        mItem.addSelectionListener(menuListener);
      }
    }
    setDropMenu(menu);
    final ToolItem item = new ToolItem(parent, SWT.DROP_DOWN, index);
    item.setToolTipText("Change screen layout");
    item.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(
        ISharedImages.IMG_DEF_VIEW));
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
    setDropItem(item);
    parent.pack(true);
  }

  // helping classes
  // ////////////////

  /**
   * Switch perspective in the active page
   */
  private static final class SwitchPerspectiveMenuListener extends
      SelectionAdapter {
    public void widgetSelected(SelectionEvent e) {
      MenuItem mItem = (MenuItem) e.widget;

      if (mItem.getSelection()) {
        IWorkbenchPage page = getActivePage();
        if (page != null) {
          IPerspectiveDescriptor descriptor = (IPerspectiveDescriptor) mItem
              .getData(KEY_PERSPECTIVE_DESCR);
          page.setPerspective(descriptor);
          final Image image = descriptor.getImageDescriptor().createImage();
          getDropItem().setImage(image);
          getDropItem().addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
              image.dispose();
            }
          });
          BooksActivator.getDefault().getPreferenceStore()
              .setValue(IPreferenceConstants.P_CURRENT_PERSPECTIVE_ID,
                  descriptor.getId());
          // PERSPECTIVE_LISTENER.updateToolbar(mItem.getParent().getShell(),
          // descriptor);
        }
      }
    }

  }

  /**
   * Update toolbar on activation of a perspective.
   * <p>
   * This listener listens to perspective activation on all windows and updates
   * the toolbar instance if necessary.
   */
  private static final class PerspectiveListener implements
      IPerspectiveListener {
    public void perspectiveChanged(IWorkbenchPage page,
        IPerspectiveDescriptor perspective, String changeId) {
      // unused
    }

    public void perspectiveActivated(IWorkbenchPage page,
        IPerspectiveDescriptor perspective) {
      Shell shell = page.getWorkbenchWindow().getShell();
      updateToolbar(shell, page.getPerspective());

    }

    public void updateToolbar(Shell shell, IPerspectiveDescriptor perspective) {

      final Image image = perspective.getImageDescriptor().createImage();
      getDropItem().setImage(image);
      getDropItem().addDisposeListener(new DisposeListener() {
        public void widgetDisposed(DisposeEvent e) {
          image.dispose();
        }
      });
      MenuItem[] mItems = getDropMenu().getItems();
      for (MenuItem mItem : mItems) {
        boolean isSelected = perspective == mItem
            .getData(KEY_PERSPECTIVE_DESCR);
        if (isSelected != mItem.getSelection()) {
          mItem.setSelection(isSelected);
        }
      }
    }
  }

  private static final class WindowListener implements IWindowListener {
    public void windowActivated(IWorkbenchWindow window) {
      // unused
    }

    public void windowClosed(IWorkbenchWindow window) {
      window.removePerspectiveListener(PERSPECTIVE_LISTENER);
    }

    public void windowDeactivated(IWorkbenchWindow window) {
      // unused
    }

    public void windowOpened(IWorkbenchWindow window) {
      window.addPerspectiveListener(PERSPECTIVE_LISTENER);
      // update toolbar 'selection' state when window opens, because the toolbar
      // is
      // created before the perspective. At this point we have the perspective.
      Shell shell = window.getShell();
      IPerspectiveDescriptor perspective = window.getActivePage()
          .getPerspective();
      PERSPECTIVE_LISTENER.updateToolbar(shell, perspective);
    }
  }

  @Override
  public final boolean isDynamic() {
    return true;
  }

}
