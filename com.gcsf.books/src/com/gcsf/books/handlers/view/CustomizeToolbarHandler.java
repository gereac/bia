package com.gcsf.books.handlers.view;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

public class CustomizeToolbarHandler extends AbstractHandler implements
    IHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    // nothing to do yet ... maybe in a future version
    return null;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

  @Override
  public boolean isHandled() {
    return false;
  }

}
