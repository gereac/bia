package com.gcsf.books.handlers.preference;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.gcsf.books.preferences.dialogs.MyPreferencesDialog;

/**
 * This is the modified handling of ShowPreferencePageHandler. This will create
 * a tab for each preference page
 */
public class MyShowPreferencePageHandler2 extends AbstractHandler implements
    IHandler {

  @Override
  public void dispose() {
    // TODO Auto-generated method stub

  }

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Shell aShell = HandlerUtil.getActiveShell(event);
    MyPreferencesDialog dialog = new MyPreferencesDialog(aShell);
    if (dialog != null) {
      dialog.open();
    }
    return null;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean isHandled() {
    return true;
  }

}
