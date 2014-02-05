package com.gcsf.books.gui.views;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.gcsf.books.BooksActivator;
import com.gcsf.books.gui.model.imagesview.ImagesViewContentProvider;
import com.gcsf.books.gui.model.imagesview.ImagesViewLabelProvider;
import com.gcsf.books.preferences.IPreferenceConstants;

public class ImagesView extends ViewPart {

  public static final String ID = "com.gcsf.books.view.stack.images";

  private Composite myParent = null;

  public ImagesView() {
    // TODO Auto-generated constructor stub
  }

  private TableViewer viewer;

  /**
   * This is a callback that will allow us to create the viewer and initialize
   * it.
   */
  public void createPartControl(Composite parent) {

    // Composite compoundComposite = new Composite(parent, SWT.NONE);
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

    Composite tableViewerComposite = new Composite(parent, SWT.BOTTOM
        | SWT.BORDER);
    GridLayout gridLayout2 = new GridLayout(1, false);
    gridLayout2.marginWidth = 0;
    gridLayout2.marginHeight = 0;
    gridLayout2.verticalSpacing = 0;
    gridLayout2.horizontalSpacing = 0;
    tableViewerComposite.setLayout(gridLayout2);
    GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData2.widthHint = SWT.DEFAULT;
    gridData2.heightHint = SWT.DEFAULT;
    tableViewerComposite.setLayoutData(gridData2);

    createTableViewerControl(tableViewerComposite);
  }

