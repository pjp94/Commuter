package com.pancholi.commuter.googlemaps;

public class MapsUtil {

  public static final String API_KEY = "AIzaSyDpDNa67hOuSCY3QEnvzzUd-we2SisjETc";
  public static final String URL = "https://maps.googleapis.com/maps/api/distancematrix/json?" +
          "origins=%s&departure=%s&units=imperial&key=" + API_KEY;

  private MapsUtil() {}
}
