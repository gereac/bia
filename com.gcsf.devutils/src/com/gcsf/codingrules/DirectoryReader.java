package com.gcsf.codingrules;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

//------------------------------------------------------------------------
public class DirectoryReader {

  // ----------------------------------------------------------------------
  public List<File> readFiles(String aDirectory, String aFileExtension) {
    if (null == aDirectory) {
      throw new IllegalArgumentException(
          "aDirectory is not allowed to be null ...");
    }

    List<File> result = new ArrayList<File>();

    File outputDirectory = new File(aDirectory);
    File[] allXmlFiles = outputDirectory.listFiles(new InternalFileFilter(
        aFileExtension));

    for (File xmlFile : allXmlFiles) {
      result.add(xmlFile);
    }

    return result;
  }
}

// ------------------------------------------------------------------------
class InternalFileFilter implements FileFilter {
  private String myExtension = null;

  // ----------------------------------------------------------------------
  public InternalFileFilter(String aExtension) {
    if (null != aExtension) {
      myExtension = aExtension.toLowerCase();
    }
  }

  // ----------------------------------------------------------------------
  public boolean accept(File aPathName) {
    if (null == myExtension) {
      return true;
    }

    return (aPathName.getName().toLowerCase().endsWith(myExtension));
  }
}
