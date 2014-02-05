package com.gcsf.books.preferences.pages;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.gcsf.books.BooksActivator;
import com.gcsf.books.preferences.IPreferenceConstants;
import com.gcsf.books.utilities.Messages;

public class GeneralPreferencesPage extends PreferencePage implements
    IWorkbenchPreferencePage {

  private RadioGroupFieldEditor doubleClick;

  private BooleanFieldEditor checkCounter;

  private BooleanFieldEditor checkCollectionStatus;

  private BooleanFieldEditor checkOne;

  private BooleanFieldEditor checkTwo;

  private FontFieldEditor fontSelector;

  public GeneralPreferencesPage() {
    super();
    setPreferenceStore(BooksActivator.getDefault().getPreferenceStore());
  }

  @Override
  protected Control createContents(Composite parent) {
    Composite container = new Composite(parent, SWT.NULL);

    GridLayout layout = new GridLayout();
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    container.setLayout(layout);
    GridData grid = new GridData();
    grid.grabExcessHorizontalSpace = true;
    grid.horizontalAlignment = SWT.FILL;
    container.setLayoutData(grid);

    GridLayout layoutAsk = new GridLayout();
    layoutAsk.marginWidth = 0;
    layoutAsk.marginHeight = 0;
    GridData gridAsk = new GridData();
    gridAsk.horizontalAlignment = SWT.FILL;
    gridAsk.grabExcessHorizontalSpace = true;

    Group askForConfirmationGroup = new Group(container, SWT.NONE);
    askForConfirmationGroup.setText(Messages
        .getString("GeneralPreferencesPage.askForConfirmationWhen")); //$NON-NLS-1$
    askForConfirmationGroup.setLayoutData(gridAsk);
    askForConfirmationGroup.setLayout(layoutAsk);

    checkOne = new BooleanFieldEditor(
        IPreferenceConstants.P_CHECK_ONE,
        Messages
            .getString("GeneralPreferencesPage.confirmationWhenRemovingItems"), askForConfirmationGroup); //$NON-NLS-1$
    checkOne.setPage(this);
    checkOne.setPreferenceStore(getPreferenceStore());
    checkOne.load();

    checkTwo = new BooleanFieldEditor(
        IPreferenceConstants.P_CHECK_TWO,
        Messages
            .getString("GeneralPreferencesPage.confirmationWhenCancellingDataEntryScreens"), askForConfirmationGroup); //$NON-NLS-1$
    checkTwo.setPage(this);
    checkTwo.setPreferenceStore(getPreferenceStore());
    checkTwo.load();

    doubleClick = new RadioGroupFieldEditor(
        IPreferenceConstants.P_DOUBLE_CLICK,
        Messages
            .getString("GeneralPreferencesPage.whenDoubleClickingABookInTheList"), 1, //$NON-NLS-1$
        new String[][] {
            {
                Messages.getString("GeneralPreferencesPage.editIt"), IPreferenceConstants.P_DB_EDIT_IT }, //$NON-NLS-1$
            { Messages.getString("GeneralPreferencesPage.showItsStories"), //$NON-NLS-1$
                IPreferenceConstants.P_DB_SHOW_STORIES } }, container, true);
    doubleClick.setPage(this);
    doubleClick.setPreferenceStore(getPreferenceStore());
    doubleClick.load();

    GridLayout layoutFont = new GridLayout();
    layoutAsk.marginWidth = 0;
    layoutAsk.marginHeight = 0;
    GridData layoutDataFont = new GridData(GridData.FILL_HORIZONTAL);

    Group fontGroup = new Group(container, SWT.NONE);
    fontGroup.setText("Font ...");
    fontGroup.setLayout(layoutFont);
    fontGroup.setLayoutData(layoutDataFont);

    fontSelector = new FontFieldEditor(IPreferenceConstants.P_FONT_SELECTOR,
        "Font", fontGroup);
    fontSelector.setPage(this);
    fontSelector.setPreferenceStore(getPreferenceStore());
    fontSelector.load();

    checkCollectionStatus = new BooleanFieldEditor(
        IPreferenceConstants.P_CHECK_COLLECTION_STATUS,
        Messages
            .getString("GeneralPreferencesPage.showCollectionStatusInTreeFolders"), container); //$NON-NLS-1$
    checkCollectionStatus.setPage(this);
    checkCollectionStatus.setPreferenceStore(getPreferenceStore());
    checkCollectionStatus.load();

    checkCounter = new BooleanFieldEditor(
        IPreferenceConstants.P_CHECK_COUNTER,
        Messages.getString("GeneralPreferencesPage.showCounterInTreeFolders"), container); //$NON-NLS-1$
    checkCounter.setPage(this);
    checkCounter.setPreferenceStore(getPreferenceStore());
    checkCounter.load();

    return container;

  }

  @Override
  public void init(IWorkbench workbench) {
    noDefaultAndApplyButton();
  }

  /*
   * The user has pressed Ok or Apply. Store/apply this page's values
   * appropriately.
   */
  public boolean performOk() {
    checkOne.store();
    checkTwo.store();
    doubleClick.store();
    checkCounter.store();
    checkCollectionStatus.store();
    fontSelector.store();
    return super.performOk();
  }

  protected void performDefaults() {
    checkOne.loadDefault();
    checkTwo.loadDefault();
    doubleClick.loadDefault();
    checkCounter.loadDefault();
    checkCollectionStatus.loadDefault();
    fontSelector.loadDefault();
    super.performDefaults();
  }

}
