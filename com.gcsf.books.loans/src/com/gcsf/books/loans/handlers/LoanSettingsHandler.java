package com.gcsf.books.loans.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.gcsf.books.loans.dialogs.LoanSettingsDialog;

public class LoanSettingsHandler extends AbstractHandler implements IHandler {

  @Override
  public void dispose() {
    // TODO Auto-generated method stub
  }

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Shell aShell = HandlerUtil.getActiveShell(event);
    LoanSettingsDialog dialog = new LoanSettingsDialog(aShell);
    if (null != dialog) {
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
