package com.gcsf.books.search.handlers;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.gcsf.books.search.SearchActivator;
import com.gcsf.books.search.filters.StaticFiltersController;
import com.gcsf.books.search.filters.nondynamic.StaticFilter;
import com.gcsf.books.search.preferences.ISearchPreferencesConstants;

public class ToolbarCollectionContributionItem extends ContributionItem {
  private static ToolItem dropItem;

  private static Menu dropMenu;

  private static final String KEY_COLLECTION_DESCR = "k_s_descr";

  private final SelectionListener menuListener = new CollectionMenuListener();

  IPropertyChangeListener preferenceListener = new IPropertyChangeListener() {
    /*
     * @see IPropertyChangeListener.propertyChange()
     */
    public void propertyChange(PropertyChangeEvent event) {
      if (event.getProperty().equals(
          ISearchPreferencesConstants.P_STATIC_FILTER)) {
        updateToolbar(StaticFiltersController.getInstance().getActiveFilter());
      }
    }

  };

  public ToolbarCollectionContributionItem() {
    SearchActivator.getDefault().getPreferenceStore()
        .addPropertyChangeListener(preferenceListener);
  }

  private void setDropItem(ToolItem aItem) {
    dropItem = aItem;
  }

  private static ToolItem getDropItem() {
    return dropItem;
  }

  private void setDropMenu(Menu aMenu) {
    dropMenu = aMenu;
  }

  private static Menu getDropMenu() {
    return dropMenu;
  }

  @Override
  public void fill(final ToolBar parent, int index) {
    if (StaticFiltersController.getInstance().isActive()) {
      final Menu menu = new Menu(parent.getShell(), SWT.POP_UP);
      int menuIndex = 0;

      for (StaticFilter aFilter : StaticFiltersController.getInstance()
          .getFilters()) {
        if (aFilter.isSeparator()) {
          MenuItem aMItem = new MenuItem(menu, SWT.SEPARATOR, menuIndex);
        } else {
          MenuItem aMItem = new MenuItem(menu, SWT.CHECK, menuIndex);
          aMItem.setText(aFilter.getFilterLabel());
          aMItem.setImage(SearchActivator.getImageDescriptor(
              aFilter.getFilterImagePath()).createImage());
          aMItem.setData(KEY_COLLECTION_DESCR, aFilter);
          aMItem.addSelectionListener(menuListener);
          if (aFilter.getFilterLabel().equalsIgnoreCase(
              StaticFiltersController.getInstance().getActiveFilter()
                  .getFilterLabel())) {
            aMItem.setSelection(true);
          }
        }
        menuIndex++;
      }
      setDropMenu(menu);
      final ToolItem item = new ToolItem(parent, SWT.DROP_DOWN, index);
      item.setToolTipText(StaticFiltersController.getInstance()
          .getActiveFilter().getFilterLabel());

      item.setImage(SearchActivator.getImageDescriptor(
          StaticFiltersController.getInstance().getActiveFilter()
              .getFilterImagePath()).createImage());

      item.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event event) {
          if (event.detail == SWT.ARROW || event.detail == SWT.NONE) {
            Rectangle rect = item.getBounds();
            Point pt = new Point(rect.x, rect.y + rect.height);
            pt = parent.toDisplay(pt);
            menu.setLocation(pt.x, pt.y);
            menu.setVisible(true);
          }
        }
      });
      setDropItem(item);
      parent.pack(true);
    }
  }

  private static void updateToolbar(StaticFilter aFilter) {
    ToolItem aItem = getDropItem();
    aItem.setImage(SearchActivator.getImageDescriptor(
        StaticFiltersController.getInstance().getActiveFilter()
            .getFilterImagePath()).createImage());
    aItem.setToolTipText(StaticFiltersController.getInstance()
        .getActiveFilter().getFilterLabel());
    MenuItem[] mItems = getDropMenu().getItems();
    for (MenuItem mItem : mItems) {
      boolean isSelected = aFilter == mItem.getData(KEY_COLLECTION_DESCR);
      if (isSelected != mItem.getSelection()) {
        mItem.setSelection(isSelected);
      }
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
        updateToolbar(aFilter);
      }
    }
  }

  @Override
  public final boolean isDynamic() {
    return true;
  }

  @Override
  public void dispose() {
    SearchActivator.getDefault().getPreferenceStore()
        .removePropertyChangeListener(preferenceListener);
    ;
  }
}
