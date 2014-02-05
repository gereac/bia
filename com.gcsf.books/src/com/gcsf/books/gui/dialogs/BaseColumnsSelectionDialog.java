package com.gcsf.books.gui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.gcsf.books.BooksActivator;
import com.gcsf.books.gui.dialogs.model.ColumnSet;
import com.gcsf.books.gui.dialogs.model.ColumnSetProvider;
import com.gcsf.books.gui.model.listcolumns.ListColumnsContentProvider;
import com.gcsf.books.gui.model.listcolumns.ListColumnsLabelProvider;
import com.gcsf.books.gui.model.listcolumns.ListEditingSupport;
import com.gcsf.books.gui.model.listcolumns.MyFocusCellHighlighter;

public class BaseColumnsSelectionDialog extends Dialog {

  private static final String SHELL_TITLE = "Choose columns ...";

  private Point lastShellSize;

  // The last known tree width
  private static int lastTableWidth = 180;

  private TableViewer columnsTableViewer;

  private TableViewer listTableViewer;

  /**
   * Collection of buttons created by the <code>createButton</code> method.
   */
  // private HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();

  // String oldCellName = "";

  public BaseColumnsSelectionDialog(Shell parentShell) {
    super(parentShell);
  }

  protected boolean isResizable() {
    return true;
  }

