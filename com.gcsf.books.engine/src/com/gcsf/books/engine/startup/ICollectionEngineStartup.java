package com.gcsf.books.engine.startup;

public interface ICollectionEngineStartup {
  /**
   * @return boolean
   */
  public void doStartup() throws StartupException;

  /**
   * @return boolean
   */
  public void doPostStartup() throws StartupException;
}
