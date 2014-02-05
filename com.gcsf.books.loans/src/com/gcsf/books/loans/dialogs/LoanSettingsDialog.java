package com.gcsf.books.loans.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import com.gcsf.books.loans.LoanActivator;
import com.gcsf.books.loans.preferences.ILoanPreferenceConstants;

public class LoanSettingsDialog extends Dialog {

  private String SHELL_TITLE = "Loan settings";

  private Point lastShellSize;

  // TODO fix the layout of the dialog ... it is ugly now
  // TODO test the listeners of OK/Cancel buttons

  public LoanSettingsDialog(Shell parentShell) {
    super(parentShell);
  }

  private Spinner spinner;

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
  protected Control createContents(final Composite parent) {
    final Control[] control = new Control[1];
    BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
      public void run() {
        control[0] = LoanSettingsDialog.super.createContents(parent);
        GridLayout parentLayout = (GridLayout) ((Composite) control[0])
            .getLayout();
        parentLayout.numColumns = 2;
        parentLayout.makeColumnsEqualWidth = false;
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

    Composite leftComposite = new Composite(composite, SWT.LEFT);
    GridLayout leftGL = new GridLayout();
    leftGL.numColumns = 2;
    leftComposite.setLayout(leftGL);
    GridData leftData = new GridData();
    leftData.verticalAlignment = SWT.CENTER;
    leftComposite.setLayoutData(leftData);

    Label label = new Label(leftComposite, SWT.NULL);
    label.setText("Default loan period");
    GridData labelData = new GridData();
    labelData.horizontalSpan = 2;
    label.setLayoutData(labelData);

    spinner = new Spinner(leftComposite, SWT.NULL);
    spinner.setMinimum(7);
    spinner.setMaximum(100);
    spinner.setSelection(LoanActivator.getDefault().getPreferenceStore()
        .getInt(ILoanPreferenceConstants.P_LOAN_PERIOD));
    spinner.setIncrement(1);
    spinner.setPageIncrement(1);

    Label daysLabel = new Label(leftComposite, SWT.NULL);
    daysLabel.setText(" days");

    return composite;
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
    }
  }

  @Override
  protected void okPressed() {
    LoanActivator.getDefault().getPreferenceStore().setValue(
        ILoanPreferenceConstants.P_LOAN_PERIOD, spinner.getSelection());
    setReturnCode(OK);
    close();
  }

  @Override
  protected boolean isResizable() {
    return false;
  }

}