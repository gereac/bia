package com.gcsf.books.search.handlers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

public class SearchWorkbenchWindowControlContribution extends
    WorkbenchWindowControlContribution {

  @Override
  protected Control createControl(Composite parent) {
    Composite searchComposite = new Composite(parent, SWT.NONE);
    GridLayout gridLayout = new GridLayout(1, false);
    gridLayout.marginWidth = 0;
    gridLayout.marginHeight = 0;
    gridLayout.verticalSpacing = 0;
    gridLayout.horizontalSpacing = 0;
    searchComposite.setLayout(gridLayout);

    GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
    gridData.widthHint = SWT.DEFAULT;
    gridData.heightHint = SWT.DEFAULT;
    gridData.verticalAlignment = SWT.BOTTOM;
    searchComposite.setLayoutData(gridData);

    Text textSearchInput = new Text(searchComposite, SWT.SINGLE | SWT.BORDER
        | SWT.SEARCH | SWT.CANCEL);
    textSearchInput.setText("");
    textSearchInput
        .setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
    textSearchInput.addListener(SWT.DefaultSelection, new Listener() {
      public void handleEvent(Event e) {
        // System.out.println(e.widget + " - Default Selection");
        // TODO add the real implementation here
      }
    });

    return searchComposite;
  }

}
