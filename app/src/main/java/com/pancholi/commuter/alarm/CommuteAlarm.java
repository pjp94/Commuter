package com.pancholi.commuter.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pancholi.commuter.activity.BaseActivity;

import java.util.Calendar;

public class CommuteAlarm {

  private static AlarmManager alarmManager;
  private static PendingIntent alarmIntent;

  public static void decideAlarm(Context context, boolean hasCommutes) {
    getAlarmManager(context);
    scheduleOrCancelAlarm(context, hasCommutes);
  }

  private static void getAlarmManager(Context context) {
    if (alarmManager == null) {
      alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }
  }

  private static void scheduleOrCancelAlarm(Context context, boolean hasCommutes) {
    if (hasCommutes) {
      scheduleAlarm(context);
    } else {
      cancelAlarm(context);
    }
  }

  static void scheduleAlarm(Context context) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.set(Calendar.HOUR, 7);
    calendar.add(Calendar.DAY_OF_MONTH, 1);

    getAlarmManager(context);
    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
            AlarmManager.INTERVAL_HALF_HOUR, getAlarmIntent(context));
    Log.d(BaseActivity.TAG, "Alarm scheduled");
  }

  static void cancelAlarm(Context context) {
    getAlarmManager(context);
    alarmManager.cancel(getAlarmIntent(context));
    Log.d(BaseActivity.TAG, "Alarm canceled");
  }

  private static PendingIntent getAlarmIntent(Context context) {
    if (alarmIntent == null) {
      alarmIntent = PendingIntent.getBroadcast(context, 0,
              new Intent(context, AlarmReceiver.class), 0);
    }

    return alarmIntent;
  }
}
