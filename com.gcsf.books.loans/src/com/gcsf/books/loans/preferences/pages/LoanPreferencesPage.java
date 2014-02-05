package com.gcsf.books.loans.preferences.pages;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.gcsf.books.loans.LoanActivator;
import com.gcsf.books.loans.preferences.ILoanPreferenceConstants;
import com.gcsf.books.loans.preferences.SpinnerFieldEditor;
import com.gcsf.books.loans.utilities.Messages;

public class LoanPreferencesPage extends FieldEditorPreferencePage implements
    IWorkbenchPreferencePage {

  public LoanPreferencesPage() {
    super(GRID);
    setPreferenceStore(LoanActivator.getDefault().getPreferenceStore());
  }

  @Override
  public void init(IWorkbench aWorkbench) {
    noDefaultAndApplyButton();
  }

  @Override
  protected void createFieldEditors() {
    addField(new SpinnerFieldEditor(ILoanPreferenceConstants.P_LOAN_PERIOD,
        Messages.getString("LoanPreferencesPage.loanPeriod"), //$NON-NLS-1$
        getFieldEditorParent()));
  }

}
