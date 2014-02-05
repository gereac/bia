package com.gcsf.books.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.gcsf.books.gui.dialogs.BaseColumnsSelectionDialog;

public class ColumnSetChooserHandler extends AbstractHandler implements
    IHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil
        .getActiveWorkbenchWindow(event);

    final Shell shell;
    if (activeWorkbenchWindow == null) {
      shell = null;
    } else {
      shell = activeWorkbenchWindow.getShell();
    }

    BaseColumnsSelectionDialog dialog = new BaseColumnsSelectionDialog(shell);
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
