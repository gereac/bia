package com.gcsf.books;

import org.apache.log4j.Logger;

import com.gcsf.books.engine.startup.ICollectionEngineStartup;
import com.gcsf.books.engine.startup.StartupException;

public class BooksStartup implements ICollectionEngineStartup {

  private static final Logger ourLogger = Logger.getLogger(BooksStartup.class);

  @Override
  public void doPostStartup() throws StartupException {
    ourLogger.info("doPostStartup in com.gcsf.books");
    // MemoryCheck.getInstance();
  }

  @Override
  public void doStartup() throws StartupException {
    ourLogger.info("doStartup in com.gcsf.books");
  }

}
