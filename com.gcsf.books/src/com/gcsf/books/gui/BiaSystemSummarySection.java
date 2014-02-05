package com.gcsf.books.gui;

import java.io.PrintWriter;

import org.eclipse.ui.about.ISystemSummarySection;

public class BiaSystemSummarySection implements ISystemSummarySection {

  // TODO add a more descriptive detail here

  @Override
  public void write(PrintWriter writer) {
    writer.println("User Name = " + System.getProperty("user.name"));
  }

}
