package com.gcsf.books.utilities;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

public class PathResolver {

  private static final String COLLECTION_FOLDER_NAME = "data";

  private static final String CONFIG_FOLDER_NAME = "config";

  private static PathResolver ourInstance = null;

  private String collectionPath = "";

  private String configurationPath = "";

  private PathResolver() {
    String envProp = System.getenv("startupfolder");
    if (null != envProp && !envProp.equals("")) {
      // we are in an eclipse environment
      System.out.println(envProp);
      collectionPath = new File(envProp).getAbsolutePath();
      configurationPath = new File(CONFIG_FOLDER_NAME, envProp)
          .getAbsolutePath();
    } else {
      // not in eclipse environment
      Location platformArea = Platform.getInstallLocation();
      if (null != platformArea
          && platformArea.getURL().getFile().indexOf("eclipse") == -1) {
        collectionPath = new File(platformArea.getURL().getFile(),
            COLLECTION_FOLDER_NAME).getAbsolutePath();
        configurationPath = new File(platformArea.getURL().getFile(),
            CONFIG_FOLDER_NAME).getAbsolutePath();
        System.out.println(collectionPath + " the collection path");
        System.out.println(configurationPath + " the configuration path");
      } else {
        System.out
            .println("you are in a eclipse environment ... "
                + "please define an environment variable pointing to your workspace location");
        System.exit(-1);
      }
    }
  }

  public static PathResolver getInstance() {
    if (null == ourInstance) {
      ourInstance = new PathResolver();
    }
    return ourInstance;
  }

  public String getLoggingFolder() {
    return configurationPath;
  }

  public String getCollectionFolder() {
    return collectionPath;
  }

}
