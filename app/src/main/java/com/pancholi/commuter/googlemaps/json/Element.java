package com.pancholi.commuter.googlemaps.json;

import com.google.gson.annotations.SerializedName;

public class Element {

  private String status;
  private Duration duration;
  @SerializedName("duration_in_traffic")
  private DurationInTraffic durationInTraffic;
  private Distance distance;

  public Duration getDuration() {
    return duration;
  }

  public DurationInTraffic getDurationInTraffic() {
    return durationInTraffic;
  }

  public Distance getDistance() {
    return distance;
  }
}
