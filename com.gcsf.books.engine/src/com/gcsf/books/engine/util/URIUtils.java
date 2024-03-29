/*   **********************************************************************  **
 **   Copyright notice                                                       **
 **                                                                          **
 **   (c) 2005-2009 RSSOwl Development Team                                  **
 **   http://www.rssowl.org/                                                 **
 **                                                                          **
 **   All rights reserved                                                    **
 **                                                                          **
 **   This program and the accompanying materials are made available under   **
 **   the terms of the Eclipse Public License v1.0 which accompanies this    **
 **   distribution, and is available at:                                     **
 **   http://www.rssowl.org/legal/epl-v10.html                               **
 **                                                                          **
 **   A copy is found in the file epl-v10.html and important notices to the  **
 **   license from the team is found in the textfile LICENSE.txt distributed **
 **   in this package.                                                       **
 **                                                                          **
 **   This copyright notice MUST APPEAR in all copies of the file!           **
 **                                                                          **
 **   Contributors:                                                          **
 **     RSSOwl Development Team - initial API and implementation             **
 **                                                                          **
 **  **********************************************************************  */

package com.gcsf.books.engine.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import com.gcsf.books.engine.EngineActivator;

/**
 * Utility Class for working with Links.
 * 
 * @author bpasero
 */
public class URIUtils {

  /** URL of Blank Website */
  public static final String ABOUT_BLANK = "about:blank"; //$NON-NLS-1$

  /* Default Encoding */
  private static final String DEFAULT_ENCODING = "UTF-8"; //$NON-NLS-1$

