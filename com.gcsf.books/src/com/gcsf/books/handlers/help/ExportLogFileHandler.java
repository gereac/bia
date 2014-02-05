package com.gcsf.books.handlers.help;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import com.gcsf.books.BooksActivator;
import com.gcsf.books.engine.util.CoreUtils;
import com.gcsf.books.engine.util.StringUtils;

public class ExportLogFileHandler extends AbstractHandler implements IHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    FileDialog dialog = new FileDialog(PlatformUI.getWorkbench()
        .getActiveWorkbenchWindow().getShell(), SWT.SAVE);
    dialog.setText("Export Log file dialog");
    dialog.setFilterExtensions(new String[] { "*.log" }); //$NON-NLS-1$
    dialog.setFileName("bia_problems.log"); //$NON-NLS-1$
    dialog.setOverwrite(true);

    String file = dialog.open();
    if (StringUtils.isSet(file)) {
      try {
        File logFile = Platform.getLogFileLocation().toFile();
        InputStream inS;
        if (logFile.exists())
          inS = new FileInputStream(logFile);
        else
          inS = new ByteArrayInputStream(new byte[0]);
        FileOutputStream outS = new FileOutputStream(new File(file));
        CoreUtils.copy(inS, outS);
      } catch (FileNotFoundException e) {
        BooksActivator.getDefault().logError(e.getMessage(), e);
      }
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
