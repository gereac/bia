package com.gcsf.books.model;

import java.util.HashSet;

public class Book {

  private String bookTitle;

  private HashSet<Author> bookAuthors = new HashSet<Author>();

  private String bookCopyrights;

  private String bookISBN13;

  private String bookISBN10;

  private String bookPublisher;

  private String bookDescription;

  private String bookYear;

  public Book() {
    // TODO add the correct book constructor
  }

  public String getBookTitle() {
    return bookTitle;
  }

  public void setBookTitle(String bookTitle) {
    this.bookTitle = bookTitle;
  }

  public HashSet<Author> getBookAuthors() {
    return bookAuthors;
  }

  public void addBookAuthor(Author aAuthor) {
    if (null == aAuthor) {
      throw new IllegalArgumentException("The author cannot be null");
    }
    this.bookAuthors.add(aAuthor);
  }

  public void setBookAuthors(HashSet<Author> bookAuthors) {
    if (null == bookAuthors) {
      throw new IllegalArgumentException("The authros cannot be null");
    }
    this.bookAuthors = bookAuthors;
  }

  public String getBookCopyrights() {
    return bookCopyrights;
  }

  public void setBookCopyrights(String bookCopyrights) {
    this.bookCopyrights = bookCopyrights;
  }

  public String getBookISBN13() {
    return bookISBN13;
  }

  public void setBookISBN13(String bookISBN13) {
    this.bookISBN13 = bookISBN13;
  }

  public String getBookISBN10() {
    return bookISBN10;
  }

  public void setBookISBN10(String bookISBN10) {
    this.bookISBN10 = bookISBN10;
  }

  public String getBookPublisher() {
    return bookPublisher;
  }

  public void setBookPublisher(String bookPublisher) {
    this.bookPublisher = bookPublisher;
  }

  public String getBookDescription() {
    return bookDescription;
  }

  public void setBookDescription(String bookDescription) {
    this.bookDescription = bookDescription;
  }

  public String getBookYear() {
    return bookYear;
  }

  public void setBookYear(String bookYear) {
    this.bookYear = bookYear;
  }

  public String toString() {
    // TODO add a pretty formatter for this
    return this.getBookTitle() + " " + this.getBookISBN10();
  }

  /**
   * an enhanced toString() method that will return the book representation
   * based on the style needed by the caller
   * 
   * @param aStyle
   *          - the style in which the caller needs the book representation
   * @return - the desired book representation
   * */
  public String toString(int aStyle) {
    String retVal = "";
    switch (aStyle) {
      case IBookConstants.MENU_VIEW:
        retVal = this.getBookTitle();
        break;
      default:
        break;
    }

    return retVal;
  }
}