  @Override
  protected Control createContents(final Composite parent) {
    // create the top level composite for the dialog
    Composite composite = new Composite(parent, 0);
    GridLayout layout = new GridLayout();
    layout.numColumns = 4;
    layout.makeColumnsEqualWidth = false;
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    layout.verticalSpacing = 0;
    layout.horizontalSpacing = 0;
    composite.setLayout(layout);
    composite.setLayoutData(new GridData(GridData.FILL_BOTH));
    applyDialogFont(composite);
    // initialize the dialog units
    initializeDialogUnits(composite);

    // create the dialog area and button bar
    dialogArea = createDialogArea(composite);
    createSash(composite, dialogArea);

    Label versep = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
    GridData verGd = new GridData(GridData.FILL_VERTICAL
        | GridData.GRAB_VERTICAL);

    versep.setLayoutData(verGd);
    versep.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true));

    buttonBar = createButtonBar(composite);

    Composite bottomComposite = new Composite(composite, SWT.NONE);
    GridLayout gridLayout = new GridLayout(1, false);
    gridLayout.marginWidth = 0;
    gridLayout.marginHeight = 0;
    gridLayout.verticalSpacing = 0;
    gridLayout.horizontalSpacing = 0;
    bottomComposite.setLayout(gridLayout);
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    gridData.widthHint = SWT.DEFAULT;
    gridData.heightHint = SWT.DEFAULT;
    gridData.horizontalSpan = 4;
    bottomComposite.setLayoutData(gridData);
    Label horSep = new Label(bottomComposite, SWT.None | SWT.SEPARATOR
        | SWT.HORIZONTAL);
    GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, false);
    gridData2.widthHint = SWT.DEFAULT;
    gridData2.heightHint = SWT.DEFAULT;
    horSep.setLayoutData(gridData2);
    CLabel bottomLabel = new CLabel(bottomComposite, SWT.NONE);
    bottomLabel.setText("Drag and drop to change the order of the fields");
    bottomLabel.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(
        ISharedImages.IMG_OBJS_INFO_TSK));
    GridData gridData3 = new GridData(SWT.FILL, SWT.FILL, true, false);
    gridData3.widthHint = SWT.DEFAULT;
    gridData3.heightHint = SWT.DEFAULT;
    bottomLabel.setLayoutData(gridData3);

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
  protected Control createDialogArea(Composite parent) {
    final Composite composite = (Composite) super.createDialogArea(parent);
    GridLayout parentLayout = ((GridLayout) composite.getLayout());
    parentLayout.marginHeight = 0;
    parentLayout.marginWidth = 0;
    parentLayout.verticalSpacing = 0;
    parentLayout.horizontalSpacing = 0;
    GridData parentData = (GridData) composite.getLayoutData();
    parentData.grabExcessHorizontalSpace = false;
    composite.setBackground(composite.getDisplay().getSystemColor(
        SWT.COLOR_LIST_BACKGROUND));

    GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
    composite.setLayoutData(layoutData);

    createColumnsTableAreaContents(composite);

    return composite;
  }

  @Override
  protected Control createButtonBar(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    // create a layout with spacing and margins appropriate for the font
    // size.
    GridLayout layout = new GridLayout();
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    layout.verticalSpacing = 0;
    layout.horizontalSpacing = 0;
    composite.setLayout(layout);
    GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
    composite.setLayoutData(data);
    composite.setFont(parent.getFont());

    // create the real buttons composite
    Composite bComposite = new Composite(composite, SWT.NONE);
    GridLayout bLayout = new GridLayout();
    bLayout.marginLeft = 10;
    bComposite.setLayout(bLayout);
    GridData bData = new GridData(SWT.LEFT, SWT.FILL, false, false);
    bComposite.setLayoutData(bData);

    // Add the buttons to the button bar.
    createButtonsForButtonBar(bComposite);

    // create the column set composite
    Composite cComposite = new Composite(composite, SWT.NONE);
    GridLayout cLayout = new GridLayout();
    cLayout.marginHeight = 0;
    cLayout.marginWidth = 0;
    cLayout.verticalSpacing = 0;
    cLayout.horizontalSpacing = 0;
    cComposite.setLayout(cLayout);
    GridData cData = new GridData(SWT.FILL, SWT.FILL, false, true);
    cComposite.setLayoutData(cData);

    Composite ctComposite = new Composite(cComposite, SWT.NONE);
    GridLayout ctLayout = new GridLayout();
    ctLayout.numColumns = 4;
    ctLayout.makeColumnsEqualWidth = true;
    ctComposite.setLayout(ctLayout);
    GridData ctData = new GridData(SWT.FILL, SWT.FILL, false, false);
    ctComposite.setLayoutData(ctData);

    Label clLabel = new Label(ctComposite, SWT.NONE);
    clLabel.setText("Saved column sets");
    GridData clLabelData = new GridData(SWT.FILL, SWT.FILL, false, false);
    clLabelData.horizontalSpan = 4;
    clLabel.setLayoutData(clLabelData);

    Button addButton = new Button(ctComposite, SWT.PUSH);
    addButton.setToolTipText("Add column set");
    addButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(
        ISharedImages.IMG_OBJ_ADD));
    addButton.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        ColumnSet newColumnSet = new ColumnSet("aatestaa", null);
        ColumnSetProvider.getInstance().getColumnSets().add(newColumnSet);
        listTableViewer.refresh();
        listTableViewer.editElement(newColumnSet, 0);
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e) {
        // TODO Auto-generated method stub
      }
    });
    Button dupButton = new Button(ctComposite, SWT.PUSH);
    dupButton.setToolTipText("Duplicate column set");
    dupButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(
        ISharedImages.IMG_TOOL_COPY));
    Button renButton = new Button(ctComposite, SWT.PUSH);
    renButton.setToolTipText("Rename column set");
    renButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(
        ISharedImages.IMG_TOOL_CUT));
    renButton.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        listTableViewer.editElement(((IStructuredSelection) listTableViewer
            .getSelection()).getFirstElement(), 0);
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e) {
        // TODO Auto-generated method stub

      }
    });
    Button delButton = new Button(ctComposite, SWT.PUSH);
    delButton.setToolTipText("Delete column set");
    delButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(
        ISharedImages.IMG_TOOL_DELETE));

    Composite clComposite = new Composite(cComposite, SWT.NONE);
    GridLayout clLayout = new GridLayout();
    clLayout.marginHeight = 0;
    clLayout.marginWidth = 0;
    clLayout.verticalSpacing = 0;
    clLayout.horizontalSpacing = 0;
    clComposite.setLayout(clLayout);
    GridData clData = new GridData(SWT.FILL, SWT.FILL, true, true);
    clComposite.setLayoutData(clData);
    clComposite.setBackground(parent.getDisplay().getSystemColor(
        SWT.COLOR_LIST_BACKGROUND));

    createListTableAreaContents(clComposite);

    return composite;
  }

  protected void createButtonsForButtonBar(Composite parent) {
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
    button.setData(Integer.valueOf(id));
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
    // buttons.put(Integer.valueOf(id), button);
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
    layoutData.grabExcessHorizontalSpace = false;
    layoutData.horizontalAlignment = GridData.FILL;
    int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
    Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
    layoutData.widthHint = Math.max(widthHint, minSize.x);
    button.setLayoutData(layoutData);
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
        // helpPressed();
        return;
      }
    }
  }

  /**
   * Create the sash with right control on the right. Note that this method
   * assumes GridData for the layout data of the rightControl.
   * 
   * @param composite
   * @param rightControl
   * @return Sash
   * 
   */
  protected Sash createSash(final Composite composite,
      final Control rightControl) {
    final Sash sash = new Sash(composite, SWT.VERTICAL);
    sash.setLayoutData(new GridData(GridData.FILL_VERTICAL));
    sash.setBackground(composite.getDisplay().getSystemColor(
        SWT.COLOR_LIST_BACKGROUND));
    // the following listener resizes the tree control based on sash deltas.
    // If necessary, it will also grow/shrink the dialog.
    sash.addListener(SWT.Selection, new Listener() {
      /*
       * (non-Javadoc)
       * 
       * @see
       * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets
       * .Event)
       */
      public void handleEvent(Event event) {
        if (event.detail == SWT.DRAG) {
          return;
        }
        int shift = event.x - sash.getBounds().x;
        GridData data = (GridData) rightControl.getLayoutData();
        int newWidthHint = data.widthHint + shift;
        if (newWidthHint < 20) {
          return;
        }
        Point computedSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Point currentSize = getShell().getSize();
        // if the dialog wasn't of a custom size we know we can shrink
        // it if necessary based on sash movement.
        boolean customSize = !computedSize.equals(currentSize);
        data.widthHint = newWidthHint;
        setLastTableWidth(newWidthHint);
        composite.layout(true);
        // recompute based on new widget size
        computedSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
        // if the dialog was of a custom size then increase it only if
        // necessary.
        if (customSize) {
          computedSize.x = Math.max(computedSize.x, currentSize.x);
        }
        computedSize.y = Math.max(computedSize.y, currentSize.y);
        if (computedSize.equals(currentSize)) {
          return;
        }
        setShellSize(computedSize.x, computedSize.y);
        lastShellSize = getShell().getSize();
      }
    });
    return sash;
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
   * Save the last known tree width.
   * 
   * @param width
   *          the width.
   */
  private void setLastTableWidth(int width) {
    lastTableWidth = width;
  }

  /**
   * @param parent
   *          the SWT parent for the tree area controls.
   * @return the new <code>Control</code>.
   * @since 3.0
   */
  protected Control createColumnsTableAreaContents(Composite parent) {
    // Build the tree an put it into the composite.
    columnsTableViewer = createColumnsTableViewer(parent);
    // tableViewer.setInput(getPreferenceManager());
    updateTableFont(JFaceResources.getDialogFont());
    layoutColumnsTableAreaControl(columnsTableViewer.getControl());
    return columnsTableViewer.getControl();
  }

  protected TableViewer createColumnsTableViewer(Composite parent) {
    Table innerTable = new Table(parent, SWT.CHECK | SWT.SINGLE
        | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
    innerTable.setHeaderVisible(false);
    innerTable.setLinesVisible(false);

    innerTable.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        if (event.detail == SWT.CHECK) {
          TableItem tableItem = (TableItem) event.item;
          if (((TableItem) event.item).getChecked()) {
            tableItem.setImage(BooksActivator.getImageDescriptor(
                "resource/icons/sort/a_z_sort.png").createImage());
          } else {
            tableItem.setImage(BooksActivator.getImageDescriptor(
                "resource/icons/sort/blank.png").createImage());
          }
        }
      }
    });

    TableViewer columnTableViewer = new TableViewer(innerTable);
    columnTableViewer.setUseHashlookup(true);

    // TableViewerFocusCellManager focusCellManager = new
    // TableViewerFocusCellManager(
    // columnTableViewer, new FocusBorderCellHighlighter(columnTableViewer));
    // ColumnViewerEditorActivationStrategy actSupport = new
    // ColumnViewerEditorActivationStrategy(
    // columnTableViewer) {
    // protected boolean isEditorActivationEvent(
    // ColumnViewerEditorActivationEvent event) {
    // return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
    // || event.eventType ==
    // ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
    // || (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED &&
    // event.keyCode == SWT.CR)
    // || event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
    // }
    // };
    //
    // TableViewerEditor.create(columnTableViewer, focusCellManager, actSupport,
    // ColumnViewerEditor.TABBING_HORIZONTAL
    // | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
    // | ColumnViewerEditor.TABBING_VERTICAL
    // | ColumnViewerEditor.KEYBOARD_ACTIVATION);

    TableViewerColumn tableViewerColumn = new TableViewerColumn(
        columnTableViewer, SWT.CENTER);
    tableViewerColumn.getColumn().setText("First");
    tableViewerColumn.getColumn().setWidth(150);
    tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public Image getImage(Object element) {
        return BooksActivator.getImageDescriptor(
            "resource/icons/sort/blank.png").createImage();
      }

      @Override
      public String getText(Object element) {
        return element.toString();
      }
    });

    TableColumnLayout tableColumnLayout = new TableColumnLayout();
    tableColumnLayout.setColumnData(tableViewerColumn.getColumn(),
        new ColumnWeightData(100));
    parent.setLayout(tableColumnLayout);

    columnTableViewer.setContentProvider(ArrayContentProvider.getInstance());
    columnTableViewer.setInput(new String[] { "a", "c", "b", "d", "e" });

    return columnTableViewer;
  }

  protected void updateTableFont(Font dialogFont) {
    getColumnsTableViewer().getControl().setFont(dialogFont);
  }

  public TableViewer getColumnsTableViewer() {
    return columnsTableViewer;
  }

  protected void layoutColumnsTableAreaControl(Control control) {
    GridData gd = new GridData(GridData.FILL_VERTICAL, GridData.FILL_HORIZONTAL);
    gd.widthHint = getLastRightWidth();
    gd.verticalSpan = 1;
    control.setLayoutData(gd);
  }

  /**
   * Get the last known right side width.
   * 
   * @return the width.
   */
  protected int getLastRightWidth() {
    return lastTableWidth;
  }

  protected Control createListTableAreaContents(Composite parent) {
    // Build the tree an put it into the composite.
    listTableViewer = createListTableViewer(parent);
    // tableViewer.setInput(getPreferenceManager());
    updateListTableFont(JFaceResources.getDialogFont());
    layoutListTableAreaControl(listTableViewer.getControl());
    return listTableViewer.getControl();
  }

  protected void updateListTableFont(Font dialogFont) {
    getListTableViewer().getControl().setFont(dialogFont);
  }

  public TableViewer getListTableViewer() {
    return listTableViewer;
  }

  protected void layoutListTableAreaControl(Control control) {
    GridData gd = new GridData(GridData.FILL_VERTICAL, GridData.FILL_HORIZONTAL);
    gd.widthHint = getLastRightWidth();
    gd.verticalSpan = 1;
    control.setLayoutData(gd);
  }

  protected TableViewer createListTableViewer(Composite parent) {
    Table innerTable = new Table(parent, SWT.SINGLE | SWT.FULL_SELECTION
        | SWT.V_SCROLL | SWT.H_SCROLL);
    innerTable.setHeaderVisible(false);
    innerTable.setLinesVisible(false);

    final TableViewer tableViewer = new TableViewer(innerTable);
    tableViewer.setUseHashlookup(true);

    TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer,
        SWT.LEFT);
    tableViewerColumn.getColumn().setText("Name");
    tableViewerColumn.getColumn().setWidth(150);
    tableViewerColumn.getColumn().setResizable(true);
    tableViewerColumn.getColumn().setMoveable(false);
    tableViewerColumn.setEditingSupport(new ListEditingSupport(tableViewer, 0));

    TableColumnLayout tableColumnLayout = new TableColumnLayout();
    tableColumnLayout.setColumnData(tableViewerColumn.getColumn(),
        new ColumnWeightData(100));
    parent.setLayout(tableColumnLayout);

    tableViewer.setLabelProvider(new ListColumnsLabelProvider());
    tableViewer.setContentProvider(new ListColumnsContentProvider(tableViewer));
    tableViewer.setInput(ColumnSetProvider.getInstance().getColumnSets());

    TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(
        tableViewer, new MyFocusCellHighlighter(tableViewer));
    TableViewerEditor.create(tableViewer, focusCellManager,
        new SecondClickColumnViewerEditorActivationStrategy(tableViewer),
        ColumnViewerEditor.DEFAULT | ColumnViewerEditor.KEYBOARD_ACTIVATION);

    return tableViewer;
  }
}
