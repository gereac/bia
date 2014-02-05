package com.gcsf.books;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.gcsf.books.preferences.IPreferenceConstants;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

  public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
      IWorkbenchWindowConfigurer configurer) {
    return new ApplicationWorkbenchWindowAdvisor(configurer);
  }

  public String getInitialWindowPerspectiveId() {
    return BooksActivator.getDefault().getPreferenceStore().getString(
        IPreferenceConstants.P_CURRENT_PERSPECTIVE_ID);
    // return Utils.DEFAULT_PERSPECTIVE_ID;
  }

  // @Override
  // public String getMainPreferencePageId() {
  // return "com.gcsf.books.preferences.imagesPreferencePages";
  // }
  // see method description in parent class
  // for more refinenement on this see
  // http://www.eclipse.org/forums/index.php?t=msg&th=160434&start=0&
  // that says "override the getComparatorFor() method"

  @Override
  public void initialize(IWorkbenchConfigurer configurer) {
    // saves the window position and size
    configurer.setSaveAndRestore(true);
  }

}