  private void createToolBarControl(Composite parent) {
    final int scaleIncrement = 2;

    Composite panel = new Composite(parent, SWT.NONE);
    // panel.setLayout(new RowLayout());
    GridLayout gridLayout = new GridLayout(6, false);
    gridLayout.marginWidth = 0;
    gridLayout.marginHeight = 0;
    gridLayout.verticalSpacing = 0;
    gridLayout.horizontalSpacing = 5;
    panel.setLayout(gridLayout);
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    gridData.widthHint = SWT.DEFAULT;
    gridData.heightHint = SWT.DEFAULT;
    panel.setLayoutData(gridData);

    // final Button smallThumb = new Button(panel, SWT.PUSH);
    // smallThumb.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(
    // ISharedImages.IMG_TOOL_BACK));
    // GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, false, false);
    // gridData2.widthHint = SWT.DEFAULT;
    // gridData2.heightHint = SWT.DEFAULT;
    // smallThumb.setLayoutData(gridData2);

    // final Scale scale = new Scale(panel, SWT.NONE | SWT.BORDER);
    // scale.setMinimum(0);
    // scale.setMaximum(20);
    // scale.setIncrement(scaleIncrement);
    // scale.setPageIncrement(scaleIncrement);
    // scale.setToolTipText("Thumbnail size");
    // GridData gridData3 = new GridData(SWT.FILL, SWT.FILL, false, false);
    // gridData3.widthHint = SWT.DEFAULT;
    // gridData3.heightHint = SWT.DEFAULT;
    // scale.setLayoutData(gridData3);

    Slider slider = new Slider(panel, SWT.NONE);
    slider.setMinimum(0);
    slider.setMaximum(20);
    slider.setIncrement(scaleIncrement);
    slider.setPageIncrement(scaleIncrement);
    slider.setToolTipText("Thumbnail size");
    slider.setThumb(1);
    GridData gridData4 = new GridData(SWT.FILL, SWT.FILL, false, false);
    gridData4.widthHint = 300;
    // gridData4.heightHint = PlatformUI.getWorkbench().getSharedImages()
    // .getImage(ISharedImages.IMG_ETOOL_PRINT_EDIT).getBounds().height;
    gridData4.heightHint = SWT.DEFAULT;
    slider.setLayoutData(gridData4);

    // final Button bigThumb = new Button(panel, SWT.PUSH);
    // bigThumb.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(
    // ISharedImages.IMG_TOOL_FORWARD));
    // GridData gridData5 = new GridData(SWT.FILL, SWT.FILL, false, false);
    // gridData5.widthHint = SWT.DEFAULT;
    // gridData5.heightHint = SWT.DEFAULT;
    // bigThumb.setLayoutData(gridData5);

    Label separator = new Label(panel, SWT.SEPARATOR | SWT.VERTICAL);
    separator.setText("");
    GridData gridData6 = new GridData(SWT.FILL, SWT.FILL, false, false);
    gridData6.widthHint = SWT.DEFAULT;
    gridData6.heightHint = 8;
    separator.setLayoutData(gridData6);

    Button button = new Button(panel, SWT.PUSH);
    button.setImage(BooksActivator.getImageDescriptor(
        "resource/icons/toolbar/debugt_obj.gif").createImage());
    button.setToolTipText("Regenerate thumbnails");
    GridData gridData7 = new GridData(SWT.FILL, SWT.FILL, false, false);
    gridData7.widthHint = SWT.DEFAULT;
    gridData7.heightHint = 8;
    button.setLayoutData(gridData7);

    // if (scale.getSelection() == scale.getMaximum()) {
    // bigThumb.setEnabled(false);
    // }
    // if (scale.getSelection() == scale.getMinimum()) {
    // smallThumb.setEnabled(false);
    // }

    // smallThumb.addSelectionListener(new SelectionListener() {
    //
    // @Override
    // public void widgetSelected(SelectionEvent e) {
    // int crtScaleSelection = scale.getSelection();
    // scale.setSelection(crtScaleSelection - scaleIncrement);
    // if (scale.getMinimum() == (crtScaleSelection - scaleIncrement)) {
    // smallThumb.setEnabled(false);
    // }
    // if (crtScaleSelection == scale.getMaximum()) {
    // bigThumb.setEnabled(true);
    // }
    // }
    //
    // @Override
    // public void widgetDefaultSelected(SelectionEvent e) {
    // // nothing to do
    // }
    // });

    // bigThumb.addSelectionListener(new SelectionListener() {
    //
    // @Override
    // public void widgetSelected(SelectionEvent e) {
    // int crtScaleSelection = scale.getSelection();
    // scale.setSelection(crtScaleSelection + scaleIncrement);
    // if (scale.getMaximum() == (crtScaleSelection + scaleIncrement)) {
    // bigThumb.setEnabled(false);
    // }
    // if (crtScaleSelection == scale.getMinimum()) {
    // smallThumb.setEnabled(true);
    // }
    // }
    //
    // @Override
    // public void widgetDefaultSelected(SelectionEvent e) {
    // // nothing to do
    // }
    // });

    // scale.addListener(SWT.Selection, new Listener() {
    // public void handleEvent(Event event) {
    // // int perspectiveValue = scale.getMaximum() - scale.getSelection()
    // // + scale.getMinimum();
    // int perspectiveValue = scale.getSelection();
    // if (perspectiveValue == scale.getMinimum()) {
    // smallThumb.setEnabled(false);
    // } else {
    // smallThumb.setEnabled(true);
    // }
    // if (perspectiveValue == scale.getMaximum()) {
    // bigThumb.setEnabled(false);
    // } else {
    // bigThumb.setEnabled(true);
    // }
    // }
    // });
  }

  /**
   * Passing the focus request to the viewer's control.
   */
  public void setFocus() {
    viewer.getControl().setFocus();
  }

