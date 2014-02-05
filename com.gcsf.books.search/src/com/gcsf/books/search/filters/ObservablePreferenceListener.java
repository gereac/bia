package com.gcsf.books.search.filters;

import java.util.Observable;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.gcsf.books.search.SearchActivator;
import com.gcsf.books.search.preferences.ISearchPreferencesConstants;

public class ObservablePreferenceListener extends Observable {

  IPropertyChangeListener preferenceListener = new IPropertyChangeListener() {
    /*
     * @see IPropertyChangeListener.propertyChange()
     */
    public void propertyChange(PropertyChangeEvent event) {
      if (event.getProperty().equals(
          ISearchPreferencesConstants.P_STATIC_FILTER)) {
        setChanged();
        notifyObservers();
      }
    }
  };

  public ObservablePreferenceListener() {
    super();
    SearchActivator.getDefault().getPreferenceStore()
        .addPropertyChangeListener(preferenceListener);
  }

}
