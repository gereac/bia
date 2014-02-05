package com.gcsf.books.search.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.gcsf.books.search.SearchActivator;

public class SearchPreferenceInitializer extends AbstractPreferenceInitializer {

  @Override
  public void initializeDefaultPreferences() {
    IPreferenceStore store = SearchActivator.getDefault().getPreferenceStore();
    store.setDefault(ISearchPreferencesConstants.P_STATIC_FILTER, "All");
  }

}
