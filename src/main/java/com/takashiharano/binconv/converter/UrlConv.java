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
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.takashiharano.binconv.IllegalOptionException;
import com.takashiharano.binconv.Option;
import com.takashiharano.binconv.util.FileUtil;
import com.takashiharano.binconv.util.Log;

public class UrlConv implements Converter {

  private static final String DEFAULT_CHARSET = "UTF-8";

  public void process(Option option) throws IllegalOptionException {
    try {
      if (option.hasOption("fromurl")) {
        decode(option);
      } else if (option.hasOption("tourl")) {
        encode(option);
      } else {
        throw new IllegalOptionException();
      }
    } catch (Exception e) {
      Log.print(e);
    }
  }

  private static void decode(Option option) throws IOException {
    String src;

    String srcPath = option.get("i");
    if (srcPath == null) {
      src = option.get("fromurl");
    } else {
      if (!FileUtil.exists(srcPath)) {
        Log.print("File not found: " + srcPath);
        return;
      }
      src = FileUtil.readText(srcPath);
    }

    if (src == null) {
      return;
    }

    src = src.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");
    String enc = option.get("enc", DEFAULT_CHARSET);
    String s = URLDecoder.decode(src, enc);

    String destPath = option.get("o");
    if (destPath == null) {
      Log.print(s);
    } else {
      FileUtil.write(destPath, s, enc);
      Log.print("OK");
    }
  }

  private static void encode(Option option) throws IOException {
    String src;
    String enc = option.get("enc", DEFAULT_CHARSET);
    String srcPath = option.get("i");
    if (srcPath == null) {
      src = option.get("tourl");
    } else {
      if (!FileUtil.exists(srcPath)) {
        Log.print("File not found: " + srcPath);
        return;
      }
      src = FileUtil.readText(srcPath, enc);
    }

    if (src == null) {
      Log.print("Error: the source text is null");
      return;
    }

    String s = URLEncoder.encode(src, enc);

    String destPath = option.get("o");
    if (destPath == null) {
      Log.print(s);
    } else {
      FileUtil.write(destPath, s);
      Log.print("OK");
    }
  }

}
