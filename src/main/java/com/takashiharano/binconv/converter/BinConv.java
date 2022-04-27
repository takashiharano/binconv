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

public class BinConv {

  public static void main(Option option) throws IllegalOptionException {
    try {
      if (option.hasOption("frombin")) {
        frombBin(option);
      } else if (option.hasOption("tobin")) {
        toBin(option);
      } else {
        throw new IllegalOptionException();
      }
    } catch (IOException ioe) {
      Log.print(ioe);
    }
  }

  private static void frombBin(Option option) throws IOException {
    String text;

    String srcPath = option.get("i");
    if (srcPath == null) {
      text = option.get("frombin");
    } else {
      if (!FileUtil.exists(srcPath)) {
        Log.print("File not found: " + srcPath);
        return;
      }
      text = FileUtil.readText(srcPath);
    }

    byte[] b = BinUtil.fromBinString(text);

    String destPath = option.get("o");
    if (destPath == null) {
      String s = new String(b);
      Log.print(s);
    } else {
      FileUtil.write(destPath, b);
      Log.print("OK");
    }
  }

  private static void toBin(Option option) throws IOException {
    byte[] b;

    String srcPath = option.get("i");
    if (srcPath == null) {
      String text = option.get("tobin");
      b = text.getBytes("UTF-8");
    } else {
      if (!FileUtil.exists(srcPath)) {
        Log.print("File not found: " + srcPath);
        return;
      }
      b = FileUtil.read(srcPath);
    }

    String newlinePos = "16";
    String optNewline = option.get("newline");
    if (optNewline != null) {
      newlinePos = optNewline;
    }
    int newlinePosition = Integer.parseInt(newlinePos);

    String s = BinUtil.toBinString(b, 0, 0, newlinePosition);

    String destPath = option.get("o");
    if (destPath == null) {
      Log.print(s);
    } else {
      FileUtil.write(destPath, s);
      Log.print("OK");
    }
  }

}
