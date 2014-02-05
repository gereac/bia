package com.gcsf.books.handlers.view;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import com.gcsf.books.utilities.Utils;

public class IncreaseFontHandler extends AbstractHandler implements IHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Utils.zoomText(true, false);
    return null;
  }
}
