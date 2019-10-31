package com.pancholi.commuter.database;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;

public class DetailAverage implements Comparable<DetailAverage> {

  private static List<DetailAverage> defaultList;

  private final String time;
  private final long distance;
  private final long duration;

  DetailAverage(String time, long distance, long duration) {
    this.time = time;
    this.distance = distance;
    this.duration = duration;
  }

  public static List<DetailAverage> getDefaultList() {
    if (defaultList == null) {
      defaultList = new ArrayList<>();
      defaultList.add(new DetailAverage("0700", 0, 0));
      defaultList.add(new DetailAverage("0730", 0, 0));
      defaultList.add(new DetailAverage("0800", 0, 0));
      defaultList.add(new DetailAverage("0830", 0, 0));
      defaultList.add(new DetailAverage("0900", 0, 0));
      defaultList.add(new DetailAverage("0930", 0, 0));
      defaultList.add(new DetailAverage("1000", 0, 0));
      defaultList.add(new DetailAverage("1030", 0, 0));
      defaultList.add(new DetailAverage("1100", 0, 0));
      defaultList.add(new DetailAverage("1130", 0, 0));
      defaultList.add(new DetailAverage("1200", 0, 0));
      defaultList.add(new DetailAverage("1230", 0, 0));
      defaultList.add(new DetailAverage("1300", 0, 0));
      defaultList.add(new DetailAverage("1330", 0, 0));
      defaultList.add(new DetailAverage("1400", 0, 0));
      defaultList.add(new DetailAverage("1430", 0, 0));
      defaultList.add(new DetailAverage("1500", 0, 0));
      defaultList.add(new DetailAverage("1530", 0, 0));
      defaultList.add(new DetailAverage("1600", 0, 0));
      defaultList.add(new DetailAverage("1630", 0, 0));
      defaultList.add(new DetailAverage("1700", 0, 0));
      defaultList.add(new DetailAverage("1730", 0, 0));
      defaultList.add(new DetailAverage("1800", 0, 0));
      defaultList.add(new DetailAverage("1830", 0, 0));
      defaultList.add(new DetailAverage("1900", 0, 0));
    }

    return defaultList;
  }

  public String getTime() {
    return time;
  }

  public long getDistance() {
    return distance;
  }

  public float getDistanceInMiles() {
    return distance / 1609.34f;
  }

  public long getDuration() {
    return duration;
  }

  public float getDurationInHours() {
    return duration / 3600f;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof DetailAverage)) {
      return false;
    }

    DetailAverage detailAverage = (DetailAverage) o;

    return detailAverage.getTime().equals(time);
  }

  @Override
  public int hashCode() {
    return Objects.hash(time, distance, duration);
  }

  @NonNull
  @SuppressLint("DefaultLocale")
  @Override
  public String toString() {
    return String.format("DetailAverage {time=%s, distance=%d, duration=%d}",
            time, distance, duration);
  }

  @Override
  public int compareTo(@NonNull DetailAverage detailAverage) {
    int firstTime = Integer.parseInt(time);
    int secondTime = Integer.parseInt(detailAverage.getTime());

    return Integer.compare(firstTime, secondTime);
  }
}