  public void createToolbar() {
    IActionBars aBars = getViewSite().getActionBars();
    IToolBarManager tbMgr = aBars.getToolBarManager();

    // Add combo to change graph type
    IContributionItem scaleItem = new ControlContribution("type") {
      protected Control createControl(Composite parent) {

        final int scaleIncrement = 2;

        Composite panel = new Composite(parent, SWT.NONE | SWT.BORDER);
        panel.setLayout(new RowLayout());
        final Button smallThumb = new Button(panel, SWT.PUSH);
        smallThumb.setImage(PlatformUI.getWorkbench().getSharedImages()
            .getImage(ISharedImages.IMG_TOOL_BACK));
        final Scale scale = new Scale(panel, SWT.NONE);
        scale.setMinimum(0);
        scale.setMaximum(20);
        scale.setIncrement(scaleIncrement);
        scale.setPageIncrement(scaleIncrement);
        scale.setToolTipText("Thumbnail size");

        final Button bigThumb = new Button(panel, SWT.PUSH);
        bigThumb.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(
            ISharedImages.IMG_TOOL_FORWARD));
        Label separator = new Label(panel, SWT.SEPARATOR | SWT.VERTICAL);
        separator.setText("");
        Button button = new Button(panel, SWT.PUSH);
        button.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(
            ISharedImages.IMG_ETOOL_PRINT_EDIT));
        button.setToolTipText("Regenerate thumbnails");

        if (scale.getSelection() == scale.getMaximum()) {
          bigThumb.setEnabled(false);
        }
        if (scale.getSelection() == scale.getMinimum()) {
          smallThumb.setEnabled(false);
        }

        smallThumb.addSelectionListener(new SelectionListener() {

          @Override
          public void widgetSelected(SelectionEvent e) {
            int crtScaleSelection = scale.getSelection();
            scale.setSelection(crtScaleSelection - scaleIncrement);
            if (scale.getMinimum() == (crtScaleSelection - scaleIncrement)) {
              smallThumb.setEnabled(false);
            }
            if (crtScaleSelection == scale.getMaximum()) {
              bigThumb.setEnabled(true);
            }
          }

          @Override
          public void widgetDefaultSelected(SelectionEvent e) {
            // nothing to do
          }
        });

        bigThumb.addSelectionListener(new SelectionListener() {

          @Override
          public void widgetSelected(SelectionEvent e) {
            int crtScaleSelection = scale.getSelection();
            scale.setSelection(crtScaleSelection + scaleIncrement);
            if (scale.getMaximum() == (crtScaleSelection + scaleIncrement)) {
              bigThumb.setEnabled(false);
            }
            if (crtScaleSelection == scale.getMinimum()) {
              smallThumb.setEnabled(true);
            }
          }

          @Override
          public void widgetDefaultSelected(SelectionEvent e) {
            // nothing to do
          }
        });

        scale.addListener(SWT.Selection, new Listener() {
          public void handleEvent(Event event) {
            // int perspectiveValue = scale.getMaximum() - scale.getSelection()
            // + scale.getMinimum();
            int perspectiveValue = scale.getSelection();
            if (perspectiveValue == scale.getMinimum()) {
              smallThumb.setEnabled(false);
            } else {
              smallThumb.setEnabled(true);
            }
            if (perspectiveValue == scale.getMaximum()) {
              bigThumb.setEnabled(false);
            } else {
              bigThumb.setEnabled(true);
            }
          }
        });

        return panel;
      }
    };

    tbMgr.add(scaleItem);
    tbMgr.update(true);
    aBars.updateActionBars();

  }

  private void createTableViewerControl(Composite parent) {

    GridLayout parentLayout = new GridLayout();
    parentLayout.marginWidth = 0;
    parentLayout.marginHeight = 0;
    parentLayout.verticalSpacing = 0;
    parentLayout.horizontalSpacing = 0;
    parent.setLayout(parentLayout);
    GridData parentData = new GridData(SWT.FILL, SWT.FILL, true, true);
    parent.setLayoutData(parentData);

    myParent = parent;
    viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    viewer.setContentProvider(new ImagesViewContentProvider());
    viewer.setLabelProvider(new ImagesViewLabelProvider());
    viewer.setInput(getViewSite());
    GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    gridData.widthHint = SWT.DEFAULT;
    gridData.heightHint = SWT.DEFAULT;
    viewer.getTable().setLayoutData(gridData);

    hookDoubleClickCommand();

    getViewSite().setSelectionProvider(viewer);
  }

  private void hookDoubleClickCommand() {
    viewer.addDoubleClickListener(new IDoubleClickListener() {
      public void doubleClick(DoubleClickEvent event) {
        if (BooksActivator.getDefault().getPreferenceStore().getString(
            IPreferenceConstants.P_DOUBLE_CLICK).equals(
            IPreferenceConstants.P_DB_EDIT_IT)) {
          MessageDialog.openInformation(myParent.getShell(), "Info",
              "Edit the book");
        } else {
          MessageDialog.openInformation(myParent.getShell(), "Info",
              "Show the book stories");
        }
        // TODO add the call to the appropriate commands
      }
    });
  }

}
