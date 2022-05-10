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
 */
package com.takashiharano.binconv.converter;

import java.io.IOException;
import java.util.Base64;

import com.takashiharano.binconv.IllegalOptionException;
import com.takashiharano.binconv.Option;
import com.takashiharano.binconv.util.FileUtil;
import com.takashiharano.binconv.util.Log;

public class Base64Conv {

  public static void main(Option option) throws IllegalOptionException {
    try {
      if (option.hasOption("frombase64")) {
        decode(option);
      } else if (option.hasOption("tobase64")) {
        encode(option);
      } else {
        throw new IllegalOptionException();
      }
    } catch (Exception e) {
      Log.print(e);
    }
  }

  private static void decode(Option option) throws IOException {
    String b64;

    String srcPath = option.get("i");
    if (srcPath == null) {
      b64 = option.get("frombase64");
    } else {
      if (!FileUtil.exists(srcPath)) {
        Log.print("File not found: " + srcPath);
        return;
      }
      b64 = FileUtil.readText(srcPath);
    }

    if (b64 == null) {
      return;
    }

    b64 = b64.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");
    byte[] content = Base64.getDecoder().decode(b64);

    String destPath = option.get("o");
    if (destPath == null) {
      String s = new String(content);
      Log.print(s);
    } else {
      FileUtil.write(destPath, content);
      Log.print("OK");
    }
  }

  private static void encode(Option option) throws IOException {
    byte[] b;

    String srcPath = option.get("i");
    if (srcPath == null) {
      String text = option.get("tobase64");
      b = text.getBytes("UTF-8");
    } else {
      if (!FileUtil.exists(srcPath)) {
        Log.print("File not found: " + srcPath);
        return;
      }
      b = FileUtil.read(srcPath);
    }

    if (b == null) {
      Log.print("Error: the source byte is null");
      return;
    }

    String b64 = Base64.getEncoder().encodeToString(b);

    String newlinePos = "76";
    String optNewline = option.get("newline");
    if (optNewline != null) {
      newlinePos = optNewline;
    }
    int newlinePosition = Integer.parseInt(newlinePos);
    if (newlinePosition > 0) {
      b64 = insertNewLine(b64, newlinePosition);
    }

    String destPath = option.get("o");
    if (destPath == null) {
      Log.print(b64);
    } else {
      FileUtil.write(destPath, b64);
      Log.print("OK");
    }
  }

  private static String insertNewLine(String str, int pos) {
    StringBuilder sb = new StringBuilder();
    int p = 0;
    while (p < str.length()) {
      int endIndex = p + pos;
      if (endIndex >= str.length()) {
        endIndex = str.length();
      }
      String s = str.substring(p, endIndex);
      sb.append(s);
      sb.append("\r\n");
      p += pos;
    }
    return sb.toString();
  }

}
