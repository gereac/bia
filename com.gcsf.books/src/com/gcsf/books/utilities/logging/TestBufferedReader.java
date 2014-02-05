package com.gcsf.books.utilities.logging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestBufferedReader {

  /**
   * @param args
   */
  public static void main(String[] args) {
    File file = new File("a.txt");
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(
          new FileInputStream(file)));
      String line;
      while ((line = br.readLine()) != null)
        System.out.println(line);
      br.close();
    } catch (IOException e) {
      System.out.println(e);
    }

  }
}
