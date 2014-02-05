package com.gcsf.books.utilities.xml;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

/**
 * Namespace of user database xml file. 
 */
public class NameSpace implements NamespaceContext {

  /**
   * @param aPrefix Prefix
   * @return Return namespace URI
   * 
   * @see javax.xml.namespace.NamespaceContext#getNamespaceURI(java.lang.String)
   */
  public String getNamespaceURI(String aPrefix) {
    return "com.gcsf.books";
  }

  /**
   * @param aNamespaceURI
   *          Namespace.
   * @return Return prefix.
   * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
   */
  public String getPrefix(String aNamespaceURI) {
    return null;
  }

  /**
   * @param aNamespaceURI
   *          Namespace
   * @return Return prefix iterator.
   * @see javax.xml.namespace.NamespaceContext#getPrefixes(java.lang.String)
   */
  @SuppressWarnings("unchecked")
  public Iterator getPrefixes(String aNamespaceURI) {
    return null;
  }
  
}
