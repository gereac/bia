package com.gcsf.books.loans.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.gcsf.books.loans.LoanActivator;

public class LoansPreferencesInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = LoanActivator.getDefault().getPreferenceStore();
		store.setDefault(ILoanPreferenceConstants.P_LOAN_PERIOD, 21);
	}

}
