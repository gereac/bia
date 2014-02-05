package com.gcsf.books.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.gcsf.books.engine.logging.LogHelper;
import com.gcsf.books.model.Author;
import com.gcsf.books.model.Book;
import com.gcsf.books.utilities.xml.IXMLUtils;

/*
 * A class designed to support the generation of test demo database
 * **/
public class DemoCollectionGenerator {

  private static final Logger ourLogger = Logger
      .getLogger(DemoCollectionGenerator.class);

  // private Map<String, Book> myBooks = new HashMap<String, Book>();
  //
  // private Map<String, Author> myAuthors = new HashMap<String, Author>();
  //
  // private Date myDate;
  //
  // private long myVersion = -1;

  private static ArgsHandler myArguments = null;

  public static final String ARG_FILE_NAME = "-filename";

  public static final String ARG_PATH = "-path";

  public static final String ARG_VERSION = "-version";

  private static final SimpleDateFormat ourDateFormat = new SimpleDateFormat(
      Utils.DATE_FORMAT);

  /**
   * @param args
   */
  public static void main(String[] aArgs) {
    myArguments = new ArgsHandler(aArgs);
    if (myArguments.hasArgument(ARG_FILE_NAME)
        && myArguments.hasArgument(ARG_PATH)
        && myArguments.hasArgument(ARG_VERSION)) {
      long aVersion = Long.parseLong(myArguments.getArgumentValue(ARG_VERSION));
      saveAll(myArguments.getArgumentValue(ARG_PATH) + "/"
          + myArguments.getArgumentValue(ARG_FILE_NAME), aVersion);
    } else {
      System.exit(-1);
    }
  }

  private static void saveAll(String aFilename, long aVersion) {
    Vector<Author> someAuthors = new Vector<Author>();
    for (int i = 0; i < 100; i++) {
      Author aAuthor = new Author();
      aAuthor.setDisplayName("author display name n" + i);
      aAuthor.setFirstName("Author First Name n" + i);
      aAuthor.setLastName("Author Last Name n" + i);
      aAuthor.setMiddleName("Author Middle Name n" + i);
      someAuthors.add(aAuthor);
    }

    Vector<Book> someBooks = new Vector<Book>();
    for (int i = 0; i < 1000; i++) {
      Book aBook = new Book();
      aBook.setBookTitle("Test Book Title " + i);
      aBook.setBookDescription("Test Book Description " + i);
      HashSet<Author> bookAuthors = new HashSet<Author>();
      // for (int k = 1; k < 100; k++) {
      // Author author = new Author();
      // author
      // .setDisplayName("author display name n" + new Random().nextInt(k));
      // bookAuthors.add(author);
      // }
      Author author = new Author();
      author
          .setDisplayName("author display name n" + new Random().nextInt(100));
      bookAuthors.add(author);
      aBook.setBookAuthors(bookAuthors);
      aBook.setBookPublisher("Test Book Publisher " + i);
      aBook.setBookYear(Integer.toString(2000 - (new Random().nextInt(i + 1))));
      aBook.setBookCopyrights("@Copyright. All rights reserved");
      aBook.setBookISBN10(Integer.toString(1234567890 - i));
      aBook.setBookISBN13("1234567890123");
      someBooks.add(aBook);
    }

    DocumentBuilderFactory builderFactory = DocumentBuilderFactory
        .newInstance();

    try {
      DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element rootElement = doc.createElement(IXMLUtils.ROOT_NODE);
      rootElement.setAttribute(IXMLUtils.XML_ATTRIBUTE_DATE, ourDateFormat
          .format(new Date()));
      rootElement.setAttribute(IXMLUtils.XML_ATTRIBUTE_VERSION, Long
          .toString(aVersion));
      doc.appendChild(rootElement);
      saveAuthorsToXml(doc, rootElement, someAuthors);
      saveBooksToXml(doc, rootElement, someBooks);
      saveXMLDocument(aFilename, doc, aVersion);

    } catch (Exception exception) {
      ourLogger.error("Failed to save book data to file: " + aFilename);
      ourLogger.error(LogHelper.getStackString(exception));
    }
  }

