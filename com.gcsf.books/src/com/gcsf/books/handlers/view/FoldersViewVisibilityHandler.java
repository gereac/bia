package com.gcsf.books.handlers.view;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.RadioState;

import com.gcsf.books.BooksActivator;
import com.gcsf.books.gui.views.FoldersView;
import com.gcsf.books.preferences.IPreferenceConstants;

public class FoldersViewVisibilityHandler extends AbstractHandler implements
    IHandler {

  /**
   * The name of the parameter providing the view identifier.
   */
  //private static final String PARAMETER_NAME_VISIBILITY = "org.eclipse.ui.commands.radioStateParameter"; //$NON-NLS-1$

  private static final String FOLDERS_VIEW_VIEWID = FoldersView.ID;

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {

    if (HandlerUtil.matchesRadioState(event))
      return null; // we are already in the updated state - do nothing

    String currentState = event.getParameter(RadioState.PARAMETER_ID);

    Boolean isFoldersViewVisible = Boolean.TRUE;
    // Get the visibility parameter, if any.
    Object value = event.getParameter(RadioState.PARAMETER_ID);
    if (null == value) {
      throw new ExecutionException(
          "The visibility parameter should not be null");
    } else {
      isFoldersViewVisible = (Boolean.valueOf((String) value));
    }

    IWorkbenchWindow window = HandlerUtil
        .getActiveWorkbenchWindowChecked(event);
    IWorkbenchPage activePage = window.getActivePage();

    if (activePage == null) {
      throw new ExecutionException("The active page should not be null");
    } else {
      IViewPart foldersViewPart = activePage.findView(FOLDERS_VIEW_VIEWID);
      if (null == foldersViewPart) {
        // there is no folders view displayed
        if (!activePage.getPerspective().getId().endsWith("simplelayout")) {
          // the current perspective is not simple layout
          try {
            BooksActivator.getDefault().getPreferenceStore().setValue(
                IPreferenceConstants.P_BOOK_DISPLAY_MODE, true);
            activePage.showView(FOLDERS_VIEW_VIEWID);
          } catch (PartInitException e) {
            System.out.println(e);
          }
        } else {
          BooksActivator.getDefault().getPreferenceStore().setValue(
              IPreferenceConstants.P_BOOK_DISPLAY_MODE,
              isFoldersViewVisible.booleanValue());
        }
      } else {
        boolean isFoldersViewVisibleInPage = activePage
            .isPartVisible(foldersViewPart);
        BooksActivator.getDefault().getPreferenceStore().setValue(
            IPreferenceConstants.P_BOOK_DISPLAY_MODE,
            isFoldersViewVisible.booleanValue());
        if (!isFoldersViewVisible.booleanValue() && isFoldersViewVisibleInPage) {
          activePage.hideView(foldersViewPart);
        }
      }
    }

    // and finally update the current state
    HandlerUtil.updateRadioState(event.getCommand(), currentState);
    return null;
  }
}
