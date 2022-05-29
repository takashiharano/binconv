package com.takashiharano.binconv.converter;

import com.takashiharano.binconv.IllegalOptionException;
import com.takashiharano.binconv.Option;

public interface Converter {

  public void process(Option option) throws IllegalOptionException;

}
