package com.pancholi.commuter.util;

import java.util.Calendar;

public class TimeUtil {

  private TimeUtil() { }

  public static String getReadableCurrentTime(Calendar calendar) {
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    int flooredMinute = minute >= 30 ? 30 : 0;

    return formatReadableTime(hour, flooredMinute);
  }

  private static String formatReadableTime(int hour, int minute) {
    String hourString = String.format(hour < 10 ? "0%d" : "%d", hour);
    String minuteString = minute == 0 ? "00" : String.valueOf(minute);

    return hourString + minuteString;
  }
}
