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
package com.takashiharano.binconv;

import com.takashiharano.binconv.converter.Converter;
import com.takashiharano.binconv.util.Log;

public class Main {

  private static final String[] FORMATS = { "base64", "hex", "bin", "url" };

  public static void main(String args[]) {
    try {
      process(args);
    } catch (IllegalOptionException e) {
      printUsage();
    }
  }

  private static void process(String args[]) throws IllegalOptionException {
    Option option = parseOptions(args);
    for (int i = 0; i < FORMATS.length; i++) {
      String format = FORMATS[i];
      if (isTargetFormat(option, format)) {
        Converter conv = getConverterInstance(format);
        conv.process(option);
        return;
      }
    }

    throw new IllegalOptionException();
  }

  private static Option parseOptions(String args[]) {
    Option option = new Option();
    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      if (arg.startsWith("-")) {
        String name = arg.substring(1);
        String value = "";
        if ((i + 1) < args.length) {
          value = args[i + 1];
          if (value.startsWith("-")) {
            value = "";
          } else {
            i++;
          }
        }
        option.put(name, value);
      }
    }
    return option;
  }

  private static boolean isTargetFormat(Option option, String formatName) {
    if (option.hasOption("from" + formatName) || option.hasOption("to" + formatName)) {
      return true;
    }
    return false;
  }

  public static Converter getConverterInstance(String name) {
    String basePackageName = "com.takashiharano.binconv";
    String packageName = basePackageName + ".converter";
    name = name.substring(0, 1).toUpperCase() + name.substring(1);
    String fullClassName = packageName + "." + name + "Conv";
    Converter converter = getBean(fullClassName);
    return converter;
  }

  private static Converter getBean(String className) {
    try {
      Class<?> c = Class.forName(className);
      Converter bean = (Converter) c.getDeclaredConstructor().newInstance();
      return bean;
    } catch (Exception e) {
      return null;
    }
  }

  static private void printUsage() {
    String module = "binconv.jar";
    String options = "-<MODE> [SRC] -i <SRC_FILE_PATH> -o <DEST_FILE_PATH> [-newline <POS>] [-addr] [-ascii] [-enc <CHARSET>]";
    String usage = "java -jar " + module + " " + options;

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < FORMATS.length; i++) {
      if (i > 0) {
        sb.append("|");
      }
      String format = FORMATS[i];
      sb.append("from");
      sb.append(format);
      sb.append("|to");
      sb.append(format);
    }
    String modes = sb.toString();

    Log.print("Usage:");
    Log.print(usage);
    Log.print("");
    Log.print("MODE: " + modes);
    Log.print("");
    Log.print("CHARSET: utf8, sjis, euc_jp, etc");
    Log.print("See https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html");
  }

}
