/**
 * 
 */
package com.gcsf.books.gui.model.listview;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.gcsf.books.BooksActivator;
import com.gcsf.books.export.IBookDecorator;
import com.gcsf.books.model.FolderItem;
import com.gcsf.books.model.Item;
import com.gcsf.books.preferences.IPreferenceConstants;

public class ListViewLabelProvider extends LabelProvider implements
    ITableLabelProvider {

  private static final String EXTENSION_POINT_STATIC_FILTER_OVER_IMAGE = "com.gcsf.books.filters.static.overimage";

  @Override
  public void dispose() {
    bookImage.dispose();
  }

  private Image bookImage;

  public String getColumnText(Object aObject, int aIndex) {
    String returnValue = null;
    switch (aIndex) {
      case 0:
        returnValue = ((Item) aObject).getId();
        break;
      case 1:
        returnValue = ((Item) aObject).getDetail1();
        break;
      case 2:
        returnValue = ((Item) aObject).getDetail2();
        break;
      case 3:
        returnValue = ((Item) aObject).getYear();
        break;
      default:
        break;
    }
    return returnValue;
  }

  public Image getColumnImage(final Object element, final int columnIndex) {
    Image returnValue = null;
    if (columnIndex == 0) {
      if (element instanceof FolderItem) {
        returnValue = PlatformUI.getWorkbench().getSharedImages().getImage(
            ISharedImages.IMG_OBJ_FOLDER);
      } else {
        if (BooksActivator.getDefault().getPreferenceStore().getBoolean(
            IPreferenceConstants.P_BOOK_DISPLAY_MODE)) {
          bookImage = BooksActivator.getImageDescriptor(
              "/resource/icons/book/book.png").createImage();
          returnValue = bookImage;
          if (BooksActivator.getDefault().getPreferenceStore().getBoolean(
              IPreferenceConstants.P_CHECK_COLLECTION_STATUS)) {
            IConfigurationElement[] config = Platform.getExtensionRegistry()
                .getConfigurationElementsFor(
                    EXTENSION_POINT_STATIC_FILTER_OVER_IMAGE);
            if (config.length > 0) {
              IBookDecorator o = null;
              String pluginContributor = "";
              for (IConfigurationElement e : config) {
                try {
                  o = (IBookDecorator) e.createExecutableExtension("class");
                  pluginContributor = e.getContributor().getName();
                } catch (CoreException e1) {
                  // TODO check if this should be caught or not
                }
              }
              DecorationOverlayIcon overlayIcon = new DecorationOverlayIcon(
                  bookImage, BooksActivator.getImageDescriptor(
                      pluginContributor, o.getDecoratorImagePath()),
                  IDecoration.BOTTOM_RIGHT);
              return overlayIcon.createImage();
            }
          }
        } else {
          bookImage = BooksActivator.getImageDescriptor(
              "resource/icons/misc/container_obj.gif").createImage();
        }
        returnValue = bookImage;
      }
    }
    return returnValue;
  }

}