package com.pancholi.commuter.googlemaps.json;

public class Row {

  private Element[] elements;

  public Element getElement() {
    return elements == null || elements.length == 0 ? null : elements[0];
  }
}
