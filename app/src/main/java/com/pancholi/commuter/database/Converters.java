package com.pancholi.commuter.database;

import java.util.Date;

import androidx.room.TypeConverter;

public class Converters {

  @TypeConverter
  public static Date fromTimestamp(Long time) {
    return time == null ? null : new Date(time);
  }

  @TypeConverter
  public static Long toTimestamp(Date date) {
    return date == null ? null : date.getTime();
  }
}
