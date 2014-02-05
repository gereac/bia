package com.gcsf.books.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.gcsf.books.BooksActivator;
import com.gcsf.books.utilities.Utils;

public class BooksPreferencesInitializer extends AbstractPreferenceInitializer {

  /*
   * (non-Javadoc)
   * 
   * @seeorg.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
   * initializeDefaultPreferences()
   */
  public void initializeDefaultPreferences() {
    IPreferenceStore store = BooksActivator.getDefault().getPreferenceStore();
    store.setDefault(IPreferenceConstants.P_USE_AUTH, true);
    store.setDefault(IPreferenceConstants.P_CHECK_ONE, true);
    store.setDefault(IPreferenceConstants.P_CHECK_TWO, true);
    store.setDefault(IPreferenceConstants.P_DOUBLE_CLICK,
        IPreferenceConstants.P_DB_EDIT_IT);
    store.setDefault(IPreferenceConstants.P_BOOK_DISPLAY_MODE, true);
    store.setDefault(IPreferenceConstants.P_CHECK_COLLECTION_STATUS, true);
    store.setDefault(IPreferenceConstants.P_CURRENT_PERSPECTIVE_ID,
        Utils.DEFAULT_PERSPECTIVE_ID);
  }

}
