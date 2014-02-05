package com.gcsf.books.search.handlers;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.gcsf.books.search.SearchActivator;
import com.gcsf.books.search.filters.StaticFiltersController;
import com.gcsf.books.search.filters.nondynamic.StaticFilter;

public class CollectionSwitcherMenu extends ContributionItem {
  private static final String KEY_COLLECTION_DESCR = "k_s_descr";

  private static final String PERSPECTIVE_PATERN_DESCR = "com.gcsf.books.perspective.layout";

  private final SelectionListener menuListener = new CollectionMenuListener();

  public CollectionSwitcherMenu() {
  }

  public CollectionSwitcherMenu(String id) {
    super(id);
  }

  @Override
  public final boolean isDynamic() {
    return true;
  }

  /**
   * Fills a drop-down menu with all available perspectives. The current one is
   * selected.
   */
  @Override
  public void fill(Menu menu, int index) {

    MenuItem collectionItem = new MenuItem(menu, SWT.CASCADE, index);
    collectionItem.setText("In Collection Status");
    collectionItem.setEnabled(true);
    Menu collectionMenu = new Menu(collectionItem);
    collectionMenu.setVisible(true);
    collectionItem.setMenu(collectionMenu);

    int menuIndex = 0;
    for (StaticFilter aFilter : StaticFiltersController.getInstance()
        .getFilters()) {
      if (aFilter.isSeparator()) {
        MenuItem aMItem = new MenuItem(collectionMenu, SWT.SEPARATOR, menuIndex);
      } else {
        final MenuItem item = new MenuItem(collectionMenu, SWT.CHECK, menuIndex);
        item.setData(KEY_COLLECTION_DESCR, aFilter);
        item.setText(aFilter.getFilterLabel());
        final Image image = SearchActivator.getImageDescriptor(
            aFilter.getFilterImagePath()).createImage();
        item.setImage(image);
        item.addDisposeListener(new DisposeListener() {
          public void widgetDisposed(DisposeEvent e) {
            image.dispose();
          }
        });
        item.addSelectionListener(menuListener);
        if (aFilter.equals(StaticFiltersController.getInstance()
            .getActiveFilter())) {
          item.setSelection(true);
          collectionItem.setImage(image);
          collectionItem.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
              image.dispose();
            }
          });
        }
      }
      menuIndex++;
    }
  }

  // helping classes
  // ////////////////

  /**
   * applies the selected collection status filter
   */
  private static final class CollectionMenuListener extends SelectionAdapter {
    public void widgetSelected(SelectionEvent e) {
      MenuItem mItem = (MenuItem) e.widget;

      if (mItem.getSelection()) {
        StaticFilter aFilter = (StaticFilter) mItem
            .getData(KEY_COLLECTION_DESCR);
        StaticFiltersController.getInstance().setActiveFilter(aFilter);
      }
    }
  }

}
