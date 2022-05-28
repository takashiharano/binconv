/*
 * The MIT License
 *
 * Copyright 2020-2022 Takashi Harano
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * # This class is a subset of FileUtil on libutil.com.
 */
package com.takashiharano.binconv.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class FileUtil {

  public static final String DEFAULT_CHARSET = "UTF-8";
  public static String LINE_SEPARATOR = "\n";

  /**
   * Check if the given bytes corresponding to the given charset.
   *
   * @param src
   *          string byte array
   * @param charsetName
   *          charset name
   * @return true if the byte array matches the charset
   */
  public static boolean checkCharset(byte[] src, String charsetName) {
    try {
      byte[] tmp = new String(src, charsetName).getBytes(charsetName);
      return Arrays.equals(tmp, src);
    } catch (UnsupportedEncodingException e) {
      return false;
    }
  }

  /**
   * Detects charset and returns its name.
   *
   * @param src
   *          string byte array
   * @return charset name
   */
  public static String getCharsetName(byte[] src) {
    if (src.length >= 3) {
      if ((src[0] == (byte) 0xFE) && (src[1] == (byte) 0xFF)) {
        // big-endian
        return "UTF-16BE";
      } else if ((src[0] == (byte) 0xFF) && (src[1] == (byte) 0xFE)) {
        // little-endian
        return "UTF-16LE";
      }
    }

    String[] CHARSET_NAMES = { "UTF-16", "UTF-8", "EUC_JP", "SJIS" };
    for (int i = 0; i < CHARSET_NAMES.length; i++) {
      String charsetName = CHARSET_NAMES[i];
      if (checkCharset(src, charsetName)) {
        return charsetName;
      }
    }
    return null;
  }

  /**
   * Returns the pathname string of this abstract pathname's parent, or null if
   * this pathname does not name a parent directory. The parent of an abstract
   * pathname consists of the pathname's prefix, if any, and each name in the
   * pathname's name sequence except for the last. If the name sequence is empty
   * then the pathname does not name a parent directory.
   *
   * e.g., "a/b/c.txt" to "/a/b"
   *
   * @param path
   *          file path
   * @return parent path
   */
  public static String getParentPath(String path) {
    File file = new File(path);
    return getParentPath(file);
  }

  /**
   * Returns parent path of the file.<br>
   * e.g., "a/b/c.txt" to "/a/b"
   *
   * @param file
   *          an file object
   * @return parent path
   */
  public static String getParentPath(File file) {
    return file.getParent();
  }

  /**
   * Tests whether the file or directory denoted by this abstract path name
   * exists.
   *
   * @param path
   *          the path of file or directory
   * @return true if and only if the file or directory denoted by this abstract
   *         pathname exists; false otherwise (including the case of null)
   */
  public static boolean exists(String path) {
    if (path == null) {
      return false;
    }
    File file = new File(path);
    return file.exists();
  }

  /**
   * Creates the directory named by this abstract pathname, including any
   * necessary but nonexistent parent directories. Note that if this operation
   * fails it may have succeeded in creating some of the necessary parent
   * directories.
   *
   * @param path
   *          the path of directory
   * @return true if and only if the directory was created,along with all
   *         necessary parent directories; false otherwise
   */
  public static boolean mkdir(String path) {
    File file = new File(path);
    boolean created = file.mkdirs();
    return created;
  }

  /**
   * Creates the parent directories.
   *
   * @param path
   *          the path of directory
   * @return true if and only if the directory was created
   */
  public static boolean mkParentDir(String path) {
    File file = new File(path);
    return mkParentDir(file);
  }

  /**
   * Creates the parent directories.
   *
   * @param file
   *          the target file
   * @return true if and only if the directory was created
   */
  public static boolean mkParentDir(File file) {
    boolean created = false;
    String parent = getParentPath(file);
    if ((parent != null) && !exists(parent)) {
      created = mkdir(parent);
    }
    return created;
  }

  /**
   * Read a binary file.
   *
   * @param path
   *          file path
   * @return byte array of the file content. Returns null if the file does not
   *         exist, or in case of a read error.
   */
  public static byte[] read(String path) {
    File file = new File(path);
    return read(file);
  }

  /**
   * Read a binary file.
   *
   * @param file
   *          the file object
   * @return byte array of the file content. Returns null if the file does not
   *         exist, or in case of a read error.
   */
  public static byte[] read(File file) {
    if (!file.exists()) {
      return null;
    }
    byte[] content = new byte[(int) file.length()];
    try (FileInputStream fis = new FileInputStream(file)) {
      @SuppressWarnings("unused")
      int readSize = fis.read(content, 0, content.length);
    } catch (IOException ioe) {
      content = null;
    }
    return content;
  }

  /**
   * Read a text file.
   *
   * @param path
   *          file path
   * @return text content. Returns null if the file does not exist, or in case of
   *         a read error.
   */
  public static String readText(String path) {
    return readText(path, DEFAULT_CHARSET);
  }

  /**
   * Read a text file.
   *
   * @param file
   *          the file object to read
   * @return text content. Returns null if the file does not exist, or in case of
   *         a read error.
   */
  public static String readText(File file) {
    return readText(file, DEFAULT_CHARSET);
  }

  /**
   * Read a text file.
   *
   * @param path
   *          file path to read
   * @param charsetName
   *          charset name. set null to auto detection
   * @return text content. Returns null if the file does not exist, or in case of
   *         a read error.
   */
  public static String readText(String path, String charsetName) {
    File file = new File(path);
    return readText(file, charsetName);
  }

  /**
   * Read a text file.
   *
   * @param file
   *          file object to read
   * @param charsetName
   *          charset name. set null to auto detection
   * @return text content. Returns null if the file does not exist, or in case of
   *         a read error.
   */
  public static String readText(File file, String charsetName) {
    byte[] content = read(file);
    if (content == null) {
      return null;
    }
    if (charsetName == null) {
      charsetName = getCharsetName(content);
      if (charsetName == null) {
        charsetName = DEFAULT_CHARSET;
      }
    }
    String text;
    try {
      text = new String(content, charsetName);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return text;
  }

  /**
   * Write a content using a byte array into a file.
   *
   * @param path
   *          file path
   * @param content
   *          the content to write
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void write(String path, byte[] content) throws IOException {
    File file = new File(path);
    write(file, content);
  }

  /**
   * Write a content using a byte array into a file.
   *
   * @param file
   *          the file object
   * @param content
   *          the content to write
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void write(File file, byte[] content) throws IOException {
    mkParentDir(file);
    try (FileOutputStream fos = new FileOutputStream(file); BufferedOutputStream bos = new BufferedOutputStream(fos)) {
      bos.write(content, 0, content.length);
      bos.flush();
    } catch (IOException e) {
      throw e;
    }
  }

  /**
   * Write a text into a file.
   *
   * @param path
   *          file path
   * @param content
   *          the string content to write
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void write(String path, String content) throws IOException {
    write(path, content, DEFAULT_CHARSET);
  }

  /**
   * Write a text into a file.
   *
   * @param path
   *          file path
   * @param content
   *          the string content to write
   * @param charsetName
   *          charset name
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void write(String path, String content, String charsetName) throws IOException {
    File file = new File(path);
    write(file, content, charsetName);
  }

  /**
   * Write a text into a file.
   *
   * @param file
   *          the file object
   * @param content
   *          the string content to write
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void write(File file, String content) throws IOException {
    mkParentDir(file);
    write(file, content, DEFAULT_CHARSET);
  }

  /**
   * Write a text into a file.
   * 
   * @param file
   *          the file object
   * @param content
   *          the string content to write
   * @param charsetName
   *          charset name. e.g., "UTF-8", "Shift_JIS"
   * @throws IOException
   *           If an I/O error occurs
   */
  public static void write(File file, String content, String charsetName) throws IOException {
    mkParentDir(file);
    try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charsetName))) {
      bw.write(content);
    } catch (IOException e) {
      throw e;
    }
  }

}
