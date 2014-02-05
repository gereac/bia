package com.gcsf.books.gui.bundles.guimodel;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.gcsf.books.gui.bundles.model.CodeFeature;
import com.gcsf.books.gui.bundles.model.CodePlugin;
import com.gcsf.books.gui.bundles.model.CodeProvider;

public class BundlesContentProvider implements ITreeContentProvider {

  public Object[] getChildren(Object parentElement) {
    if (parentElement instanceof List<?>)
      return ((List<?>) parentElement).toArray();
    if (parentElement instanceof CodeProvider)
      return ((CodeProvider) parentElement).getFeatures();
    if (parentElement instanceof CodeFeature)
      return ((CodeFeature) parentElement).getPlugins();
    return new Object[0];
  }

  public Object getParent(Object element) {
    if (element instanceof CodeFeature)
      return ((CodeFeature) element).getProvider();
    if (element instanceof CodePlugin)
      return ((CodePlugin) element).getFeature();
    return null;
  }

  public boolean hasChildren(Object element) {
    if (element instanceof List<?>)
      return ((List<?>) element).size() > 0;
    if (element instanceof CodeProvider)
      return ((CodeProvider) element).getFeatures().length > 0;
    if (element instanceof CodeFeature)
      return ((CodeFeature) element).getPlugins().length > 0;
    return false;
  }

  public Object[] getElements(Object providers) {
    return getChildren(providers);
  }

  public void dispose() {
  }

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
  }

}
