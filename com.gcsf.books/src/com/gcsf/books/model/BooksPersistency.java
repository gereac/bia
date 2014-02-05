package com.gcsf.books.model;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gcsf.books.engine.logging.LogHelper;
import com.gcsf.books.utilities.FileUtils;
import com.gcsf.books.utilities.PathResolver;
import com.gcsf.books.utilities.Utils;
import com.gcsf.books.utilities.logging.BIALogger;
import com.gcsf.books.utilities.xml.IXMLUtils;
import com.gcsf.books.utilities.xml.NameSpace;

public class BooksPersistency {

  private static final Logger ourLogger = Logger
      .getLogger(BooksPersistency.class);

  private Date myDate;

  private long myVersion = -1;

  private int myBooksNumber = 0;

  private final String myCollectionFile = "books.xml";

  private static BooksPersistency ourInstance = null;

  private static final SimpleDateFormat ourDateFormat = new SimpleDateFormat(
      Utils.DATE_FORMAT);

  private Map<String, Book> myBooks = new HashMap<String, Book>();

  private Map<String, Author> myAuthors = new HashMap<String, Author>();

  /**
   * private constructor
   * */
  private BooksPersistency() {
    // nothing to do yet
  }

  /**
   * accessor for the singleton
   * */
  public static synchronized BooksPersistency getInstance() {
    if (ourInstance == null) {
      ourInstance = new BooksPersistency();
      ourInstance.init();
    }
    return ourInstance;
  }

  private void init() {
    // TODO execute this only when not started from Eclipse IDE
    // checkCollection();

    // TODO read the information from the xml data file
    loadDataFromXMLFile(myCollectionFile);
  }

  /**
   * checks for the collection folder and collection file
   * */
  // TODO complete the method with all the code
  // private void checkCollection(){
  // File collectionFolder = new File("collection");
  // if(collectionFolder.exists()) {
  // // the file or folder exists ... check whether is a file or folder
  // }
  // else {
  // // the file or folder does not exist ... we try to create it
  // if(collectionFolder.mkdir()) {
  // // the folder was created ... we will create also the collection file
  // }
  // else {
  // // TODO add a better handling here ... how to inform the user ??
  // System.exit(-1);
  // }
  // }
  // }
  /**
   * @return - the number of the books in the collection
   * */
  public int getBooksNo() {
    return myBooksNumber;
  }

  public Collection<Book> getBooks() {
    return myBooks.values();
  }

  public Collection<Author> getAuthors() {
    return myAuthors.values();
  }

  private void loadDataFromXMLFile(String aFilename) {
    // File file = new File(aFilename);
    File file = null;
    // String envProp = System.getenv("collectionfile");
    // if (envProp != null && envProp != "") {
    // file = new File(envProp);
    // } else {
    // file = new File("data", aFilename);
    // }
    file = new File(PathResolver.getInstance().getCollectionFolder(), aFilename);
    // File file = new File();
    // TODO implement a better check here and complete the else part of the
    // above if
    if (!file.exists()) {
      ourLogger.error("file " + file.getAbsolutePath() + " does not exists");
      System.exit(-1);
    } else {
      try {
        ourLogger.info("BooksPersistency: load books from file: " + aFilename);
        Element root = FileUtils.getXmlRoot(file);

        root.getAttribute(IXMLUtils.XML_ATTRIBUTE_DATE);
        try {
          myDate = ourDateFormat.parse(root
              .getAttribute(IXMLUtils.XML_ATTRIBUTE_DATE));
        } catch (ParseException exception) {
          ourLogger.error("Failed to parse date of books file");
        }
        myVersion = Long.parseLong(root
            .getAttribute(IXMLUtils.XML_ATTRIBUTE_VERSION));

        System.out.println(myVersion + " || " + myDate);

        myAuthors = loadAuthorsFromXml(root);
        for (Author author : myAuthors.values()) {
          ourLogger.debug(author.toString());
          System.out.println(author.toString());
        }

        myBooks = loadBooksFromXml(root);
        for (Book book : myBooks.values()) {
          ourLogger.debug("Book: " + book.toString());
          System.out.println("Book: " + book.toString());
        }

      } catch (Exception exception) {
        ourLogger.error("Failed load books database attributes: " + exception);
        ourLogger.error(LogHelper.getStackString(exception));
      }
    }
  }

