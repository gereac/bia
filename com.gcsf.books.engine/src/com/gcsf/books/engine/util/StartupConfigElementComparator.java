package com.gcsf.books.engine.util;

import java.util.Comparator;

import org.eclipse.core.runtime.IConfigurationElement;

public class StartupConfigElementComparator implements
    Comparator<IConfigurationElement> {

  @Override
  public int compare(IConfigurationElement aConfig1,
      IConfigurationElement aConfig2) {
    Integer level1 = Integer.parseInt(aConfig1.getAttribute("level"));
    Integer level2 = Integer.parseInt(aConfig2.getAttribute("level"));
    return level1.compareTo(level2);
  }

}
