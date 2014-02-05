package com.gcsf.books.preferences.pages;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.gcsf.books.BooksActivator;
import com.gcsf.books.preferences.IPreferenceConstants;
import com.gcsf.books.utilities.Messages;

public class ImagesPreferencePage extends FieldEditorPreferencePage implements
    IWorkbenchPreferencePage {

  public ImagesPreferencePage() {
    super(GRID);
    setPreferenceStore(BooksActivator.getDefault().getPreferenceStore());
    setDescription(Messages.getString("ImagesPreferencePage.imagesPreferences")); //$NON-NLS-1$
  }

  /**
   * Creates the field editors. Field editors are abstractions of the common GUI
   * blocks needed to manipulate various types of preferences. Each field editor
   * knows how to save and restore itself.
   */
  public void createFieldEditors() {
    addField(new DirectoryFieldEditor(
        IPreferenceConstants.P_PATH_IMAGES,
        Messages.getString("ImagesPreferencePage.defaultFolderForImageFiles"), getFieldEditorParent())); //$NON-NLS-1$
    addField(new DirectoryFieldEditor(
        IPreferenceConstants.P_PATH_THUMBNAILS,
        Messages
            .getString("ImagesPreferencePage.defaultFolderForThumbnailFiles"), getFieldEditorParent())); //$NON-NLS-1$
  }

  @Override
  public void init(IWorkbench workbench) {
    noDefaultAndApplyButton();
  }

}
