package com.pancholi.commuter;

import com.pancholi.commuter.util.TimeUtil;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class TimeTests {

  private static Calendar setCalendar(int hour, int minute) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, hour);
    calendar.set(Calendar.MINUTE, minute);

    return calendar;
  }

  @Test
  public void testZeroMinute() {
    Calendar calendar = setCalendar(7, 0);
    String time = TimeUtil.getReadableCurrentTime(calendar);

    Assert.assertEquals("0700", time);
  }

  @Test
  public void testThirtyMinute() {
    Calendar calendar = setCalendar(7, 30);
    String time = TimeUtil.getReadableCurrentTime(calendar);

    Assert.assertEquals("0730", time);
  }

  @Test
  public void testLessThanThirtyMinute() {
    Calendar calendar = setCalendar(9, 2);
    String time = TimeUtil.getReadableCurrentTime(calendar);

    Assert.assertEquals("0900", time);
  }

  @Test
  public void testGreaterThanThirtyMinute() {
    Calendar calendar = setCalendar(8, 49);
    String time = TimeUtil.getReadableCurrentTime(calendar);

    Assert.assertEquals("0830", time);
  }

  @Test
  public void testDoubleDigitHour() {
    Calendar calendar = setCalendar(14, 0);
    String time = TimeUtil.getReadableCurrentTime(calendar);

    Assert.assertEquals("1400", time);
  }

  @Test
  public void testDoubleDigitHourAndLessThanThirtyMinute() {
    Calendar calendar = setCalendar(16, 5);
    String time = TimeUtil.getReadableCurrentTime(calendar);

    Assert.assertEquals("1600", time);
  }

  @Test
  public void testDoubleDigitHourAndGreaterThanThirtyMinute() {
    Calendar calendar = setCalendar(18, 34);
    String time = TimeUtil.getReadableCurrentTime(calendar);

    Assert.assertEquals("1830", time);
  }
}
