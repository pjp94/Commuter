package com.pancholi.commuter.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DeviceBootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();

    if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
      CommuteAlarm.scheduleAlarm(context);
    }
  }
}
