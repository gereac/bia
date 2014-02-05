package com.gcsf.books.gui.bundles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IBundleGroup;
import org.eclipse.core.runtime.IBundleGroupProvider;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.about.InstallationPage;
import org.osgi.framework.Bundle;

import com.gcsf.books.gui.bundles.guimodel.BundlesContentProvider;
import com.gcsf.books.gui.bundles.guimodel.TableLabelProvider;
import com.gcsf.books.gui.bundles.internals.AboutBundleGroupData;
import com.gcsf.books.gui.bundles.internals.AboutData;
import com.gcsf.books.gui.bundles.model.CodeFeature;
import com.gcsf.books.gui.bundles.model.CodePlugin;
import com.gcsf.books.gui.bundles.model.CodeProvider;

public class BiaInstallationPage extends InstallationPage {

  private TreeViewer m_treeViewer;

  private AboutBundleGroupData[] bundleGroupInfos;

  private boolean reverseSort = false; // initially sort ascending

  private List<String> providersStrings = new ArrayList<String>();

  private List<CodeProvider> codeProviders = new ArrayList<CodeProvider>();

  public BiaInstallationPage() {
    // nothing to do yet
  }

  @Override
  public void createControl(Composite parent) {
    Group ipGroup = new Group(parent, SWT.NULL);
    ipGroup.setText("Custom About Page");
    GridLayout ipGGL = new GridLayout();
    ipGroup.setLayout(ipGGL);
    GridData ipGGD = new GridData();
    ipGGD.grabExcessHorizontalSpace = true;
    ipGGD.grabExcessVerticalSpace = true;
    ipGGD.horizontalAlignment = SWT.FILL;
    ipGGD.verticalAlignment = SWT.FILL;
    ipGroup.setLayoutData(ipGGD);

    Tree infoTree = new Tree(ipGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
        | SWT.FULL_SELECTION);
    infoTree.setHeaderVisible(true);
    infoTree.setLinesVisible(true);
    m_treeViewer = new TreeViewer(infoTree);

    TableLayout tableLayout = new TableLayout();
    tableLayout.addColumnData(new ColumnWeightData(40, 200));
    tableLayout.addColumnData(new ColumnWeightData(30, 150));
    tableLayout.addColumnData(new ColumnWeightData(30, 150));
    infoTree.setLayout(tableLayout);

    GridData treeLayoutData = new GridData();
    treeLayoutData.grabExcessHorizontalSpace = true;
    treeLayoutData.grabExcessVerticalSpace = true;
    treeLayoutData.horizontalAlignment = SWT.FILL;
    treeLayoutData.verticalAlignment = SWT.FILL;
    infoTree.setLayoutData(treeLayoutData);

    TreeColumn column1 = new TreeColumn(infoTree, SWT.LEFT);
    column1.setAlignment(SWT.LEFT);
    column1.setText("Provider/Feature/Plugin");
    column1.setMoveable(false);
    column1.setResizable(true);
    TreeColumn column2 = new TreeColumn(infoTree, SWT.RIGHT);
    column2.setAlignment(SWT.LEFT);
    column2.setText("Version");
    column2.setMoveable(false);
    column2.setResizable(true);
    TreeColumn column3 = new TreeColumn(infoTree, SWT.RIGHT);
    column3.setAlignment(SWT.LEFT);
    column3.setText("Id");
    column3.setMoveable(false);
    column3.setResizable(true);

    initializeBundleGroupInfos();

    m_treeViewer.setContentProvider(new BundlesContentProvider());
    m_treeViewer.setLabelProvider(new TableLabelProvider());
    m_treeViewer.setInput(codeProviders);
    m_treeViewer.expandToLevel(2);
  }

  // @Override
  // public void createPageButtons(Composite parent) {
  // createButton(parent, IDialogConstants.DETAILS_ID, "Test Button");
  // }
  //
  // @Override
  // protected void buttonPressed(int buttonId) {
  // if (buttonId == IDialogConstants.DETAILS_ID) {
  // System.out.println("Test button pressed");
  // }
  // }

  private void initializeBundleGroupInfos() {
    if (bundleGroupInfos == null) {
      IBundleGroupProvider[] providers = Platform.getBundleGroupProviders();

      // create a descriptive object for each BundleGroup
      LinkedList<AboutBundleGroupData> groups = new LinkedList<AboutBundleGroupData>();
      if (providers != null) {
        for (int i = 0; i < providers.length; ++i) {
          IBundleGroup[] bundleGroups = providers[i].getBundleGroups();
          for (int j = 0; j < bundleGroups.length; ++j) {
            groups.add(new AboutBundleGroupData(bundleGroups[j]));
          }
        }
      }
      bundleGroupInfos = (AboutBundleGroupData[]) groups
          .toArray(new AboutBundleGroupData[0]);
    } else {
      // the order of the array may be changed due to sorting, so create a
      // copy, since the client set this value.
      AboutBundleGroupData[] clientArray = bundleGroupInfos;
      bundleGroupInfos = new AboutBundleGroupData[clientArray.length];
      System.arraycopy(clientArray, 0, bundleGroupInfos, 0, clientArray.length);
    }
    AboutData.sortByProvider(reverseSort, bundleGroupInfos);

    CodeProvider codeProvider = null;
    for (int i = 0; i < bundleGroupInfos.length; i++) {
      if (!providersStrings.contains(bundleGroupInfos[i].getProviderName())) {
        providersStrings.add(bundleGroupInfos[i].getProviderName());
        codeProvider = new CodeProvider(bundleGroupInfos[i].getProviderName());
        codeProviders.add(codeProvider);
      } else {
        for (Iterator<CodeProvider> it = codeProviders.iterator(); it.hasNext();) {
          CodeProvider aProvider = (CodeProvider) it.next();
          if (aProvider.toString()
              .equals(bundleGroupInfos[i].getProviderName())) {
            codeProvider = aProvider;
            break;
          }
        }
      }
      CodeFeature codeFeature = new CodeFeature(codeProvider);
      codeFeature.setFeatureInfo(bundleGroupInfos[i].getName(),
          bundleGroupInfos[i].getVersion(), bundleGroupInfos[i].getId());
      codeProvider.addFeature(codeFeature);
      Bundle[] bundles = bundleGroupInfos[i].getBundleGroup().getBundles();
      for (Bundle bundle : bundles) {
        CodePlugin plugin = new CodePlugin(codeFeature, (String) bundle
            .getHeaders().get("Bundle-Name"), bundle.getVersion()
            .getQualifier(), bundle.getSymbolicName());
        codeFeature.addPlugin(plugin);
      }
    }
  }
}
