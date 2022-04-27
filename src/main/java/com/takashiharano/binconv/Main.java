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

import com.takashiharano.binconv.converter.Base64Conv;
import com.takashiharano.binconv.converter.BinConv;
import com.takashiharano.binconv.converter.HexConv;
import com.takashiharano.binconv.util.Log;

public class Main {

  public static void main(String args[]) {
    try {
      process(args);
    } catch (IllegalOptionException e) {
      printUsage();
    }
  }

  private static void process(String args[]) throws IllegalOptionException {
    Option option = parseOptions(args);

    if (option.hasOption("frombase64") || option.hasOption("tobase64")) {
      Base64Conv.main(option);
    } else if (option.hasOption("fromhex") || option.hasOption("tohex")) {
      HexConv.main(option);
    } else if (option.hasOption("frombin") || option.hasOption("tobin")) {
      BinConv.main(option);
    } else {
      throw new IllegalOptionException();
    }
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

  static private void printUsage() {
    Log.print("Usage:");
    Log.print("java -jar binconv.jar -<MODE> [SRC] -i <SRC_FILE_PATH> -o <DEST_FILE_PATH> [-newline <NEWLINE_POS>]");
    Log.print("");
    Log.print("MODE:");
    Log.print("frombase64|tobase64|fromhex|tohex|frombin|tobin");
  }

}
