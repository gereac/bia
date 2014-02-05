package com.gcsf.books.preferences.dialogs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IPageChangeProvider;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * A tab presentation of the preference pages. Each page is represented by a tab
 * */

public class MyPreferencesDialog extends Dialog implements
    IPreferencePageContainer, IPageChangeProvider {

  /**
   * Layout for the page container.
   * 
   */
  private class PageLayout extends Layout {
    public Point computeSize(Composite composite, int wHint, int hHint,
        boolean force) {
      if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
        return new Point(wHint, hHint);
      }
      int x = minimumPageSize.x;
      int y = minimumPageSize.y;
      Control[] children = composite.getChildren();
      for (int i = 0; i < children.length; i++) {
        Point size = children[i].computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
        x = Math.max(x, size.x);
        y = Math.max(y, size.y);
      }

      // As pages can implement their own computeSize
      // take it into account
      if (currentPage != null) {
        Point size = currentPage.computeSize();
        x = Math.max(x, size.x);
        y = Math.max(y, size.y);
      }

      if (wHint != SWT.DEFAULT) {
        x = wHint;
      }
      if (hHint != SWT.DEFAULT) {
        y = hHint;
      }
      return new Point(x, y);
    }

    public void layout(Composite composite, boolean force) {
      Rectangle rect = composite.getClientArea();
      Control[] children = composite.getChildren();
      for (int i = 0; i < children.length; i++) {
        children[i].setSize(rect.width, rect.height);
      }
    }
  }

  private static final String EXTENSION_POINT_ID_PREFERENCES = "org.eclipse.ui.preferencePages";

  private String SHELL_TITLE = "Tabbed preferences";

  /**
   * Return code used when dialog failed
   */
  protected static final int FAILED = 2;

  /**
   * Collection of buttons created by the <code>createButton</code> method.
   */
  private HashMap buttons = new HashMap();

  private Point lastShellSize;

  /**
   * The minimum page size; 400 by 400 by default.
   * 
   * @see #setMinimumPageSize(Point)
   */
  private Point minimumPageSize = new Point(200, 200);

  private IPreferencePage currentPage;

  /**
   * The Composite in which a page is shown.
   */
  private Composite pageContainer;

  private ScrolledComposite scrolled;

  private CTabFolder cTabFolder;

  // The id of the last page that was selected
  private static String lastPreferenceId = null;

  private ListenerList pageChangedListeners = new ListenerList();

  /**
   * Preference store, initially <code>null</code> meaning none.
   * 
   * @see #setPreferenceStore
   */
  private IPreferenceStore preferenceStore;

  // private DialogMessageArea messageArea;

  public MyPreferencesDialog(Shell shell) {
    super(shell);
  }

  @Override
  protected Control createContents(final Composite parent) {
    final Control[] control = new Control[1];
    BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
      public void run() {
        control[0] = MyPreferencesDialog.super.createContents(parent);
        GridLayout parentLayout = (GridLayout) ((Composite) control[0])
            .getLayout();
        parentLayout.numColumns = 2;
        parentLayout.makeColumnsEqualWidth = false;
        // Add the first page
        selectSavedItem();
      }
    });
    return control[0];
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    final Composite composite = (Composite) super.createDialogArea(parent);
    GridLayout parentLayout = ((GridLayout) composite.getLayout());
    parentLayout.marginHeight = 0;
    parentLayout.marginWidth = 0;
    parentLayout.verticalSpacing = 0;
    parentLayout.horizontalSpacing = 0;

    GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
    composite.setLayoutData(layoutData);

    cTabFolder = new CTabFolder(composite, SWT.NONE | SWT.BORDER);

    GridData layoutDataTf = new GridData(SWT.FILL, SWT.FILL, true, true);
    cTabFolder.setLayoutData(layoutDataTf);
    GridLayout layoutTf = new GridLayout();
    layoutTf.horizontalSpacing = SWT.FILL;
    layoutTf.verticalSpacing = SWT.FILL;
    cTabFolder.setLayout(layoutTf);

    cTabFolder.setSimple(false);
    cTabFolder.addSelectionListener(new SelectionListener() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        CTabItem myTab = (CTabItem) e.item;
        setSelectedNodePreference(myTab.getText());
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e) {
        // nothing to do here
      }
    });

    Display display = Display.getCurrent();
    Color titleForeColor = display.getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
    Color titleBackColor1 = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
    Color titleBackColor2 = display
        .getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);

    cTabFolder.setSelectionForeground(titleForeColor);
    cTabFolder.setSelectionBackground(new Color[] { titleBackColor1,
        titleBackColor2 }, new int[] { 100 }, true);

    PreferenceManager crtManager = PlatformUI.getWorkbench()
        .getPreferenceManager();
    List<?> aList = crtManager.getElements(PreferenceManager.PRE_ORDER);
    for (Iterator<?> aIt = aList.iterator(); aIt.hasNext();) {
      IPreferenceNode aNode = (IPreferenceNode) aIt.next();
      CTabItem aItem = new CTabItem(cTabFolder, SWT.NONE);
      aItem.setText(aNode.getLabelText());
      Composite aComposite = new Composite(cTabFolder, SWT.NONE);
      GridData layoutDataAc = new GridData(SWT.FILL, SWT.FILL, true, true);
      aComposite.setLayoutData(layoutDataAc);
      GridLayout layoutAc = new GridLayout();
      layoutAc.horizontalSpacing = SWT.FILL;
      layoutAc.verticalSpacing = SWT.FILL;
      aComposite.setLayout(layoutAc);

      aItem.setControl(aComposite);

      pageContainer = createPageContainer(aComposite);
      GridData pageContainerData = new GridData(SWT.FILL, SWT.FILL, true, true);
      pageContainerData.horizontalIndent = IDialogConstants.HORIZONTAL_MARGIN;
      pageContainer.setLayoutData(pageContainerData);
      showPage(aNode);
      update();
    }

    return composite;
  }

  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText(SHELL_TITLE);
    newShell.addShellListener(new ShellAdapter() {
      public void shellActivated(ShellEvent e) {
        if (lastShellSize == null) {
          lastShellSize = getShell().getSize();
        }
      }
    });
  }

  protected void constrainShellSize() {
    super.constrainShellSize();
    // record opening shell size
    if (lastShellSize == null) {
      lastShellSize = getShell().getSize();
    }
  }

  @Override
  protected Control createButtonBar(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    // create a layout with spacing and margins appropriate for the font
    // size.
    GridLayout layout = new GridLayout();
    layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
    layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
    layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
    layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
    composite.setLayout(layout);
    GridData data = new GridData(SWT.FILL, SWT.FILL, false, true);
    composite.setLayoutData(data);
    composite.setFont(parent.getFont());
    // Add the buttons to the button bar.
    createButtonsForButtonBar(composite);
    return composite;
  }

  protected void createButtonsForButtonBar(Composite parent) {
    // create OK and Cancel buttons by default
    getShell().setDefaultButton(
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
            true));
    createButton(parent, IDialogConstants.CANCEL_ID,
        IDialogConstants.CANCEL_LABEL, false);
    // additionally create Help Button
    // if (isHelpAvailable()) {
    createButton(parent, IDialogConstants.HELP_ID, IDialogConstants.HELP_LABEL,
        false);
    // }
  }

  protected Button createButton(Composite parent, int id, String label,
      boolean defaultButton) {
    Button button = new Button(parent, SWT.PUSH | SWT.CENTER);
    button.setText(label);
    button.setFont(JFaceResources.getDialogFont());
    button.setData(new Integer(id));
    button.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        buttonPressed(((Integer) event.widget.getData()).intValue());
      }
    });
    if (defaultButton) {
      Shell shell = parent.getShell();
      if (shell != null) {
        shell.setDefaultButton(button);
      }
    }
    buttons.put(new Integer(id), button);
    setButtonLayoutData(button);
    return button;
  }

  /**
   * Set the layout data of the button to a GridData with appropriate heights
   * and widths.
   * 
   * @param button
   */
  protected void setButtonLayoutData(Button button) {
    GridData layoutData = new GridData();
    layoutData.grabExcessHorizontalSpace = true;
    layoutData.horizontalAlignment = GridData.FILL;
    int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
    Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
    layoutData.widthHint = Math.max(widthHint, minSize.x);
    button.setLayoutData(layoutData);
  }

  public boolean isExtensionImplemented() {
    boolean isImplemented = true;
    IConfigurationElement[] elements = Platform.getExtensionRegistry()
        .getConfigurationElementsFor(EXTENSION_POINT_ID_PREFERENCES);
    if (0 == elements.length) {
      isImplemented = false;
    }
    return isImplemented;
  }

  protected void buttonPressed(int buttonId) {
    switch (buttonId) {
      case IDialogConstants.OK_ID: {
        okPressed();
        return;
      }
      case IDialogConstants.CANCEL_ID: {
        cancelPressed();
        return;
      }
      case IDialogConstants.HELP_ID: {
        helpPressed();
        return;
      }
    }
  }

  @Override
  protected void cancelPressed() {
    // Inform all pages that we are canceling
    Iterator<?> nodes = PlatformUI.getWorkbench().getPreferenceManager()
        .getElements(PreferenceManager.PRE_ORDER).iterator();
    while (nodes.hasNext()) {
      final IPreferenceNode node = (IPreferenceNode) nodes.next();
      if (getPage(node) != null) {
        SafeRunnable.run(new SafeRunnable() {
          public void run() {
            if (!getPage(node).performCancel()) {
              return;
            }
          }

        });
      }
    }

    // Give subclasses the choice to save the state of the preference pages if
    // needed
    handleSave();

    setReturnCode(CANCEL);
    close();
  }

  /**
   * Save the values specified in the pages.
   * <p>
   * The default implementation of this framework method saves all pages of type
   * <code>PreferencePage</code> (if their store needs saving and is a
   * <code>PreferenceStore</code>).
   * </p>
   * <p>
   * Subclasses may override.
   * </p>
   */
  protected void handleSave() {
    Iterator<?> nodes = PlatformUI.getWorkbench().getPreferenceManager()
        .getElements(PreferenceManager.PRE_ORDER).iterator();
    while (nodes.hasNext()) {
      IPreferenceNode node = (IPreferenceNode) nodes.next();
      IPreferencePage page = node.getPage();
      if (page instanceof PreferencePage) {
        // Save now in case the workbench does not shutdown cleanly
        IPreferenceStore store = ((PreferencePage) page).getPreferenceStore();
        if (store != null && store.needsSaving()
            && store instanceof IPersistentPreferenceStore) {
          try {
            ((IPersistentPreferenceStore) store).save();
          } catch (IOException e) {
            String message = JFaceResources
                .format(
                    "PreferenceDialog.saveErrorMessage", new Object[] { page.getTitle(), e.getMessage() }); //$NON-NLS-1$
            Policy.getStatusHandler().show(
                new Status(IStatus.ERROR, Policy.JFACE, message, e),
                JFaceResources.getString("PreferenceDialog.saveErrorTitle")); //$NON-NLS-1$

          }
        }
      }
    }
  }

  @Override
  public boolean close() {
    // Do this is in a SafeRunnable as it may run client code
    SafeRunnable runnable = new SafeRunnable() {
      /*
       * (non-Javadoc)
       * 
       * @see org.eclipse.core.runtime.ISafeRunnable#run()
       */
      public void run() throws Exception {
        List<?> nodes = PlatformUI.getWorkbench().getPreferenceManager()
            .getElements(PreferenceManager.PRE_ORDER);
        for (int i = 0; i < nodes.size(); i++) {
          IPreferenceNode node = (IPreferenceNode) nodes.get(i);
          node.disposeResources();
        }
      }

      /*
       * (non-Javadoc)
       * 
       * @see
       * org.eclipse.jface.util.SafeRunnable#handleException(java.lang.Throwable
       * )
       */
      public void handleException(Throwable e) {
        super.handleException(e);
        clearSelectedNode();// Do not cache a node with problems
      }
    };
    SafeRunner.run(runnable);

    return super.close();
  }

  /**
   * Clear the last selected node. This is so that we not chache the last
   * selection in case of an error.
   */
  void clearSelectedNode() {
    setSelectedNodePreference(null);
  }

  @Override
  /**
   * The preference dialog implementation of this <code>Dialog</code>
   * framework method sends <code>performOk</code> to all pages of the
   * preference dialog, then calls <code>handleSave</code> on this dialog to
   * save any state, and then calls <code>close</code> to close this dialog.
   */
  protected void okPressed() {
    SafeRunnable.run(new SafeRunnable() {
      private boolean errorOccurred;

      /*
       * (non-Javadoc)
       * 
       * @see org.eclipse.core.runtime.ISafeRunnable#run()
       */
      public void run() {
        getButton(IDialogConstants.OK_ID).setEnabled(false);
        errorOccurred = false;
        boolean hasFailedOK = false;
        try {
          // Notify all the pages and give them a chance to abort
          Iterator<?> nodes = PlatformUI.getWorkbench().getPreferenceManager()
              .getElements(PreferenceManager.PRE_ORDER).iterator();
          while (nodes.hasNext()) {
            IPreferenceNode node = (IPreferenceNode) nodes.next();
            IPreferencePage page = node.getPage();
            if (page != null) {
              if (!page.performOk()) {
                hasFailedOK = true;
                return;
              }
            }
          }
        } catch (Exception e) {
          handleException(e);
        } finally {
          // Don't bother closing if the OK failed
          if (hasFailedOK) {
            setReturnCode(FAILED);
            getButton(IDialogConstants.OK_ID).setEnabled(true);
            return;
          }

          if (!errorOccurred) {
            // Give subclasses the choice to save the state of the
            // preference pages.
            handleSave();
          }
          setReturnCode(OK);
          close();
        }
      }

      /*
       * (non-Javadoc)
       * 
       * @see
       * org.eclipse.core.runtime.ISafeRunnable#handleException(java.lang.Throwable
       * )
       */
      public void handleException(Throwable e) {
        errorOccurred = true;

        Policy.getLog().log(
            new Status(IStatus.ERROR, Policy.JFACE, 0, e.toString(), e));

        clearSelectedNode();
        String message = JFaceResources.getString("SafeRunnable.errorMessage"); //$NON-NLS-1$

        Policy.getStatusHandler().show(
            new Status(IStatus.ERROR, Policy.JFACE, message, e),
            JFaceResources.getString("Error")); //$NON-NLS-1$

      }
    });
  }

  /**
   * Notifies of the pressing of the Help button.
   * <p>
   * The default implementation of this framework method calls
   * <code>performHelp</code> on the currently active page.
   * </p>
   */
  private void helpPressed() {
    if (currentPage != null) {
      currentPage.performHelp();
    }
  }

  @Override
  /*
   * 
   * @see
   * org.eclipse.jface.preference.IPreferencePageContainer#getPreferenceStore()
   */
  public IPreferenceStore getPreferenceStore() {
    return preferenceStore;
  }

  @Override
  public void updateTitle() {
    if (currentPage == null) {
      return;
    }
    getShell().setText(SHELL_TITLE + " - " + currentPage.getTitle());
  }

  /**
   * Shows the preference page corresponding to the given preference node. Does
   * nothing if that page is already current.
   * 
   * @param node
   *          the preference node, or <code>null</code> if none
   * @return <code>true</code> if the page flip was successful, and
   *         <code>false</code> is unsuccessful
   */
  protected boolean showPage(IPreferenceNode node) {
    if (node == null) {
      return false;
    }
    // Create the page if necessary
    if (node.getPage() == null) {
      createPage(node);
    }
    if (node.getPage() == null) {
      return false;
    }
    IPreferencePage newPage = getPage(node);
    if (newPage == currentPage) {
      return true;
    }
    if (currentPage != null) {
      if (!currentPage.okToLeave()) {
        return false;
      }
    }
    IPreferencePage oldPage = currentPage;
    currentPage = newPage;
    // Set the new page's container
    currentPage.setContainer(this);
    // Ensure that the page control has been created
    // (this allows lazy page control creation)
    if (currentPage.getControl() == null) {
      final boolean[] failed = { false };
      SafeRunnable.run(new ISafeRunnable() {
        public void handleException(Throwable e) {
          failed[0] = true;
        }

        public void run() {
          createPageControl(currentPage, pageContainer);
        }
      });
      if (failed[0]) {
        return false;
      }
      // the page is responsible for ensuring the created control is
      // accessible
      // via getControl.
      Assert.isNotNull(currentPage.getControl());
    }
    // Force calculation of the page's description label because
    // label can be wrapped.
    final Point[] size = new Point[1];
    final Point failed = new Point(-1, -1);
    SafeRunnable.run(new ISafeRunnable() {
      public void handleException(Throwable e) {
        size[0] = failed;
      }

      public void run() {
        size[0] = currentPage.computeSize();
      }
    });
    if (size[0].equals(failed)) {
      return false;
    }
    Point contentSize = size[0];
    // Do we need resizing. Computation not needed if the
    // first page is inserted since computing the dialog's
    // size is done by calling dialog.open().
    // Also prevent auto resize if the user has manually resized
    Shell shell = getShell();
    Point shellSize = shell.getSize();
    if (oldPage != null) {
      pageContainer.pack();
      Rectangle rect = pageContainer.getClientArea();
      Point containerSize = new Point(rect.width, rect.height);
      int hdiff = contentSize.x - containerSize.x;
      int vdiff = contentSize.y - containerSize.y;
      if (null == lastShellSize) {
        lastShellSize = shell.getSize();
      }
      if ((hdiff > 0 || vdiff > 0) && shellSize.equals(lastShellSize)) {
        hdiff = Math.max(0, hdiff);
        vdiff = Math.max(0, vdiff);
        setShellSize(shellSize.x + hdiff, shellSize.y + vdiff);
        lastShellSize = shell.getSize();
        if (currentPage.getControl().getSize().x == 0) {
          currentPage.getControl().setSize(containerSize);
        }

      } else {
        currentPage.setSize(containerSize);
      }
    }
    scrolled.setMinSize(contentSize);

    return true;
  }

  protected void createPage(IPreferenceNode node) {
    node.createPage();
  }

  protected IPreferencePage getPage(IPreferenceNode node) {
    return node.getPage();
  }

  /**
   * Create the page control for the supplied page.
   * 
   * @param page
   *          - the preference page to be shown
   * @param parent
   *          - the composite to parent the page
   * 
   */
  protected void createPageControl(IPreferencePage page, Composite parent) {
    page.createControl(parent);
  }

  /**
   * Changes the shell size to the given size, ensuring that it is no larger
   * than the display bounds.
   * 
   * @param width
   *          the shell width
   * @param height
   *          the shell height
   */
  private void setShellSize(int width, int height) {
    Rectangle preferred = getShell().getBounds();
    preferred.width = width;
    preferred.height = height;
    getShell().setBounds(getConstrainedShellBounds(preferred));
  }

  /**
   * Updates this dialog's controls to reflect the current page.
   */
  protected void update() {
    updateTitle();
    // Saved the selected node in the preferences
    // setSelectedNode();
    firePageChanged(new PageChangedEvent(this, getCurrentPage()));
  }

  /**
   * Sets the name of the selected item preference.
   * 
   * @param pageId
   *          The identifier for the page
   */
  protected void setSelectedNodePreference(String pageId) {
    lastPreferenceId = pageId;
  }

  /**
   * Returns the currentPage.
   * 
   * @return IPreferencePage
   */
  protected IPreferencePage getCurrentPage() {
    return currentPage;
  }

  /**
   * Notifies any selection changed listeners that the selected page has
   * changed. Only listeners registered at the time this method is called are
   * notified.
   * 
   * @param event
   *          a selection changed event
   * 
   * @see IPageChangedListener#pageChanged
   * 
   * @since 3.1
   */
  protected void firePageChanged(final PageChangedEvent event) {
    Object[] listeners = pageChangedListeners.getListeners();
    for (int i = 0; i < listeners.length; i++) {
      final IPageChangedListener l = (IPageChangedListener) listeners[i];
      SafeRunnable.run(new SafeRunnable() {
        public void run() {
          l.pageChanged(event);
        }
      });
    }
  }

  /**
   * @see org.eclipse.jface.dialogs.IPageChangeProvider#addPageChangedListener(org.eclipse.jface.dialogs.IPageChangedListener)
   */
  public void addPageChangedListener(IPageChangedListener listener) {
    pageChangedListeners.add(listener);
  }

  /**
   * @see org.eclipse.jface.dialogs.IPageChangeProvider#removePageChangedListener(org.eclipse.jface.dialogs.IPageChangedListener)
   * @since 3.1
   */
  public void removePageChangedListener(IPageChangedListener listener) {
    pageChangedListeners.remove(listener);
  }

  /**
   * @see org.eclipse.jface.dialogs.IPageChangeProvider#getSelectedPage()
   */
  public Object getSelectedPage() {
    return getCurrentPage();
  }

  /**
   * Creates the inner page container.
   * 
   * @param parent
   * @return Composite
   */
  protected Composite createPageContainer(Composite parent) {
    Composite outer = new Composite(parent, SWT.NONE);
    GridData outerData = new GridData(SWT.FILL, SWT.FILL, true, true);
    outerData.horizontalIndent = IDialogConstants.HORIZONTAL_MARGIN;
    outer.setLayout(new GridLayout());
    outer.setLayoutData(outerData);
    // Create an outer composite for spacing
    scrolled = new ScrolledComposite(outer, SWT.V_SCROLL | SWT.H_SCROLL);
    // always show the focus control
    scrolled.setShowFocusedControl(true);
    scrolled.setExpandHorizontal(true);
    scrolled.setExpandVertical(true);
    GridData scrolledData = new GridData(SWT.FILL, SWT.FILL, true, true);
    scrolled.setLayoutData(scrolledData);
    Composite result = new Composite(scrolled, SWT.NONE);
    GridData resultData = new GridData(SWT.FILL, SWT.FILL, true, true);
    result.setLayout(getPageLayout());
    result.setLayoutData(resultData);
    scrolled.setContent(result);

    return result;
  }

  /**
   * Return the layout for the composite that contains the pages.
   * 
   * @return PageLayout
   */
  protected Layout getPageLayout() {
    return new PageLayout();
  }

  /**
   * Selects the saved item in the CTabFolder list of preference pages. If it
   * cannot do this it selects the first one.
   */
  protected void selectSavedItem() {
    int selectionIndex = 0;
    IPreferenceNode node = findNodeMatching(getSelectedNodePreference());
    if (node == null) {
      // the node should exist ... otherwise there is a problem
      // TODO add error handling here
    } else {
      // find the corresponding tab
      CTabItem[] tabs = cTabFolder.getItems();
      for (int i = 0; i < tabs.length; i++) {
        if (tabs[i].getText().equals(node.getLabelText())) {
          selectionIndex = i;
        }
      }
    }
    // select the correct tab
    getCTabFolder().setSelection(selectionIndex);
  }

  /** Return the CTabFolder for this dialog */
  private CTabFolder getCTabFolder() {
    return cTabFolder;
  }

  /**
   * Get the name of the selected item preference
   * 
   * @return String
   */
  protected String getSelectedNodePreference() {
    return lastPreferenceId;
  }

  /**
   * Find the <code>IPreferenceNode</code> that has data the same id as the
   * supplied value.
   * 
   * @param nodeId
   *          the id to search for.
   * @return <code>IPreferenceNode</code> or <code>null</code> if not found.
   */
  protected IPreferenceNode findNodeMatching(String nodeId) {
    List<?> nodes = PlatformUI.getWorkbench().getPreferenceManager()
        .getElements(PreferenceManager.PRE_ORDER);
    for (Iterator<?> i = nodes.iterator(); i.hasNext();) {
      IPreferenceNode node = (IPreferenceNode) i.next();
      if (node.getLabelText().equals(nodeId)) {
        return node;
      }
    }
    return null;
  }

  /**
   * Returns the button created by the method <code>createButton</code> for the
   * specified ID as defined on <code>IDialogConstants</code>. If
   * <code>createButton</code> was never called with this ID, or if
   * <code>createButton</code> is overridden, this method will return
   * <code>null</code>.
   * 
   * @param id
   *          the id of the button to look for
   * 
   * @return the button for the ID or <code>null</code>
   * 
   * @see #createButton(Composite, int, String, boolean)
   */
  protected Button getButton(int id) {
    return (Button) buttons.get(Integer.valueOf(id));
  }

  @Override
  protected boolean isResizable() {
    return true;
  }

  @Override
  public void updateButtons() {
    // TODO Auto-generated method stub
  }

  @Override
  public void updateMessage() {
    // TODO Auto-generated method stub
  }

}
