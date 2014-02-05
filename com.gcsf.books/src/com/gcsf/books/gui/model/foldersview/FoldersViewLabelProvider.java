/**
 * 
 */
package com.gcsf.books.gui.model.foldersview;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.gcsf.books.BooksActivator;
import com.gcsf.books.export.IBookDecorator;
import com.gcsf.books.model.FolderItem;
import com.gcsf.books.model.Item;
import com.gcsf.books.preferences.IPreferenceConstants;

public class FoldersViewLabelProvider extends StyledCellLabelProvider {

  private static final String FOREGROUND_COLOR_STYLER = "custom_foreground_color";

  // private static final String BACKGROUND_COLOR_STYLER =
  // "custom_background_color";

  private static final String EXTENSION_POINT_STATIC_FILTER_OVER_IMAGE = "com.gcsf.books.filters.static.overimage";

  private Image bookImage;

  /*
   * @see ILabelProvider#getImage(Object)
   */
  public Image getImage(Object aElement) {
    if (aElement instanceof FolderItem) {
      return PlatformUI.getWorkbench().getSharedImages().getImage(
          ISharedImages.IMG_OBJ_FOLDER);
    } else {
      if (BooksActivator.getDefault().getPreferenceStore().getBoolean(
          IPreferenceConstants.P_BOOK_DISPLAY_MODE)) {
        bookImage = BooksActivator.getImageDescriptor(
            "/resource/icons/book/book.png").createImage();
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
                bookImage, BooksActivator.getImageDescriptor(pluginContributor,
                    o.getDecoratorImagePath()), IDecoration.BOTTOM_RIGHT);
            return overlayIcon.createImage();
          }
        }
      } else {
        bookImage = BooksActivator.getImageDescriptor(
            "resource/icons/misc/container_obj.gif").createImage();
      }
      return bookImage;
    }
  }

  public void update(ViewerCell cell) {
    Object obj = cell.getElement();
    StyledString styledString = new StyledString(((Item) obj).getId());

    ColorRegistry colorRegistry = JFaceResources.getColorRegistry();
    colorRegistry.put(FOREGROUND_COLOR_STYLER, new RGB(255, 0, 0));
    // colorRegistry.put(BACKGROUND_COLOR_STYLER, new RGB(0, 0, 255));
    // TODO see if the color definition bellow shouldn't be moved to utilities
    // class/package

    if (BooksActivator.getDefault().getPreferenceStore().getBoolean(
        IPreferenceConstants.P_CHECK_COUNTER)) {
      if (obj instanceof FolderItem) {
        FolderItem parent = (FolderItem) obj;
        Styler style = StyledString.createColorRegistryStyler(
            FOREGROUND_COLOR_STYLER, null);
        if (parent.getChildren().size() > 0) {
          styledString.append(" (" + parent.getChildren().size() + ")", style);
        }
      }
    }

    cell.setText(styledString.toString());
    cell.setStyleRanges(styledString.getStyleRanges());
    cell.setImage(getImage(obj));
    super.update(cell);
  }

  @Override
  public void dispose() {
    if (null != bookImage) {
      bookImage.dispose();
    }
  }

}