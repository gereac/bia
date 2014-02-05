package com.gcsf.books.contributions;

import java.util.ArrayList;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

public class HelpMenuCompoundContribution extends CompoundContributionItem {
  private static boolean ctrlPressed = false;

  static {
    PlatformUI.getWorkbench().getDisplay().addFilter(SWT.KeyDown,
        new Listener() {

          public void handleEvent(Event e) {
            if ((e.keyCode & SWT.CTRL) != 0) {
              setCtrlPressed(true);
            }
          }
        });
    PlatformUI.getWorkbench().getDisplay().addFilter(SWT.KeyUp, new Listener() {

      public void handleEvent(Event e) {
        if ((e.keyCode & SWT.CTRL) != 0) {
          setCtrlPressed(false);
        }
      }
    });
  }

  private static void setCtrlPressed(boolean b) {
    ctrlPressed = b;
  }

  private boolean getCtrlPressed() {
    return ctrlPressed;
  }

  protected IContributionItem[] getContributionItems() {
    if (getCtrlPressed()) {
      IContributionItem[] list = new IContributionItem[4];
      Separator keySeparator = new Separator();
      keySeparator.setVisible(true);
      keySeparator.setId("com.gcsf.books.separator.keyBindings");
      list[0] = keySeparator;

      CommandContributionItemParameter pKey = new CommandContributionItemParameter(
          PlatformUI.getWorkbench().getActiveWorkbenchWindow(), "",
          "com.gcsf.books.command.showKeyBindings", SWT.PUSH);
      CommandContributionItem keyItem = new CommandContributionItem(pKey);
      keyItem.setVisible(true);
      list[1] = keyItem;

      Separator logSeparator = new Separator();
      logSeparator.setVisible(true);
      logSeparator.setId("com.gcsf.books.separator.exportLogFile");
      list[2] = logSeparator;

      CommandContributionItemParameter pLog = new CommandContributionItemParameter(
          PlatformUI.getWorkbench().getActiveWorkbenchWindow(), "",
          "com.gcsf.books.command.exportLogFile", SWT.PUSH);
      CommandContributionItem logItem = new CommandContributionItem(pLog);
      logItem.setVisible(true);
      list[3] = logItem;

      setCtrlPressed(false);

      return list;
    } else {
      ArrayList<IContributionItem> list = new ArrayList<IContributionItem>();
      return (IContributionItem[]) list.toArray(new IContributionItem[list
          .size()]);
    }
  }
}
