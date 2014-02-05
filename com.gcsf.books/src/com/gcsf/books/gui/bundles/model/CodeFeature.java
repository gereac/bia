package com.gcsf.books.gui.bundles.model;

import java.util.ArrayList;
import java.util.List;

public class CodeFeature {
  private CodeProvider provider;

  String featureName;

  String featureVersion;

  String featureId;

  List<CodePlugin> plugins = new ArrayList<CodePlugin>();

  public CodeFeature(CodeProvider provider) {
    this.setProvider(provider);
  }

  public void addPlugin(CodePlugin plugin) {
    plugins.add(plugin);
  }

  public CodePlugin[] getPlugins() {
    return (CodePlugin[]) plugins.toArray(new CodePlugin[plugins.size()]);
  }

  public void setFeatureInfo(String featureName, String featureVersion,
      String featureId) {
    this.featureName = featureName;
    this.featureVersion = featureVersion;
    this.featureId = featureId;
  }

  public String getFeatureName() {
    return featureName;
  }

  public String getFeatureVersion() {
    return featureVersion;
  }

  public String getFeatureId() {
    return featureId;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("Feature name: " + featureName);
    sb.append('\n');
    sb.append("Feature version: " + featureVersion);
    sb.append('\n');
    sb.append("Feature id: " + featureId);
    return sb.toString();
  }

  public void setProvider(CodeProvider provider) {
    this.provider = provider;
  }

  public CodeProvider getProvider() {
    return provider;
  }
}
