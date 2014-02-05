package com.gcsf.books.gui.bundles.model;

public class CodePlugin {
  private CodeFeature feature;

  String pluginName;

  String pluginVersion;

  String pluginId;

  public CodePlugin(CodeFeature feature, String pluginName,
      String pluginVersion, String pluginId) {
    this.setFeature(feature);
    this.pluginName = pluginName;
    this.pluginVersion = pluginVersion;
    this.pluginId = pluginId;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("Plugin name: " + pluginName);
    sb.append('\n');
    sb.append("Plugin version: " + pluginVersion);
    sb.append('\n');
    sb.append("Plugin id: " + pluginId);
    return sb.toString();
  }

  public String getPluginName() {
    return pluginName;
  }

  public String getPluginVersion() {
    return pluginVersion;
  }

  public String getPluginId() {
    return pluginId;
  }

  public void setFeature(CodeFeature feature) {
    this.feature = feature;
  }

  public CodeFeature getFeature() {
    return feature;
  }
}