  private Map<String, Author> loadAuthorsFromXml(Element aRootElement) {
    Map<String, Author> authors = new HashMap<String, Author>();
    try {

      XPath xpath = XPathFactory.newInstance().newXPath();
      xpath.setNamespaceContext(new NameSpace());

      NodeList nodes = (NodeList) xpath.evaluate("/" + IXMLUtils.ROOT_NODE
          + "/" + IXMLUtils.AUTHORS_ROOT_NODE + "/*", aRootElement,
          XPathConstants.NODESET);

      for (int i = 0; i < nodes.getLength(); i++) {
        Node authorNode = nodes.item(i);
        Node authorname = authorNode.getAttributes().getNamedItem(
            IXMLUtils.XML_ATTRIBUTE_NAME);
        if (authorname != null) {
          Author author = parseAuthor(authorNode);
          authors.put(author.getDisplayName(), author);
        }
      }
    } catch (Exception exception) {
      ourLogger.error("Failed load authors. " + exception);
      ourLogger.error(LogHelper.getStackString(exception));
    }
    return authors;
  }

  private Author parseAuthor(Node aAuthorNode) {
    Node authorNode = aAuthorNode.getAttributes().getNamedItem(
        IXMLUtils.XML_ATTRIBUTE_NAME);
    String authorname = authorNode.getTextContent();
    Author author = new Author();
    author.setDisplayName(authorname);
    ourLogger.debug("Setting attribute for author <" + authorname + ">");
    NodeList childList = aAuthorNode.getChildNodes();
    for (int childIndex = 0; childIndex < childList.getLength(); childIndex++) {
      Node child = childList.item(childIndex);
      if (child.getNodeType() == Node.ELEMENT_NODE) {
        String name = child.getNodeName();
        if (name.equals(IXMLUtils.AUTHOR_FIRSTNAME_NODE)) {
          String firstName = child.getTextContent();
          if (firstName != null && !firstName.equals("")) {
            author.setFirstName(firstName);
          } else {
            author.setFirstName("");
          }
        }
        if (name.equals(IXMLUtils.AUTHOR_MIDDLENAME_NODE)) {
          String middleName = child.getTextContent();
          if (middleName != null && !middleName.equals("")) {
            author.setMiddleName(middleName);
          } else {
            author.setMiddleName("");
          }
        }
        if (name.equals(IXMLUtils.AUTHOR_LASTNAME_NODE)) {
          String lastName = child.getTextContent();
          if (lastName != null && !lastName.equals("")) {
            author.setLastName(lastName);
          } else {
            author.setLastName("");
          }
        }
      }
    }
    return author;
  }

  private Map<String, Book> loadBooksFromXml(Element aRootElement) {
    Map<String, Book> books = new HashMap<String, Book>();
    try {

      XPath xpath = XPathFactory.newInstance().newXPath();
      xpath.setNamespaceContext(new NameSpace());

      NodeList bookNodes = (NodeList) xpath.evaluate("/" + IXMLUtils.ROOT_NODE
          + "/" + IXMLUtils.BOOKS_ROOT_NODE + "/*", aRootElement,
          XPathConstants.NODESET);

      for (int i = 0; i < bookNodes.getLength(); i++) {
        Node bookNode = bookNodes.item(i);
        Node nodeName = bookNode.getFirstChild();
        // TODO add a better check here
        if (nodeName != null) {
          Book book = parseBook(bookNode);
          books.put(book.getBookYear(), book);
          // books.put(book.getBookISBN10(), book);
          myBooksNumber++;
        }
      }
    } catch (Exception exception) {
      ourLogger.error("Failed load book database attributes: " + exception);
      ourLogger.error(LogHelper.getStackString(exception));
    }
    return books;
  }

