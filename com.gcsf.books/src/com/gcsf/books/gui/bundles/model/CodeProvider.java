package com.gcsf.books.gui.bundles.model;

import java.util.ArrayList;
import java.util.List;

public class CodeProvider {
  String providerName;

  List<CodeFeature> features = new ArrayList<CodeFeature>();

  public CodeProvider(String providerName) {
    this.providerName = providerName;
  }

  public void addFeature(CodeFeature feature) {
    features.add(feature);
  }

  public CodeFeature[] getFeatures() {
    return (CodeFeature[]) features.toArray(new CodeFeature[features.size()]);
  }

  public String toString() {
    return providerName;
  }
}
