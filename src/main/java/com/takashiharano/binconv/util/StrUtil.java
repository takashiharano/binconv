/*
 * The MIT License
 *
 * Copyright 2022 Takashi Harano
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
 * # This class is a subset of StrUtil on libutil.com.
 */
package com.takashiharano.binconv.util;

public class StrUtil {

  /**
   * Split the string by line separator.<br>
   * "aaa\nbbb\nccc" to ["aaa", "bbb", "ccc"]
   * 
   * @param src
   *          the string to split
   * @return the split array of strings
   */
  public static String[] text2array(String src) {
    String text = convertNewLine(src, "\n");
    String[] arr = text.split("\n", -1);
    if ((arr.length >= 2) && (arr[arr.length - 1].equals(""))) {
      String[] tmp = new String[arr.length - 1];
      for (int i = 0; i < arr.length - 1; i++) {
        tmp[i] = arr[i];
      }
      arr = tmp;
    }
    return arr;
  }

  /**
   * Convert newline control character.
   *
   * @param src
   *          the source string
   * @param newLine
   *          new line code
   * @return the converted string
   */
  public static String convertNewLine(String src, String newLine) {
    return src.replaceAll("\r\n", "\n").replaceAll("\r", "\n").replaceAll("\n", newLine);
  }

}