  private static void saveAuthorsToXml(Document aDocument, Element aParent,
      Collection<Author> aAuthors) {

    try {
      Element rootElement = aDocument
          .createElement(IXMLUtils.AUTHORS_ROOT_NODE);
      aParent.appendChild(rootElement);
      for (Author author : aAuthors) {
        Element element = aDocument.createElement(IXMLUtils.AUTHOR_ROOT_NODE);
        element.setAttribute(IXMLUtils.XML_ATTRIBUTE_NAME, author
            .getDisplayName());
        createElement(aDocument, element, IXMLUtils.AUTHOR_FIRSTNAME_NODE,
            author.getFirstName());
        createElement(aDocument, element, IXMLUtils.AUTHOR_MIDDLENAME_NODE,
            author.getMiddleName());
        createElement(aDocument, element, IXMLUtils.AUTHOR_LASTNAME_NODE,
            author.getLastName());
        rootElement.appendChild(element);
      }

    } catch (DOMException exception) {
      ourLogger.error(exception.toString());
      ourLogger.error(LogHelper.getStackString(exception));
    }
  }

  private static void saveBooksToXml(Document aDocument, Element aParent,
      Collection<Book> aBooks) {
    try {
      Element rootElement = aDocument.createElement(IXMLUtils.BOOKS_ROOT_NODE);
      aParent.appendChild(rootElement);
      for (Book book : aBooks) {
        Element element = aDocument.createElement(IXMLUtils.BOOK_ROOT_NODE);
        createElement(aDocument, element, IXMLUtils.BOOK_TITLE_NODE, book
            .getBookTitle());
        createElement(aDocument, element, IXMLUtils.BOOK_SHORTDESC_NODE, book
            .getBookDescription());
        Element authsElement = createElement(aDocument, element,
            IXMLUtils.BOOK_AUTHORS_NODE, "");
        Iterator<Author> authIt = book.getBookAuthors().iterator();
        while (authIt.hasNext()) {
          Author author = (Author) authIt.next();
          Element authElement = createElement(aDocument, authsElement,
              IXMLUtils.AUTHOR_ROOT_NODE, "");
          authElement.setAttribute(IXMLUtils.XML_ATTRIBUTE_NAME, author
              .getDisplayName());
        }
        Element publElement = createElement(aDocument, element,
            IXMLUtils.BOOK_PUBLISHER_NODE, "");
        publElement.setAttribute(IXMLUtils.XML_ATTRIBUTE_PUBLNAME, book
            .getBookPublisher());
        createElement(aDocument, element, IXMLUtils.BOOK_YEAR_NODE, book
            .getBookYear());
        createElement(aDocument, element, IXMLUtils.BOOK_COPYRIGHTS_NODE, book
            .getBookCopyrights());
        createElement(aDocument, element, IXMLUtils.BOOK_ISBN10_NODE, book
            .getBookISBN10());
        createElement(aDocument, element, IXMLUtils.BOOK_ISBN13_NODE, book
            .getBookISBN13());

        rootElement.appendChild(element);
      }

    } catch (DOMException exception) {
      ourLogger.error("Failed to save user data: " + exception.getMessage());
      ourLogger.error(LogHelper.getStackString(exception));
    }

  }

  private static boolean saveXMLDocument(String aFileName, Document aDocument,
      long aVersion) {
    ourLogger.info("Saving XML file... " + aFileName + " (Version: " + aVersion
        + ")");
    // open output stream where XML Document will be saved
    File xmlOutputFile = new File(aFileName);
    FileOutputStream fos;
    Transformer transformer;
    try {
      fos = new FileOutputStream(xmlOutputFile);
    } catch (FileNotFoundException e) {
      ourLogger.debug("Error occured: " + e.getMessage());
      return false;
    }
    // Use a Transformer for output
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    try {
      transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(
          "{http://xml.apache.org/xalan}indent-amount", "4");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    } catch (TransformerConfigurationException e) {
      ourLogger.debug("Transformer configuration error: " + e.getMessage());
      return false;
    }
    DOMSource source = new DOMSource(aDocument);
    StreamResult result = new StreamResult(fos);
    // transform source into result will do save
    try {
      transformer.transform(source, result);
    } catch (TransformerException e) {
      ourLogger.debug("Error transform: " + e.getMessage());
    }
    ourLogger.debug("XML file saved.");
    try {
      fos.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return true;
  }

  private static Element createElement(Document aDoc, Element aParent,
      String aName, String aValue) {
    Element element = aDoc.createElement(aName);
    if (aValue != null) {
      element.setTextContent(aValue);
    }
    aParent.appendChild(element);
    return element;
  }

}
