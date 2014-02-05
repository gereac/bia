package com.gcsf.books.handlers.help;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.keys.IBindingService;

public class ShowKeyBindingsHandler extends AbstractHandler implements IHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IWorkbench workbench = PlatformUI.getWorkbench();
    IBindingService bindingService = (IBindingService) workbench
        .getService(IBindingService.class);
    bindingService.openKeyAssistDialog();
    return null;
  }

}
