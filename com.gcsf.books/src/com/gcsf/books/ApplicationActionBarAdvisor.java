package com.gcsf.books;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.State;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.UIElement;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
  private IWorkbenchWindow aWindow;

  private ICoolBarManager aCoolbar;

  private Command lockCommand;

  private ICommandService commandService;

  public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
    super(configurer);
    aWindow = configurer.getWindowConfigurer().getWindow();
  }

  @Override
  protected void fillCoolBar(final ICoolBarManager coolBar) {

    aCoolbar = coolBar;

    commandService = (ICommandService) aWindow
        .getService(ICommandService.class);
    IHandlerService handlerService = (IHandlerService) aWindow
        .getService(IHandlerService.class);

    Category locks = commandService
        .getCategory("com.gcsf.books.command.category.lock");
    if (!locks.isDefined()) {
      locks.define("Lock", "Lock category commands.");
    }
    lockCommand = commandService
        .getCommand("com.gcsf.books.command.lockToolBar");
    if (!lockCommand.isDefined()) {
      lockCommand.define("Lock Toolbar", "", locks);
    }

    State aState = new State();
    aState.setValue(Boolean.FALSE);
    aState.setId(RegistryToggleState.STATE_ID);
    lockCommand.addState(RegistryToggleState.STATE_ID, aState);

    IHandler handler = new LockHandler();
    handlerService.activateHandler("com.gcsf.books.command.lockToolBar",
        handler);

    MenuManager coolBarContextMenuManager = new MenuManager(null,
        "com.gcsf.books.CoolBarContextMenu"); //$NON-NLS-1$
    coolBar.setContextMenuManager(coolBarContextMenuManager);

    CommandContributionItemParameter pCustomize = new CommandContributionItemParameter(
        aWindow, "", "com.gcsf.books.command.customizeToolbar", SWT.PUSH);
    pCustomize.icon = BooksActivator
        .getImageDescriptor("/resource/icons/toolbar/toolbar.gif");
    CommandContributionItem customizeItem = new CommandContributionItem(
        pCustomize);
    customizeItem.setVisible(true);

    coolBarContextMenuManager.add(customizeItem);

    Separator lockSeparator = new Separator();
    lockSeparator.setVisible(true);
    lockSeparator.setId("com.gcsf.books.separator.lockToolBarSeparator");

    coolBarContextMenuManager.add(lockSeparator);

    CommandContributionItemParameter pLock = new CommandContributionItemParameter(
        aWindow, "", "com.gcsf.books.command.lockToolBar", SWT.CHECK);
    pLock.icon = BooksActivator
        .getImageDescriptor("/resource/icons/lock/lock.png");
    CommandContributionItem lockItem = new CommandContributionItem(pLock);
    lockItem.setVisible(true);

    coolBarContextMenuManager.add(lockItem);
  }

  class LockHandler extends AbstractHandler implements IHandler,
      IElementUpdater {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
      if (!aCoolbar.getLockLayout()) {
        aCoolbar.setLockLayout(true);
        HandlerUtil.toggleCommandState(lockCommand);
      } else {
        aCoolbar.setLockLayout(false);
        HandlerUtil.toggleCommandState(lockCommand);
      }
      commandService.refreshElements(event.getCommand().getId(), null);
      return null;
    }

    @Override
    public void updateElement(UIElement element, Map parameters) {
      Object toggleStateObject = lockCommand.getState(
          RegistryToggleState.STATE_ID).getValue();
      if (!(Boolean) toggleStateObject) {
        element.setIcon(BooksActivator
            .getImageDescriptor("/resource/icons/lock/lock.png"));
        element.setText("Lock toolbar");
      } else {
        element.setIcon(BooksActivator
            .getImageDescriptor("/resource/icons/lock/lock_open.png"));
        element.setText("Unlock toolbar");
      }
    }
  }
}