  private Book parseBook(Node aBookNode) {

    Book book = new Book();
    NodeList childList = aBookNode.getChildNodes();
    for (int childIndex = 0; childIndex < childList.getLength(); childIndex++) {
      Node child = childList.item(childIndex);
      if (child.getNodeType() == Node.ELEMENT_NODE) {
        String name = child.getNodeName();
        if (name.equals(IXMLUtils.BOOK_TITLE_NODE)) {
          String bookTitle = child.getTextContent();
          if (bookTitle != null && !bookTitle.equals("")) {
            book.setBookTitle(bookTitle);
          } else {
            // TODO an exception should be thrown here ... the title cannot be
            // empty
            // make the schema file for the xml to enforce this
            book.setBookTitle("");
          }
        }
        if (name.equals(IXMLUtils.BOOK_SHORTDESC_NODE)) {
          String bookDescription = child.getTextContent();
          if (bookDescription != null && !bookDescription.equals("")) {
            book.setBookDescription(bookDescription);
          } else {
            book.setBookDescription("");
          }
        }
        if (name.equals(IXMLUtils.BOOK_AUTHORS_NODE)) {
          NodeList authList = child.getChildNodes();
          for (int childAuthIndex = 0; childAuthIndex < authList.getLength(); childAuthIndex++) {
            Node authChild = authList.item(childAuthIndex);
            if (authChild.getNodeType() == Node.ELEMENT_NODE) {
              String authName = authChild.getNodeName();
              if (authName.equals(IXMLUtils.AUTHOR_ROOT_NODE)) {
                Node authorNode = authChild.getAttributes().getNamedItem(
                    IXMLUtils.XML_ATTRIBUTE_NAME);
                String authorname = authorNode.getTextContent();
                Author author = new Author();
                author.setDisplayName(authorname);
                author.setFirstName("");
                author.setMiddleName("");
                author.setLastName("");
                book.addBookAuthor(author);
                ourLogger.debug("Setting attribute for author <" + authorname
                    + ">");
              }
            }
          }
        }
        if (name.equals(IXMLUtils.BOOK_PUBLISHER_NODE)) {
          NamedNodeMap childAttrs = child.getAttributes();
          if (childAttrs != null) {
            Node publAttrs = childAttrs
                .getNamedItem(IXMLUtils.XML_ATTRIBUTE_PUBLNAME);
            if (publAttrs != null) {
              String bookPublisher = publAttrs.getTextContent();
              if (bookPublisher != null && !bookPublisher.equals("")) {
                book.setBookPublisher(bookPublisher);
              } else {
                book.setBookPublisher("");
              }
            }
          } else {
            book.setBookPublisher("");
          }
        }
        if (name.equals(IXMLUtils.BOOK_YEAR_NODE)) {
          String bookYear = child.getTextContent();
          if (bookYear != null && !bookYear.equals("")) {
            book.setBookYear(bookYear);
          } else {
            book.setBookYear("");
          }
        }
        if (name.equals(IXMLUtils.BOOK_COPYRIGHTS_NODE)) {
          String bookCopyrights = child.getTextContent();
          if (bookCopyrights != null && !bookCopyrights.equals("")) {
            book.setBookCopyrights(bookCopyrights);
          } else {
            book.setBookCopyrights("");
          }
        }
        if (name.equals(IXMLUtils.BOOK_ISBN10_NODE)) {
          String bookISBN10 = child.getTextContent();
          if (bookISBN10 != null && !bookISBN10.equals("")) {
            book.setBookISBN10(bookISBN10);
          } else {
            book.setBookISBN10("");
          }
        }
        if (name.equals(IXMLUtils.BOOK_ISBN13_NODE)) {
          String bookISBN13 = child.getTextContent();
          if (bookISBN13 != null && !bookISBN13.equals("")) {
            book.setBookISBN13(bookISBN13);
          } else {
            book.setBookISBN13("");
          }
        }
      }
    }
    return book;
  }

  /**
   * main method ... used so far for testings
   * */
  public static void main(String[] args) {
    BIALogger.initialize();
    getInstance();
  }

}