  /** Common Newsfeed Extensions */
  private static final String[] FEED_EXTENSIONS = new String[] {
      "rss", "rdf", "xml", "atom", "feed" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

  /* Used when encoding a URL in a fast way */
  private static final String[] CHARS_TO_ENCODE = new String[] {
      " ", "[", "]", "{", "}", "|", "^", "\\", "<", ">" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$

  private static final String[] ENCODED_CHARS = new String[] {
      "%20", "%5B", "%5D", "%7B", "%7D", "%7C", "%5E", "%5C", "%3C", "%3E" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$

  /** The HTTP Protocol */
  public static final String HTTP = "http://"; //$NON-NLS-1$

  /** Identifier for a Protocol */
  public static final String PROTOCOL_IDENTIFIER = "://"; //$NON-NLS-1$

  /* This utility class constructor is hidden */
  private URIUtils() {
    // Protect default constructor
  }

  /**
   * Will create a new {@link URI} out of the given one that only contains the
   * Scheme and Host part.
   * 
   * @param link
   *          The link to normalize.
   * @return the normalized link.
   */
  public static URI normalizeUri(URI link) {
    return normalizeUri(link, false);
  }

  /**
   * Will create a new {@link URI} out of the given one that only contains the
   * Scheme and Host part. If <code>withPort</code> is set to TRUE, the port
   * will be part of the normalized URI too.
   * 
   * @param link
   *          The link to normalize.
   * @param withPort
   *          If set to <code>TRUE</code>, include the port in the normalized
   *          URI.
   * @return the normalized link.
   */
  public static URI normalizeUri(URI link, boolean withPort) {
    try {
      if (withPort)
        return new URI(link.getScheme(), null, link.getHost(), link.getPort(),
            null, null, null);
      return new URI(link.getScheme(), link.getHost(), null, null);
    } catch (URISyntaxException e) {
      EngineActivator.getDefault().logError(e.getMessage(), e);
    }

    return link;
  }

  /**
   * @param base
   *          the base {@link URI} to resolve against.
   * @param relative
   *          the relative {@link URI} to resolve.
   * @return a resolved {@link URI} that is absolute.
   * @throws URISyntaxException
   *           in case of an error while resolving.
   */
  public static URI resolve(URI base, URI relative) throws URISyntaxException {
    if (relative.isAbsolute())
      return relative;

    /* Resolve against Host */
    if (relative.toString().startsWith("/")) { //$NON-NLS-1$
      base = normalizeUri(base, true);
      return base.resolve(relative);
    }

    /* Resolve against Given Base */
    if (base.toString().endsWith("/")) //$NON-NLS-1$
      return base.resolve(relative);

    /* Resolve against Given Base By Appending Leading Slash */
    return new URI(base.toString() + "/").resolve(relative.toString()); //$NON-NLS-1$
  }

  /**
   * Return TRUE in case the given String looks like a Link to a Feed.
   * 
   * @param str
   *          The String to check
   * @return TRUE in case the String looks like a Link to a Feed.
   */
  public static boolean looksLikeFeedLink(String str) {
    return looksLikeFeedLink(str, true);
  }

  /**
   * Return TRUE in case the given String looks like a Link to a Feed.
   * 
   * @param str
   *          The String to check
   * @param strict
   *          if <code>true</code> require the given String to contain one of
   *          the feed extensions with a leading ".", <code>false</code>
   *          otherwise.
   * @return TRUE in case the String looks like a Link to a Feed.
   */
  public static boolean looksLikeFeedLink(String str, boolean strict) {
    if (!looksLikeLink(str))
      return false;

    for (String extension : FEED_EXTENSIONS) {
      if (strict && str.contains("." + extension)) //$NON-NLS-1$
        return true;
      else if (!strict && str.contains(extension))
        return true;
    }

    return false;
  }

  /**
   * Return TRUE in case the given String looks like a Link.
   * 
   * @param str
   *          The String to check
   * @return TRUE in case the String looks like a Link.
   */
  public static boolean looksLikeLink(String str) {

    /* Is empty or null? */
    if (!StringUtils.isSet(str))
      return false;

    /* Contains whitespaces ? */
    if (str.indexOf(' ') >= 0)
      return false;

    /* RegEx Link check */
    if (RegExUtils.isValidURL(str))
      return true;

    /* Try creating an URL object */
    try {
      new URL(str);
    } catch (MalformedURLException e) {
      return false;
    }

    /* String is an URL */
    return true;
  }

  /**
   * URLEncode the given String. Note that URLEncoder uses "+" to display any
   * spaces. But we need "%20", so we'll replace all "+" with "%20". This method
   * is used to create a "mailto:" URL that is handled by a mail application.
   * The String is HTML Encoded if the user has set so.
   * 
   * @param str
   *          String to encode
   * @return String encoded String
   */
  public static String mailToUrllEncode(String str) {
    return urlEncode(str).replaceAll("\\+", "%20"); //$NON-NLS-1$ //$NON-NLS-2$
  }

  /**
   * This is a simple wrapper method for the encode() Method of the URLEncoder.
   * UTF-8 is used for encoding.
   * 
   * @param str
   *          String to encode
   * @return the URL Encoded String
   */
  public static String urlEncode(String str) {

    /* Try Default encoding */
    try {
      return URLEncoder.encode(str, DEFAULT_ENCODING);
    }

    /* Return in this case */
    catch (UnsupportedEncodingException e1) {
      return str;
    }
  }

  /**
   * This is a simple wrapper method for the decode() Method of the URLDecoder.
   * UTF-8 is used for encoding.
   * 
   * @param str
   *          String to decode
   * @return the URL Decoded String
   */
  public static String urlDecode(String str) {

    /* Try Default encoding */
    try {
      return URLDecoder.decode(str, DEFAULT_ENCODING);
    }

    /* Return in this case */
    catch (UnsupportedEncodingException e1) {
      return str;
    }
  }

  /**
   * Try to create an URI from the given String. The String is preprocessed to
   * work around some bugs in the implementation of Java's equals() for URIs:
   * <p>
   * <li>remove leading and trailing whitespaces</li>
   * <li>encode invalid URI Characters</li>
   * </p>
   * 
   * @param str
   *          The String to interpret as URI.
   * @return The URI or NULL in case of the String does not match the URI
   *         Syntax.
   */
  public static URI createURI(String str) {
    if (str == null)
      return null;

    try {

      /* Remove surrounding whitespaces */
      str = str.trim();

      /* Encode invalid URI Characters */
      str = fastEncode(str);

      return new URI(str);
    } catch (URISyntaxException e) {
      return null;
    }
  }

  /**
   * Returns a new <code>URI</code> from the given one, that potentially points
   * to the favicon.ico.
   * 
   * @param link
   *          The Link to look for a favicon.
   * @param rewriteHost
   *          If <code>TRUE</code>, change the host for a better result.
   * @return Returns the <code>URI</code> from the given one, that potentially
   *         points to the favicon.ico.
   * @throws URISyntaxException
   *           In case of a malformed URI.
   */
  public static URI toFaviconUrl(URI link, boolean rewriteHost)
    throws URISyntaxException {
    String host = link.getHost();

    if (!StringUtils.isSet(host))
      return null;

    /* Strip all but the last two segments from the Host */
    if (rewriteHost) {
      String[] hostSegments = host.split("\\."); //$NON-NLS-1$
      int len = hostSegments.length;

      /* Rewrite if conditions match */
      if (len > 2 && !"www".equals(hostSegments[0])) //$NON-NLS-1$
        host = hostSegments[len - 2] + "." + hostSegments[len - 1]; //$NON-NLS-1$

      /* Rewrite failed, avoid reloading by throwing an exception */
      else
        throw new URISyntaxException("", ""); //$NON-NLS-1$ //$NON-NLS-2$
    }

    StringBuilder buf = new StringBuilder();
    buf.append(HTTP);
    buf.append(host);
    buf.append("/favicon.ico"); //$NON-NLS-1$

    return new URI(fastEncode(buf.toString()));
  }

  /**
   * @param link
   *          the absolute link to convert to a top level URI (e.g.
   *          http://www.rssowl.org/feed.xml becomes http://www.rssowl.org).
   * @return the top level URL or <code>null</code> if the link is not
   *         convertable.
   * @throws URISyntaxException
   *           in case of any error converting the link to a top level link.
   */
  public static URI toTopLevel(URI link) throws URISyntaxException {
    if (link == null)
      return null;

    String host = link.getHost();
    if (!StringUtils.isSet(host))
      return null;

    return new URI(HTTP + host);
  }

  /**
   * Try to get the File Name of the given URI.
   * 
   * @param uri
   *          The URI to parse the File from.
   * @param extension
   *          the file extension or <code>null</code> if unknown.
   * @return String The File Name or the URI in external Form.
   */
  public static String getFile(URI uri, String extension) {

    /* Fallback if Extension not set */
    if (!StringUtils.isSet(extension))
      return getFile(uri);

    /* Prefix Extension if necessary */
    if (!extension.startsWith(".")) //$NON-NLS-1$
      extension = "." + extension; //$NON-NLS-1$

    /* Obtain Filename Candidates from Query and Path */
    String fileQuerySegment = getFileSegmentFromQuery(uri.getQuery(), extension);
    String lastPathSegment = getLastSegmentFromPath(uri.getPath());

    /* Favour Query over Path if Extension part of it */
    if (StringUtils.isSet(fileQuerySegment)
        && fileQuerySegment.contains(extension))
      return urlDecode(fileQuerySegment);

    /* Use Path if Extension part of it */
    if (StringUtils.isSet(lastPathSegment)
        && lastPathSegment.contains(extension))
      return urlDecode(lastPathSegment);

    /* Favour Path over Query otherwise */
    if (StringUtils.isSet(lastPathSegment))
      return urlDecode(lastPathSegment);

    /* Use Query as Fallback */
    if (StringUtils.isSet(fileQuerySegment))
      return urlDecode(fileQuerySegment);

    return uri.toASCIIString();
  }

  private static String getLastSegmentFromPath(String path) {
    if (StringUtils.isSet(path)) {
      String parts[] = path.split("/"); //$NON-NLS-1$
      if (parts.length > 0 && StringUtils.isSet(parts[parts.length - 1]))
        return parts[parts.length - 1];
    }

    return null;
  }

  private static String getFileSegmentFromQuery(String query, String extension) {
    if (StringUtils.isSet(query)) {
      StringTokenizer tokenizer = new StringTokenizer(query, "&?=/"); //$NON-NLS-1$
      List<String> tokens = new ArrayList<String>();
      while (tokenizer.hasMoreTokens())
        tokens.add(tokenizer.nextToken());

      Collections.reverse(tokens);

      for (String token : tokens) {
        if (token.contains(extension))
          return token;
      }
    }

    return null;
  }

  private static String getFile(URI uri) {
    String file = uri.getPath();
    if (StringUtils.isSet(file)) {
      String parts[] = file.split("/"); //$NON-NLS-1$
      if (parts.length > 0 && StringUtils.isSet(parts[parts.length - 1]))
        return urlDecode(parts[parts.length - 1]);
    }
    return uri.toASCIIString();
  }

  /**
   * @param url
   *          the link to encode.
   * @return the encoded link.
   */
  public static String fastEncode(String url) {
    for (int i = 0; i < CHARS_TO_ENCODE.length; i++) {
      if (url.contains(CHARS_TO_ENCODE[i]))
        url = StringUtils.replaceAll(url, CHARS_TO_ENCODE[i], ENCODED_CHARS[i]);
    }

    return url;
  }

  /**
   * @param url
   *          the link to decode.
   * @return the decoded link.
   */
  public static String fastDecode(String url) {
    for (int i = 0; i < ENCODED_CHARS.length; i++) {
      if (url.contains(ENCODED_CHARS[i]))
        url = StringUtils.replaceAll(url, ENCODED_CHARS[i], CHARS_TO_ENCODE[i]);
    }

    return url;
  }

  /**
   * @param value
   *          the input value (either a link or phrase).
   * @return the value as is if it is a link or a search url for the phrase.
   */
  public static String getLink(String value) {
    if (!StringUtils.isSet(value))
      return value;

    if (value.contains(":") || value.contains("/")) //$NON-NLS-1$ //$NON-NLS-2$
      return value;

    if (value.contains(" ") || !value.contains(".")) //$NON-NLS-1$ //$NON-NLS-2$
      return "http://www.google.com/search?q=" + urlEncode(value); //$NON-NLS-1$

    return value;
  }

  /**
   * @param link
   *          the String to ensure that it begins with a protocol.
   * @return the same String if it begins with a protocol, or a String where the
   *         http-protocol was appended to the beginning.
   */
  public static String ensureProtocol(String link) {
    if (link != null && !link.contains(PROTOCOL_IDENTIFIER))
      return HTTP + link;

    return link;
  }
}