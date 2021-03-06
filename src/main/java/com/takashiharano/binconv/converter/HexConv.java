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

import com.takashiharano.binconv.IllegalOptionException;
import com.takashiharano.binconv.Option;
import com.takashiharano.binconv.util.BinUtil;
import com.takashiharano.binconv.util.FileUtil;
import com.takashiharano.binconv.util.Log;
import com.takashiharano.binconv.util.StrUtil;

public class HexConv implements Converter {

  public void process(Option option) throws IllegalOptionException {
    try {
      if (option.hasOption("fromhex")) {
        fromHex(option);
      } else if (option.hasOption("tohex")) {
        toHex(option);
      } else {
        throw new IllegalOptionException();
      }
    } catch (Exception ioe) {
      Log.print(ioe);
    }
  }

  private static void fromHex(Option option) throws IOException {
    String text;
    String srcPath = option.get("i");
    if (srcPath == null) {
      text = option.get("fromhex");
    } else {
      if (!FileUtil.exists(srcPath)) {
        Log.print("File not found: " + srcPath);
        return;
      }
      text = FileUtil.readText(srcPath);
    }

    if (text.startsWith("Address")) {
      try {
        text = trimHexText(text);
      } catch (Exception e) {
        Log.print("ERROR: Illegal source format");
        return;
      }
    }

    byte[] b = BinUtil.fromHexString(text);

    String destPath = option.get("o");
    if (destPath == null) {
      String s = new String(b);
      Log.print(s);
    } else {
      FileUtil.write(destPath, b);
      Log.print("OK");
    }
  }

  private static void toHex(Option option) throws IOException {
    byte[] b;

    String srcPath = option.get("i");
    if (srcPath == null) {
      String text = option.get("tohex");
      b = text.getBytes("UTF-8");
    } else {
      if (!FileUtil.exists(srcPath)) {
        Log.print("File not found: " + srcPath);
        return;
      }
      b = FileUtil.read(srcPath);
    }

    String s;
    if (option.hasOption("addr")) {
      boolean ascii = option.hasOption("ascii");
      s = BinUtil.dumpHex(b, 0, 0, true, true, ascii);
    } else {
      int newlinePos = option.getIntValue("newline", 16);
      s = BinUtil.toHexString(b, 0, 0, newlinePos);
    }

    String destPath = option.get("o");
    if (destPath == null) {
      Log.print(s);
    } else {
      FileUtil.write(destPath, s);
      Log.print("OK");
    }
  }

  private static String trimHexText(String s) {
    String[] a = StrUtil.text2array(s);
    StringBuilder sb = new StringBuilder();
    for (int i = 2; i < a.length; i++) {
      String w = a[i];
      w = w.substring(11, 60);
      sb.append(w);
    }
    return sb.toString();
  }

}
