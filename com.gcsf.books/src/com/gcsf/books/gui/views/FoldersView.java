package com.gcsf.books.gui.views;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import com.gcsf.books.BooksActivator;
import com.gcsf.books.gui.model.foldersview.FoldersViewContentProvider;
import com.gcsf.books.gui.model.foldersview.FoldersViewLabelProvider;
import com.gcsf.books.preferences.IPreferenceConstants;

public class FoldersView extends ViewPart implements Observer {
  public static final String ID = "com.gcsf.books.view.folders";

  private static final String EXTENSION_POINT_STATIC_FILTER_OBSERVABLE = "com.gcsf.books.filters.static.observable";

  private TreeViewer treeBookViewer;

  private Observable o = null;

  IPropertyChangeListener preferenceListener = new IPropertyChangeListener() {
    /*
     * @see IPropertyChangeListener.propertyChange()
     */
    public void propertyChange(PropertyChangeEvent event) {
      if (event.getProperty().equals(IPreferenceConstants.P_CHECK_COUNTER)) {
        treeBookViewer.refresh(true);
      }
      if (event.getProperty().equals(
          IPreferenceConstants.P_CHECK_COLLECTION_STATUS)) {
        treeBookViewer.refresh(true);
      }
    }

  };

  /**
   * This is a callback that will allow us to create the viewer and initialize
   * it.
   */
  public void createPartControl(Composite parent) {

    GridLayout parentLayout = new GridLayout();
    parentLayout.marginWidth = 0;
    parentLayout.marginHeight = 0;
    parentLayout.verticalSpacing = 0;
    parentLayout.horizontalSpacing = 0;
    parent.setLayout(parentLayout);
    GridData parentData = new GridData(SWT.FILL, SWT.FILL, true, true);
    parent.setLayoutData(parentData);

    Composite toolbarComposite = new Composite(parent, SWT.TOP);
    GridLayout gridLayout = new GridLayout(1, false);
    gridLayout.marginWidth = 0;
    gridLayout.marginHeight = 0;
    gridLayout.verticalSpacing = 0;
    gridLayout.horizontalSpacing = 0;
    toolbarComposite.setLayout(gridLayout);
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    gridData.widthHint = SWT.DEFAULT;
    gridData.heightHint = SWT.DEFAULT;
    toolbarComposite.setLayoutData(gridData);

    createToolBarControl(toolbarComposite);

    Composite treeViewerComposite = new Composite(parent, SWT.BOTTOM
        | SWT.BORDER);
    GridLayout gridLayout2 = new GridLayout(1, false);
    gridLayout2.marginWidth = 0;
    gridLayout2.marginHeight = 0;
    gridLayout2.verticalSpacing = 0;
    gridLayout2.horizontalSpacing = 0;
    treeViewerComposite.setLayout(gridLayout2);
    GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData2.widthHint = SWT.DEFAULT;
    gridData2.heightHint = SWT.DEFAULT;
    treeViewerComposite.setLayoutData(gridData2);

    createTreeViewerControl(treeViewerComposite);
  }

  private void createToolBarControl(final Composite toolbarComposite) {

    final Cursor cursor = new Cursor(toolbarComposite.getDisplay(),
        SWT.CURSOR_HAND);
    final CLabel folderCLabel = new CLabel(toolbarComposite, SWT.BORDER);
    folderCLabel.setImage(BooksActivator.getImageDescriptor(
        "resource/icons/toolbar/cprj_obj.gif").createImage());
    folderCLabel.setText("No folders");
    folderCLabel.setBackground(new Color(getViewSite().getShell().getDisplay(),
        new RGB(255, 255, 255)));

    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    gridData.widthHint = SWT.DEFAULT;
    gridData.heightHint = SWT.DEFAULT;
    folderCLabel.setLayoutData(gridData);

    final Menu folderMenu = new Menu(folderCLabel.getShell(), SWT.POP_UP);
    MenuItem menuItem = new MenuItem(folderMenu, SWT.PUSH);
    menuItem.setText("No folders");
    menuItem.setImage(BooksActivator.getImageDescriptor(
        "resource/icons/toolbar/signed_yes.gif").createImage());
    menuItem.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        folderCLabel.setText("Label changed from the menu");
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e) {
      }
    });
    folderCLabel.addListener(SWT.MouseDown, new Listener() {
      @Override
      public void handleEvent(Event event) {
        if (event.detail == SWT.NONE) {
          Point pt = new Point(event.x, event.y);
          pt = toolbarComposite.toDisplay(pt);
          folderMenu.setLocation(pt.x, pt.y);
          folderMenu.setVisible(true);
        }
      }
    });
    folderCLabel.addListener(SWT.MouseHover, new Listener() {
      @Override
      public void handleEvent(Event event) {
        folderCLabel.setCursor(cursor);
      }
    });
  }

  private void createTreeViewerControl(Composite treeViewerComposite) {

    Tree browseTree = new Tree(treeViewerComposite, SWT.SINGLE | SWT.H_SCROLL
        | SWT.V_SCROLL);
    GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
    browseTree.setLayoutData(gd);

    treeBookViewer = new TreeViewer(browseTree);
    treeBookViewer.setUseHashlookup(true);
    treeBookViewer.setContentProvider(new FoldersViewContentProvider());
    treeBookViewer.setLabelProvider(new FoldersViewLabelProvider());
    treeBookViewer.setInput(BooksActivator.getDefault().getModel()
        .getChildren());

    getViewSite().setSelectionProvider(treeBookViewer);
  }

  /**
   * Passing the focus request to the viewer's control.
   */
  public void setFocus() {
    treeBookViewer.getControl().setFocus();
  }

  @Override
  public void init(IViewSite site) throws PartInitException {
    super.init(site);
    BooksActivator.getDefault().getPreferenceStore().addPropertyChangeListener(
        preferenceListener);
    IConfigurationElement[] config = Platform.getExtensionRegistry()
        .getConfigurationElementsFor(EXTENSION_POINT_STATIC_FILTER_OBSERVABLE);
    if (config.length > 0) {
      for (IConfigurationElement e : config) {
        try {
          o = (Observable) e.createExecutableExtension("class");
        } catch (CoreException e1) {
          // TODO check if this should be caught or not
        }
      }
    }
    if (null != o) {
      o.addObserver(this);
    }
  }

  @Override
  public void dispose() {
    BooksActivator.getDefault().getPreferenceStore()
        .removePropertyChangeListener(preferenceListener);
    if (null != o) {
      o.deleteObserver(this);
    }
  }

  @Override
  public void update(Observable o, Object arg) {
    treeBookViewer.refresh(true);
  }

}