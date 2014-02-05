package com.gcsf.books.utilities;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.themes.ITheme;

public class Utils {
  /**
   * Date format for books management
   */
  public static final String DATE_FORMAT = "dd.MM.yy - HH:mm";

  public static final String DEFAULT_PERSPECTIVE_ID = "com.gcsf.books.perspective.layout.fullheighttree";

  /** News-Text Font Id */
  public static final String TEXT_FONT_ID = "com.gcsf.books.ui.textFont"; //$NON-NLS-1$

  /* Default Text Font Height */
  private static final int DEFAULT_TEXT_FONT_HEIGHT = 10;

  public static final Font SUBHEADER_FONT = new Font(null,
      "Arial", 10, SWT.BOLD); //$NON-NLS-1$

  public static final Color ORANGE_COLOR = new Color(null, 245, 143, 0);

  public static final Color GREY_COLOR = new Color(null, 236, 233, 226);

  public static final Color HEADING_COLOR = new Color(null, 102, 102, 102);

  private static FormColors formColors;

  public static FormColors FORM_COLOR(final Display display) {
    if (formColors == null) {
      formColors = new FormColors(display);
      formColors.createColor(IFormColors.H_GRADIENT_START, ORANGE_COLOR
          .getRGB());
      formColors.createColor(IFormColors.H_GRADIENT_END, GREY_COLOR.getRGB());
      formColors
          .createColor(IFormColors.H_BOTTOM_KEYLINE1, GREY_COLOR.getRGB());
      formColors.createColor(IFormColors.H_BOTTOM_KEYLINE2, ORANGE_COLOR
          .getRGB());
      formColors.createColor(IFormColors.TITLE, HEADING_COLOR.getRGB());
    }
    return formColors;
  }

  /**
   * @param zoomIn
   * @param reset
   */
  @SuppressWarnings("restriction")
  public static void zoomText(boolean zoomIn, boolean reset) {

    // TODO fix the reset of the font ... does not work correctly now

    Font bannerFont = JFaceResources.getBannerFont();
    Font defaultFont = JFaceResources.getDefaultFont();
    Font dialogFont = JFaceResources.getDialogFont();
    Font headerFont = JFaceResources.getHeaderFont();
    Font textFont = JFaceResources.getTextFont();

    // for (FontData fontData : bannerFont.getFontData()) {
    // System.out.println(fontData.height + " " + fontData.getName());
    // }
    // for (FontData fontData : defaultFont.getFontData()) {
    // System.out.println(fontData.height + " " + fontData.getName());
    // }
    // for (FontData fontData : dialogFont.getFontData()) {
    // System.out.println(fontData.height + " " + fontData.getName());
    // }
    // for (FontData fontData : headerFont.getFontData()) {
    // System.out.println(fontData.height + " " + fontData.getName());
    // }
    // for (FontData fontData : textFont.getFontData()) {
    // System.out.println(fontData.height + " " + fontData.getName());
    // }

    /* Retrieve Font */
    ITheme theme = PlatformUI.getWorkbench().getThemeManager()
        .getCurrentTheme();
    FontRegistry registry = theme.getFontRegistry();
    FontData[] oldFontDatas = registry.getFontData(TEXT_FONT_ID);
    FontData[] newFontDatas = new FontData[oldFontDatas.length];

    /* Set Height */
    for (int i = 0; i < oldFontDatas.length; i++) {
      FontData oldFontData = oldFontDatas[i];
      int oldHeight = oldFontData.getHeight();

      if (reset)
        newFontDatas[i] = new FontData(oldFontData.getName(),
            DEFAULT_TEXT_FONT_HEIGHT, oldFontData.getStyle());
      else
        newFontDatas[i] = new FontData(oldFontData.getName(),
            zoomIn ? oldHeight + 1 : Math.max(oldHeight - 1, 0), oldFontData
                .getStyle());
    }

    registry.put(TEXT_FONT_ID, newFontDatas);

    Display display = PlatformUI.getWorkbench().getDisplay();
    Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
    Font font = new Font(display, newFontDatas[0]);

    try {
      shell.setFont(font);
      applyFont(font, shell);
    } catch (Exception e) {
      System.out.println(e);
    }

    /* Store in Preferences */
    String key = org.eclipse.ui.internal.themes.ThemeElementHelper
        .createPreferenceKey(theme, TEXT_FONT_ID);
    String fdString = PreferenceConverter.getStoredRepresentation(newFontDatas);
    String storeString = org.eclipse.ui.internal.util.PrefUtil
        .getInternalPreferenceStore().getString(key);
    if (!fdString.equals(storeString))
      org.eclipse.ui.internal.util.PrefUtil.getInternalPreferenceStore()
          .setValue(key, fdString);
  }

  private static void applyFont(Font font, Composite composite) {
    // printChildren((Composite) control, 0);
    for (Control c : composite.getChildren()) {
      if (c instanceof Composite) {
        if (c instanceof Tree || c instanceof Table) {
          c.setFont(font);
        }
        applyFont(font, (Composite) c);
      }
    }
  }

  private static void printChildren(Composite composite, int count) {
    StringBuilder spaces = new StringBuilder(count * 2);
    for (int i = 0; i < count * 2; i++) {
      spaces.append(' ');
    }
    for (Control c : composite.getChildren()) {
      System.out.println(String.format("%s%s (%s)", spaces.toString(), c
          .toString(), c.getLayoutData()));
      if (c instanceof Composite) {
        printChildren((Composite) c, count + 1);
      }
    }
  }

}
