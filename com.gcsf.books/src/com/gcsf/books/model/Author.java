package com.gcsf.books.model;

/**
 * The class is modelling an author of a Book by providing information about it
 * */
public class Author {

  /** the display name of the author */
  private String displayName;

  /** the first name of the author */
  private String firstName;

  /** the middle name of the author */
  private String middleName;

  /** the last name of the author */
  private String lastName;

  // public Author(String displayName) {
  // this.displayName = displayName;
  // }

  public Author() {
    // TODO Auto-generated constructor stub
  }

  /** getter for the display name */
  public String getDisplayName() {
    return displayName;
  }

  /** setter for the display name */
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /** getter for the first name */
  public String getFirstName() {
    return firstName;
  }

  /** setter for the first name */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /** getter for the middle name */
  public String getMiddleName() {
    return middleName;
  }

  /** setter for the middle name */
  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  /** getter for the last name */
  public String getLastName() {
    return lastName;
  }

  /** setter for the last name */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /** toString implementation for a beautiful display */
  public String toString() {
    return this.displayName + " : " + this.firstName + " " + this.middleName
        + " " + this.lastName;
  }

}
