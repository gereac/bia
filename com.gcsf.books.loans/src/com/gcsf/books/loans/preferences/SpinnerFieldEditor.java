package com.gcsf.books.loans.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import com.gcsf.books.loans.utilities.Messages;

public class SpinnerFieldEditor extends FieldEditor {

  private Composite top;

  private Spinner spinner;

  private Label label;

  private Label daysLabel;

  public SpinnerFieldEditor(String name, String labelText, Composite parent) {
    super(name, labelText, parent);
  }

  @Override
  protected void adjustForNumColumns(int numColumns) {
    ((GridData) top.getLayoutData()).horizontalSpan = numColumns;
  }

  @Override
  protected void doFillIntoGrid(Composite parent, int numColumns) {
    top = parent;

    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = numColumns;
    top.setLayoutData(gd);

    GridLayout groupLayout = new GridLayout();
    groupLayout.numColumns = 1;
    GridData groupData = new GridData();
    groupData.grabExcessHorizontalSpace = true;
    groupData.horizontalAlignment = SWT.FILL;
    Group group = new Group(top, SWT.NONE);
    group.setLayout(groupLayout);
    group.setLayoutData(groupData);
    group.setText(Messages.getString("SpinnerFieldEditor.loanPeriodField")); //$NON-NLS-1$

    GridLayout prefCompLayout = new GridLayout();
    prefCompLayout.numColumns = 3;
    GridData prefCompData = new GridData();
    prefCompData.grabExcessHorizontalSpace = true;
    prefCompData.horizontalAlignment = SWT.FILL;
    Composite prefComposite = new Composite(group, SWT.NONE);
    prefComposite.setLayout(prefCompLayout);
    prefComposite.setLayoutData(prefCompData);

    GridData labelData = new GridData();
    labelData.horizontalAlignment = SWT.LEFT;
    label = new Label(prefComposite, SWT.NONE);
    label.setText(Messages.getString("SpinnerFieldEditor.defaultLoanPeriod")); //$NON-NLS-1$
    label.setLayoutData(labelData);

    GridData spinnerData = new GridData();
    spinnerData.horizontalAlignment = SWT.CENTER;
    spinner = new Spinner(prefComposite, SWT.BORDER);
    spinner.setMinimum(1);
    spinner.setMaximum(120);
    spinner.setIncrement(1);
    spinner.setPageIncrement(1);
    spinner.setLayoutData(spinnerData);
    spinner.pack();

    GridData labelDays = new GridData();
    labelDays.horizontalAlignment = SWT.RIGHT;
    daysLabel = new Label(prefComposite, SWT.NONE);
    daysLabel.setText(Messages.getString("SpinnerFieldEditor.days")); //$NON-NLS-1$
    daysLabel.setLayoutData(labelDays);
  }

  @Override
  protected void doLoad() {
    int value = getPreferenceStore().getInt(getPreferenceName());
    setSpinner(value);
  }

  private void setSpinner(Integer value) {
    spinner.setSelection(value);
  }

  @Override
  protected void doLoadDefault() {
    setSpinner(ILoanPreferenceConstants.LOAN_PERIOD);
  }

  @Override
  protected void doStore() {
    int value = spinner.getSelection();
    getPreferenceStore().setValue(getPreferenceName(), value);
  }

  @Override
  public int getNumberOfControls() {
    return 3;
  }

}
