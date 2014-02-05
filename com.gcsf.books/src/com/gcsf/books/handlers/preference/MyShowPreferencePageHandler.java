package com.gcsf.books.handlers.preference;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.gcsf.books.BooksActivator;
import com.gcsf.books.utilities.RegistryReader;

public class MyShowPreferencePageHandler extends AbstractHandler implements
    IHandler {

  @Override
  public void dispose() {
    // TODO Auto-generated method stub
  }

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    if (reg == null)
      reg = new PreferenceRegistry();

    PreferenceManager manager = new PreferenceManager('/');
    for (Node node : reg.delegates())
      manager.addToRoot(node);

    PreferenceDialog dialog = new PreferenceDialog(PlatformUI.getWorkbench()
        .getActiveWorkbenchWindow().getShell(), manager);
    dialog.open();

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

  PreferenceRegistry reg;

  private class Node extends PreferenceNode {
    private IConfigurationElement configElement;

    public Node(String id, IConfigurationElement configurationElement) {
      super(id);
      this.configElement = configurationElement;
    }

    public String getLabelText() {
      return configElement.getAttribute("name"); //$NON-NLS-1$
    }

    public void createPage() {
      IWorkbenchPreferencePage page;
      try {
        page = (IWorkbenchPreferencePage) configElement
            .createExecutableExtension("class"); //$NON-NLS-1$
      } catch (CoreException e) {
        throw new RuntimeException(e);
      }

      page.init(PlatformUI.getWorkbench());
      if (getLabelImage() != null) {
        page.setImageDescriptor(getImageDescriptor());
      }
      page.setTitle(getLabelText());
      setPage(page);
    }
  }

  private class PreferenceRegistry extends RegistryReader<Node> {

    public PreferenceRegistry() {
      init(BooksActivator.getDefault().getExtensionTracker(),
          PlatformUI.PLUGIN_ID + ".preferencePages"); //$NON-NLS-1$
    }

    @Override
    protected Node createDelegate(IConfigurationElement configElement)
      throws CoreException {
      String id = configElement.getAttribute("id"); //$NON-NLS-1$
      return id.contains("org.eclipse.help.ui") ? null : new Node(id,
          configElement);
    }

    @Override
    protected void removeDelegate(Node delegate) {
    }

  }

}
