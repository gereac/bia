package com.gcsf.books.gui.bundles.guimodel;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.gcsf.books.gui.bundles.model.CodeFeature;
import com.gcsf.books.gui.bundles.model.CodePlugin;

public class TableLabelProvider implements ITableLabelProvider {
  public Image getColumnImage(Object element, int columnIndex) {
    return null;
  }

  public String getColumnText(Object element, int columnIndex) {
    switch (columnIndex) {
      case 0:
        if (element instanceof CodeFeature)
          return ((CodeFeature) element).getFeatureName();
        if (element instanceof CodePlugin)
          return ((CodePlugin) element).getPluginName();
        return element.toString();
      case 1:
        if (element instanceof CodeFeature)
          return ((CodeFeature) element).getFeatureVersion();
        if (element instanceof CodePlugin)
          return ((CodePlugin) element).getPluginVersion();
      case 2:
        if (element instanceof CodeFeature)
          return ((CodeFeature) element).getFeatureId();
        if (element instanceof CodePlugin)
          return ((CodePlugin) element).getPluginId();
    }
    return null;
  }

  public void addListener(ILabelProviderListener listener) {
  }

  public void dispose() {
  }

  public boolean isLabelProperty(Object element, String property) {
    return false;
  }

  public void removeListener(ILabelProviderListener listener) {
  }
}
