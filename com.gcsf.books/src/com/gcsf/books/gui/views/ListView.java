package com.gcsf.books.gui.views;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.gcsf.books.BooksActivator;
import com.gcsf.books.gui.model.listview.ListViewContentProvider;
import com.gcsf.books.gui.model.listview.ListViewLabelProvider;
import com.gcsf.books.gui.model.listview.TableSorter;
import com.gcsf.books.model.FolderItem;
import com.gcsf.books.model.Item;
import com.gcsf.books.preferences.IPreferenceConstants;

public class ListView extends ViewPart implements ISelectionListener, Observer {

  public static final String ID = "com.gcsf.books.view.stack.list";

  private static final String EXTENSION_POINT_STATIC_FILTER_OBSERVABLE = "com.gcsf.books.filters.static.observable";

  protected Action dummyAction;

  protected Action editBookAction;

  protected Action editBooksAction;

  protected Action removeBooksAction;

  protected Action printAction;

  protected Action selectAllAction;

  protected Action selectRandomAction;

  private TableViewer tableViewer;

  private TableSorter tableSorter;

  private Menu headerMenu;

  private Menu tableMenu;

  boolean doubleClickOccured = false;

  private Observable o = null;

  IPropertyChangeListener preferenceListener = new IPropertyChangeListener() {
    /*
     * @see IPropertyChangeListener.propertyChange()
     */
    public void propertyChange(PropertyChangeEvent event) {
      if (event.getProperty().equals(IPreferenceConstants.P_BOOK_DISPLAY_MODE)) {
        tableViewer.refresh(true);
      }
      if (event.getProperty().equals(
          IPreferenceConstants.P_CHECK_COLLECTION_STATUS)) {
        tableViewer.refresh(true);
      }
    }
  };

  protected void createActions() {
    dummyAction = new Action("DummyAction") {
      public void run() {
        System.out.println("DummyACtion");
      }
    };
    dummyAction.setChecked(false);

    editBookAction = new Action("") {
    };
    editBookAction.setImageDescriptor(BooksActivator
        .getImageDescriptor("resource/icons/book/book_edit.png"));
    editBookAction.setDisabledImageDescriptor(BooksActivator
        .getImageDescriptor("resource/icons/book/book_edit.png"));
    editBooksAction = new Action("") {
    };
    editBooksAction.setImageDescriptor(BooksActivator
        .getImageDescriptor("resource/icons/books/books__pencil.png"));
    removeBooksAction = new Action("") {
    };
    removeBooksAction.setText("Remove book(s) ...");
    removeBooksAction.setImageDescriptor(BooksActivator
        .getImageDescriptor("resource/icons/book/book_delete.png"));
    printAction = new Action("") {
    };
    printAction.setText("Print book(s)");
    printAction.setImageDescriptor(BooksActivator
        .getImageDescriptor("resource/icons/document-print.png"));
    selectAllAction = new Action("Select all") {
      @Override
      public void run() {
        tableViewer.getTable().deselectAll();
        tableViewer.getTable().selectAll();
      }
    };
    selectAllAction.setAccelerator(SWT.CTRL | 'A');
    selectRandomAction = new Action("Select random") {
      @Override
      public void run() {
        int entriesNo = tableViewer.getTable().getItemCount();
        int newIndexSelected = (int) (Math.random() * entriesNo);
        tableViewer.getTable().deselectAll();
        tableViewer.getTable().select(newIndexSelected);
      }
    };
    // TODO add concrete implementation for the actions above
    // TODO check if the contextual menu can be created with extension
    // points
  }

