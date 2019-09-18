package com.pancholi.commuter.googlemaps.json;

import com.google.gson.annotations.SerializedName;

public class Response {

  private String status;
  @SerializedName("origin_addresses")
  private String[] origin;
  @SerializedName("destination_addresses")
  private String[] destination;
  private Element[] rows;

  public String getOrigin() {
    return origin == null || origin.length == 0 ? null : origin[0];
  }

  public String getDestination() {
    return destination == null || destination.length == 0 ? null : destination[0];
  }

  public Element getRow() {
    return rows == null || rows.length == 0 ? null : rows[0];
  }
}
