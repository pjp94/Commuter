package com.pancholi.commuter.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.pancholi.commuter.activity.BaseActivity;

import java.util.Calendar;

public class CommuteAlarm {

  private static final String SHARED_PREFS_ALARM_SET = "sharedPreferencesAlarmSet";

  private static AlarmManager alarmManager;
  private static PendingIntent alarmIntent;

  public static void decideAlarm(Context context, boolean firstCommuteAdded) {
    if (PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(SHARED_PREFS_ALARM_SET, false)) {
      return;
    }

    getAlarmManager(context);
    scheduleOrCancelAlarm(context, firstCommuteAdded);
  }

  private static void getAlarmManager(Context context) {
    if (alarmManager == null) {
      alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }
  }

  private static void scheduleOrCancelAlarm(Context context, boolean firstCommuteAdded) {
    if (firstCommuteAdded) {
      scheduleAlarm(context);
    } else {
      cancelAlarm(context);
    }
  }

  static void scheduleAlarm(Context context) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.set(Calendar.HOUR_OF_DAY, 11);
    calendar.set(Calendar.MINUTE, 17);
//    calendar.add(Calendar.DAY_OF_MONTH, 1);

    getAlarmManager(context);
    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
            AlarmManager.INTERVAL_HALF_HOUR, getAlarmIntent(context));
    setSharedPreferencesAlarm(context, true);
    Log.d(BaseActivity.TAG, "Alarm scheduled");
  }

  static void cancelAlarm(Context context) {
    getAlarmManager(context);
    alarmManager.cancel(getAlarmIntent(context));
    setSharedPreferencesAlarm(context, false);
    Log.d(BaseActivity.TAG, "Alarm canceled");
  }

  private static PendingIntent getAlarmIntent(Context context) {
    if (alarmIntent == null) {
      alarmIntent = PendingIntent.getBroadcast(context, 0,
              new Intent(context, AlarmReceiver.class), 0);
    }

    return alarmIntent;
  }

  private static void setSharedPreferencesAlarm(Context context, boolean alarmSet) {
    PreferenceManager
            .getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(SHARED_PREFS_ALARM_SET, alarmSet)
            .apply();
  }
}
