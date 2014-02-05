package com.gcsf.books;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CBanner;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

  // private Control coolBar;

  private Control page;

  private Control customStatusLine;

  public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
    super(configurer);
  }

  public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
    return new ApplicationActionBarAdvisor(configurer);
  }

  public void preWindowOpen() {

    IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
    configurer.setShowCoolBar(true);
    configurer.setShowMenuBar(true);
    configurer.setShowPerspectiveBar(false);
    configurer.setShowFastViewBars(false);
    configurer.setShowProgressIndicator(false);
  }

  @Override
  public void postWindowCreate() {
    IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
    // TODO open maximized on the first monitor
    if (configurer.getWindow().getShell() != null) {
      configurer.getWindow().getShell().setMaximized(true);
      configurer.getWindow().getShell().setMinimumSize(800, 600);
    }
    // TODO check if the code bellow should really be here
    PreferenceManager crtManager = PlatformUI.getWorkbench()
        .getPreferenceManager();
    List<?> aList = crtManager.getElements(PreferenceManager.PRE_ORDER);
    for (Iterator<?> it = aList.iterator(); it.hasNext();) {
      IPreferenceNode aNode = (IPreferenceNode) it.next();
      if (aNode.getId().contains("org.eclipse.help.ui")) {
        crtManager.remove(aNode);
      }
    }
  }

  @Override
  public void createWindowContents(Shell aShell) {
    IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
    ApplicationWindow window = (ApplicationWindow) getWindowConfigurer()
        .getWindow();
    Menu menuBar = window.getMenuBarManager().createMenuBar(
        (Decorations) aShell);
    aShell.setMenuBar(menuBar);

    CBanner cBanner = new CBanner(aShell, SWT.NONE);
    cBanner.setSimple(true);
    CoolBar coolBar2 = window.getCoolBarManager().createControl(cBanner);
    cBanner.setLeft(coolBar2);

    FormLayout layout = new FormLayout();
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    aShell.setLayout(layout);

    page = configurer.createPageComposite(aShell);
    customStatusLine = createCustomStatusLine(aShell);

    FormData data = new FormData();
    data.top = new FormAttachment(0, 0);
    data.left = new FormAttachment(0, 0);
    data.right = new FormAttachment(100, 0);
    cBanner.setLayoutData(data);

    data = new FormData();
    data.left = new FormAttachment(0, 0);
    data.right = new FormAttachment(100, 0);
    data.bottom = new FormAttachment(100, 0);
    customStatusLine.setLayoutData(data);

    data = new FormData();
    data.left = new FormAttachment(0, 0);
    data.top = new FormAttachment(cBanner);
    data.bottom = new FormAttachment(customStatusLine, 0);
    data.right = new FormAttachment(100, 0);
    page.setLayoutData(data);

    aShell.pack();
  }

  private Composite createCustomStatusLine(Shell aShell) {
    Composite myComposite = new Composite(aShell, SWT.NONE);
    FormLayout layout = new FormLayout();
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    myComposite.setLayout(layout);

    Composite ti = new Composite(myComposite, SWT.NONE);
    FormLayout layout2 = new FormLayout();
    layout2.marginLeft = 2;
    layout2.marginRight = 1;
    ti.setLayout(layout2);
    FormData data = new FormData();
    data.top = new FormAttachment(0, 0);
    data.left = new FormAttachment(myComposite, 0);
    data.right = new FormAttachment(10, 0);
    ti.setLayoutData(data);
    Group git = new Group(ti, SWT.NONE);
    git.setLayout(new FormLayout());
    data = new FormData();
    data.top = new FormAttachment(0, 0);
    data.left = new FormAttachment(ti);
    data.right = new FormAttachment(100, 0);
    git.setLayoutData(data);
    Label lit1 = new Label(git, SWT.NONE);
    lit1.setText("Total books:");
    data = new FormData();
    data.top = new FormAttachment(0, 0);
    data.left = new FormAttachment(0, 0);
    lit1.setLayoutData(data);
    Label lit2 = new Label(git, SWT.NONE);
    lit2.setText(" 32");
    data = new FormData();
    data.top = new FormAttachment(lit1, 0, SWT.CENTER);
    data.left = new FormAttachment(lit1);
    lit2.setLayoutData(data);

    Composite si = new Composite(myComposite, SWT.NONE);
    FormLayout layout3 = new FormLayout();
    layout3.marginLeft = 1;
    layout3.marginRight = 1;
    si.setLayout(layout3);
    data = new FormData();
    data.top = new FormAttachment(0, 0);
    data.left = new FormAttachment(ti, 0);
    data.right = new FormAttachment(30, 0);
    si.setLayoutData(data);
    Group gis = new Group(si, SWT.NONE);
    gis.setLayout(new FormLayout());
    data = new FormData();
    data.top = new FormAttachment(0, 0);
    data.left = new FormAttachment(si);
    data.right = new FormAttachment(100, 0);
    gis.setLayoutData(data);
    Label lis1 = new Label(gis, SWT.NONE);
    lis1.setText("");
    data = new FormData();
    data.top = new FormAttachment(0, 0);
    data.left = new FormAttachment(0, 0);
    lis1.setLayoutData(data);

    Composite ri = new Composite(myComposite, SWT.NONE);
    FormLayout layout4 = new FormLayout();
    layout4.marginLeft = 1;
    layout4.marginRight = 1;
    ri.setLayout(layout4);
    data = new FormData();
    data.top = new FormAttachment(0, 0);
    data.left = new FormAttachment(si, 0);
    data.right = new FormAttachment(45, 0);
    ri.setLayoutData(data);
    Group gir = new Group(ri, SWT.NONE);
    gir.setLayout(new FormLayout());
    data = new FormData();
    data.top = new FormAttachment(0, 0);
    data.left = new FormAttachment(ri);
    data.right = new FormAttachment(100, 0);
    gir.setLayoutData(data);
    Label lir1 = new Label(gir, SWT.NONE);
    lir1.setText("");
    data = new FormData();
    data.top = new FormAttachment(0, 0);
    data.left = new FormAttachment(0, 0);
    lir1.setLayoutData(data);

    Composite fi = new Composite(myComposite, SWT.NONE);
    FormLayout layout5 = new FormLayout();
    layout5.marginLeft = 1;
    layout5.marginRight = 2;
    fi.setLayout(layout5);
    data = new FormData();
    data.top = new FormAttachment(0, 0);
    data.left = new FormAttachment(ri, 0);
    data.right = new FormAttachment(100, 0);
    fi.setLayoutData(data);
    Group gif = new Group(fi, SWT.NONE);
    gif.setLayout(new FormLayout());
    data = new FormData();
    data.top = new FormAttachment(0, 0);
    data.left = new FormAttachment(fi);
    data.right = new FormAttachment(100, 0);
    gif.setLayoutData(data);
    Label lif1 = new Label(gif, SWT.NONE);
    lif1.setText("");
    data = new FormData();
    data.top = new FormAttachment(0, 0);
    data.left = new FormAttachment(0, 0);
    lif1.setLayoutData(data);

    return myComposite;
  }
}