  private void createColumns(final TableViewer viewer, final Composite composite) {
    final Table table = viewer.getTable();
    headerMenu = new Menu(composite);
    String[] titles = { "Id", "Details 1", "Details 2", "Year" };
    int[] bounds = { 100, 100, 100, 100 };
    int[] weights = { 30, 30, 30, 10 };
    /* TableColumnLayout is used for getting all the space in the table */
    TableColumnLayout tableColumnLayout = new TableColumnLayout();
    composite.setLayout(tableColumnLayout);
    /*
     * it is applied on the composite holding whose only child is the table and
     * not on the table itself
     */
    for (int i = 0; i < titles.length; i++) {
      final int index = i;
      final TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
      column.getColumn().setText(titles[i]);
      column.getColumn().setWidth(bounds[i]);
      column.getColumn().setResizable(true);
      column.getColumn().setMoveable(true);
      tableColumnLayout.setColumnData(column.getColumn(), new ColumnWeightData(
          weights[i]));

      // Setting the right sorter
      column.getColumn().addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          tableSorter.setColumn(index);
          int dir = viewer.getTable().getSortDirection();
          if (viewer.getTable().getSortColumn() == column.getColumn()) {
            dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
          } else {

            dir = SWT.DOWN;
          }
          viewer.getTable().setSortDirection(dir);
          viewer.getTable().setSortColumn(column.getColumn());
          viewer.refresh();
        }
      });

    }
    table.setHeaderVisible(true);
    table.setLinesVisible(true);

  }

  private void createHeaderMenu(Composite parent) {
    Menu someMenu = new Menu(parent);
    MenuItem columnMenuItem = new MenuItem(someMenu, SWT.CASCADE);
    columnMenuItem.setText("Columns");
    columnMenuItem.setImage(BooksActivator.getImageDescriptor(
        "resource/icons/table/table_edit.png").createImage());
    Menu columnMenu = new Menu(columnMenuItem);
    int menuIndex = 0;
    MenuItem mItem = new MenuItem(columnMenu, SWT.CHECK, menuIndex);
    menuIndex++;
    mItem.setText("Just main book data");
    mItem.setSelection(true);
    MenuItem mItem3 = new MenuItem(columnMenu, SWT.CHECK, menuIndex);
    menuIndex++;
    mItem3.setText("More book details");
    MenuItem mItem8 = new MenuItem(columnMenu, SWT.SEPARATOR, menuIndex);
    menuIndex++;
    MenuItem mItem10 = new MenuItem(columnMenu, SWT.PUSH, menuIndex);
    menuIndex++;
    mItem10.setText("Choose columns ...");
    columnMenuItem.setMenu(columnMenu);

    MenuItem sortMenuItem = new MenuItem(someMenu, SWT.CASCADE);
    sortMenuItem.setText("Sort Order");
    sortMenuItem.setImage(BooksActivator.getImageDescriptor(
        "resource/icons/table/sort.gif").createImage());
    Menu sortMenu = new Menu(sortMenuItem);
    menuIndex = 0;
    MenuItem mItem1 = new MenuItem(sortMenu, SWT.CHECK, menuIndex);
    menuIndex++;
    mItem1.setText("Author / Year (desc)");
    mItem1.setSelection(true);
    MenuItem mItem31 = new MenuItem(sortMenu, SWT.CHECK, menuIndex);
    menuIndex++;
    mItem31.setText("Author / Title");
    MenuItem mItem41 = new MenuItem(sortMenu, SWT.CHECK, menuIndex);
    menuIndex++;
    mItem41.setText("Nr of Pages (desc)");
    MenuItem mItem51 = new MenuItem(sortMenu, SWT.CHECK, menuIndex);
    menuIndex++;
    mItem51.setText("Purchase Date (desc)");
    MenuItem mItem81 = new MenuItem(sortMenu, SWT.SEPARATOR, menuIndex);
    menuIndex++;
    MenuItem mItem101 = new MenuItem(sortMenu, SWT.PUSH, menuIndex);
    menuIndex++;
    mItem101.setText("Choose Sort Fields ...");
    sortMenuItem.setMenu(sortMenu);

    setHeaderMenu(someMenu);

  }

  /**
   * This is a callback that will allow us to create the viewer and initialize
   * it.
   */
  public void createPartControl(Composite parent) {
    tableViewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL
        | SWT.MULTI | SWT.FULL_SELECTION);
    createColumns(tableViewer, parent);
    tableViewer.setContentProvider(new ListViewContentProvider());
    tableViewer.setUseHashlookup(true);

    // ILabelDecorator decorator =
    // PlatformUI.getWorkbench().getDecoratorManager()
    // .getLabelDecorator();
    // tableViewer.setLabelProvider(new TableDecoratingLabelProvider(
    // new ListViewLabelProvider(), decorator));

    tableViewer.setLabelProvider(new ListViewLabelProvider());
    tableViewer.setContentProvider(new ArrayContentProvider());
    tableSorter = new TableSorter();
    tableViewer.setSorter(tableSorter);

    createActions();
    createTableMenu();
    createHeaderMenu(parent);
    setupMenus(parent);

    // New
    hookDoubleClickCommand();

    getViewSite().setSelectionProvider(tableViewer);
    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService()
        .addSelectionListener(this);
  }

  private void setupMenus(final Composite parent) {
    tableViewer.getTable().addListener(SWT.MenuDetect, new Listener() {
      public void handleEvent(Event event) {
        Point pt = parent.getDisplay().map(null, tableViewer.getTable(),
            new Point(event.x, event.y));
        Rectangle clientArea = tableViewer.getTable().getClientArea();
        boolean header = clientArea.y <= pt.y
            && pt.y < (clientArea.y + tableViewer.getTable().getHeaderHeight());
        tableViewer.getTable().setMenu(
            header ? getHeaderMenu() : getTableMenu());
      }
    });

    /*
     * IMPORTANT: Dispose the menus (only the current menu, set with setMenu(),
     * will be automatically disposed)
     */
    tableViewer.getTable().addListener(SWT.Dispose, new Listener() {
      public void handleEvent(Event event) {
        getHeaderMenu().dispose();
        getTableMenu().dispose();
      }
    });
  }

  private void createTableMenu() {
    final MenuManager mgr = new MenuManager();
    mgr.setRemoveAllWhenShown(true);
    final Separator separator = new Separator();
    separator.setVisible(true);

    mgr.addMenuListener(new IMenuListener() {

      /*
       * (non-Javadoc)
       * 
       * @see
       * org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.
       * jface.action.IMenuManager)
       */
      public void menuAboutToShow(IMenuManager manager) {
        IStructuredSelection selection = (IStructuredSelection) tableViewer
            .getSelection();

        if (!selection.isEmpty()) {
          if (selection.size() == 1) {
            /* only one book selected */
            editBookAction.setText("Edit book: "
                + ((Item) (selection.getFirstElement())).getId());
            editBooksAction.setText("Edit books ...");
            editBookAction.setEnabled(true);
            editBooksAction.setEnabled(false);
          } else {
            /* multiple books selected */
            editBookAction.setText("Edit book: ");
            editBooksAction.setText("Edit books");
            editBookAction.setEnabled(false);
            editBooksAction.setEnabled(true);
          }
          mgr.add(editBookAction);
          mgr.add(editBooksAction);
          mgr.add(removeBooksAction);
          mgr.add(separator);
          mgr.add(printAction);
          mgr.add(selectAllAction);
          mgr.add(selectRandomAction);
        }
      }
    });
    setTableMenu(mgr.createContextMenu(tableViewer.getControl()));
  }

  @Override
  public void dispose() {
    tableViewer.getLabelProvider().dispose();
    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService()
        .removeSelectionListener(this);
    BooksActivator.getDefault().getPreferenceStore()
        .removePropertyChangeListener(preferenceListener);
    if (null != o) {
      o.deleteObserver(this);
    }
  }

  private void hookDoubleClickCommand() {
    tableViewer.addDoubleClickListener(new IDoubleClickListener() {
      public void doubleClick(DoubleClickEvent event) {
        doubleClickOccured = true;
        // force a selection change
        selectionChanged(getSite().getPart(), event.getSelection());
      }
    });
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
  public void selectionChanged(IWorkbenchPart part, ISelection selection) {
    if (part != this) {
      if (!selection.isEmpty()) {
        if ((((IStructuredSelection) selection).getFirstElement()) instanceof FolderItem) {
          tableViewer.setInput(((FolderItem) ((IStructuredSelection) selection)
              .getFirstElement()).getChildren());
        } else {
          // Item aItem = (Item) (((IStructuredSelection) selection)
          // .getFirstElement());
          // ArrayList<Item> aList = new ArrayList<Item>();
          // aList.add(aItem);
          // tableViewer.setInput(aList);
          tableViewer.setInput(null);
        }
      } else {
        tableViewer.setInput(null);
      }
    } else {
      if (!selection.isEmpty()) {
        if (doubleClickOccured) {
          doubleClickOccured = false;
          if ((((IStructuredSelection) selection).getFirstElement()) instanceof FolderItem) {
            tableViewer
                .setInput(((FolderItem) ((IStructuredSelection) selection)
                    .getFirstElement()).getChildren());
          }
        }
      }
    }

  }

  /**
   * Passing the focus request to the viewer's control.
   */
  public void setFocus() {
    tableViewer.getControl().setFocus();
  }

  public Menu getHeaderMenu() {
    return headerMenu;
  }

  public void setHeaderMenu(Menu headerMenu) {
    this.headerMenu = headerMenu;
  }

  public Menu getTableMenu() {
    return tableMenu;
  }

  public void setTableMenu(Menu tableMenu) {
    this.tableMenu = tableMenu;
  }

  @Override
  public void update(Observable o, Object arg) {
    tableViewer.refresh();
  }

}
