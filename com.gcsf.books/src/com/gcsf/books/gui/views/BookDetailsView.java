package com.gcsf.books.gui.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import com.gcsf.books.model.FolderItem;
import com.gcsf.books.model.Item;
import com.gcsf.books.utilities.Utils;

public class BookDetailsView extends ViewPart implements ISelectionListener {

  public static final String ID = "com.gcsf.books.view.bookDetails";

  private Label headerLabel;

  private FormToolkit toolkit;

  private ScrolledForm form;

  // private Text titleText;

  // private Text authorText;

  private Label idElementLabel;

  private Label descirption1ElementLabel;

  private Label descirption2ElementLabel;

  public BookDetailsView() {
    // TODO Auto-generated constructor stub
  }

  /**
   * This is a callback that will allow us to create the viewer and initialize
   * it.
   */
  public void createPartControl(Composite parent) {
    final Composite comp = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout(1, false);
    layout.horizontalSpacing = 0;
    layout.verticalSpacing = 0;
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    comp.setLayout(layout);

    this.toolkit = new FormToolkit(Utils.FORM_COLOR(comp.getDisplay()));
    this.form = this.toolkit.createScrolledForm(comp);
    this.form.getBody().setLayout(layout);
    GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
    this.form.setLayoutData(gd);

    final Composite headerComp = new Composite(this.form.getBody(), SWT.NONE);
    layout = new GridLayout(1, false);

    headerComp.setLayout(layout);
    gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
    headerComp.setLayoutData(gd);

    this.headerLabel = new Label(headerComp, SWT.NONE);
    this.headerLabel.setForeground(Utils.ORANGE_COLOR);
    this.headerLabel.setFont(Utils.SUBHEADER_FONT);
    this.headerLabel.setText("Details");
    gd = new GridData(SWT.FILL, SWT.FILL, true, true);

    this.headerLabel.setLayoutData(gd);

    final Composite client = this.toolkit.createComposite(this.form.getBody(),
        SWT.WRAP);
    layout = new GridLayout(2, false);
    client.setLayout(layout);
    this.toolkit.paintBordersFor(client);
    gd = new GridData(SWT.FILL, SWT.FILL, true, true);
    client.setLayoutData(gd);

    final Label idLabel = this.toolkit.createLabel(client, "Id");
    gd = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
    gd.horizontalIndent = 20;
    gd.widthHint = 60;
    idLabel.setLayoutData(gd);

    this.idElementLabel = this.toolkit.createLabel(client, "");
    gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
    gd.horizontalIndent = 20;
    this.idElementLabel.setLayoutData(gd);

    final Label descirptionLabel1 = this.toolkit.createLabel(client,
        "Details 1");
    gd = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
    gd.horizontalIndent = 20;
    gd.widthHint = 60;
    descirptionLabel1.setLayoutData(gd);

    this.descirption1ElementLabel = this.toolkit.createLabel(client, "");
    gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
    gd.horizontalIndent = 20;
    this.descirption1ElementLabel.setLayoutData(gd);

    final Label descirptionLabel2 = this.toolkit.createLabel(client,
        "Details 2");
    gd = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
    gd.horizontalIndent = 20;
    gd.widthHint = 60;
    descirptionLabel2.setLayoutData(gd);

    this.descirption2ElementLabel = this.toolkit.createLabel(client, "");
    gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
    gd.horizontalIndent = 20;
    this.descirption2ElementLabel.setLayoutData(gd);

    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService()
        .addSelectionListener(this);

    this.form.reflow(true);
  }

  public void selectionChanged(final IWorkbenchPart part,
      final ISelection selection) {
    if (!selection.isEmpty()) {
      if ((((IStructuredSelection) selection).getFirstElement()) instanceof FolderItem) {
        this.idElementLabel.setText(((Item) ((IStructuredSelection) selection)
            .getFirstElement()).getId());
        this.descirption1ElementLabel
            .setText(((Item) ((IStructuredSelection) selection)
                .getFirstElement()).getDetail1());
        this.descirption2ElementLabel
            .setText(((Item) ((IStructuredSelection) selection)
                .getFirstElement()).getDetail2());
      }
    } else {
      this.idElementLabel.setText(""); //$NON-NLS-1$
      this.descirption1ElementLabel.setText(""); //$NON-NLS-1$
      this.descirption2ElementLabel.setText(""); //$NON-NLS-1$
    }

  }

  /**
   * Passing the focus request to the viewer's control.
   */
  public void setFocus() {
    // viewer.getControl().setFocus();
  }

}
