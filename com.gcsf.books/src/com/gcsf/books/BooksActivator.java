package com.gcsf.books;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.dynamichelpers.ExtensionTracker;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.gcsf.books.model.FolderItem;
import com.gcsf.books.model.Item;

public class BooksActivator extends AbstractUIPlugin {

  // The plug-in ID
  public static final String PLUGIN_ID = "com.gcsf.books";

  // The shared instance
  private static BooksActivator plugin;

  private FolderItem model = null;

  private IExtensionTracker tracker;

  /**
   * The constructor
   */
  public BooksActivator() {
    // nothing to do yet
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
   * )
   */
  public void start(BundleContext context) throws Exception {
    super.start(context);
    tracker = new ExtensionTracker(Platform.getExtensionRegistry());
    plugin = this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
   * )
   */
  public void stop(BundleContext context) throws Exception {
    plugin = null;
    tracker.close();
    super.stop(context);
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static BooksActivator getDefault() {
    return plugin;
  }

  /**
   * Returns an image descriptor for the image file at the given plug-in
   * relative path
   * 
   * @param path
   *          the path
   * @return the image descriptor
   */
  public static ImageDescriptor getImageDescriptor(String path) {
    return imageDescriptorFromPlugin(PLUGIN_ID, path);
  }

  /**
   * Returns an image descriptor for the image file at the given plug-in
   * relative path.
   * 
   * @param pluginId
   *          the ID of the plugin to load the image from.
   * @param path
   *          the path
   * @return the image descriptor
   */
  public static ImageDescriptor getImageDescriptor(String pluginId, String path) {
    return AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, path);
  }

  /**
   * @return the model
   */
  public FolderItem getModel() {
    if (this.model == null) {
      initModel();
    }
    return this.model;
  }

  /**
   * 
   */
  private void initModel() {
    this.model = new FolderItem("Root", null);

    final FolderItem leaf1 = new FolderItem("Leaf1", this.model);
    final FolderItem leaf2 = new FolderItem("Leaf2", this.model);
    final FolderItem leaf3 = new FolderItem("Leaf3", this.model);
    final FolderItem leaf4 = new FolderItem("Leaf4", this.model);

    new Item("item1", "This is a test1", "Some description", this.model, "2001");
    new Item("item2", "This is a test2", "Some description", this.model, "2002");

    final FolderItem leaf11 = new FolderItem("Leaf11", leaf1);
    final FolderItem leaf12 = new FolderItem("Leaf12", leaf1);
    final FolderItem leaf21 = new FolderItem("Leaf21", leaf2);
    final FolderItem leaf22 = new FolderItem("Leaf22", leaf2);
    final FolderItem leaf23 = new FolderItem("Leaf23", leaf2);
    final FolderItem leaf31 = new FolderItem("Leaf31", leaf3);
    final FolderItem leaf41 = new FolderItem("Leaf41", leaf4);
    final FolderItem leaf42 = new FolderItem("Leaf42", leaf4);
    final FolderItem leaf43 = new FolderItem("Leaf43", leaf4);
    final FolderItem leaf44 = new FolderItem("Leaf44", leaf4);

    new Item("item111", "This is a test111", "Some description", leaf11, "2000");
    new Item("item112", "This is a test112", "Some description", leaf11, "2020");
    new Item("item113", "This is a test113", "Some description", leaf11, "2030");
    new Item("item114", "This is a test114", "Some description", leaf11, "2040");
    new Item("item115", "This is a test115", "Some description", leaf11, "2000");
    new Item("item121", "This is a test121", "Some description", leaf12, "2040");
    new Item("item122", "This is a test122", "Some description", leaf12, "2030");
    new Item("item123", "This is a test123", "Some description", leaf12, "2020");
    new Item("item124", "This is a test124", "Some description", leaf12, "2000");

    new Item("item11", "This is a test11", "Some description", leaf1, "2030");
    new Item("item12", "Another test", "Some description", leaf1, "2000");
    new Item("item13", "This is a test12", "Some description", leaf1, "2040");
    new Item("item14", "This is a test12", "Some description", leaf1, "2020");
    new Item("item15", "This is a test12", "Some description", leaf1, "2020");
  }

  /**
   * Log an Error Message.
   * 
   * @param msg
   *          The message to log as Error.
   * @param e
   *          The occuring Exception to log.
   */
  public void logError(String msg, Throwable e) {
    if (msg == null)
      msg = ""; //$NON-NLS-1$

    getLog().log(
        new Status(IStatus.ERROR, getBundle().getSymbolicName(), IStatus.ERROR,
            msg, e));
  }

  /**
   * Log a Warning Message.
   * 
   * @param msg
   *          The message to log as Warning.
   * @param e
   *          The occuring Exception to log.
   */
  public void logWarning(String msg, Exception e) {
    if (msg == null)
      msg = ""; //$NON-NLS-1$

    getLog().log(
        new Status(IStatus.WARNING, getBundle().getSymbolicName(),
            IStatus.WARNING, msg, e));
  }

  public IExtensionTracker getExtensionTracker() {
    return tracker;
  }

}
