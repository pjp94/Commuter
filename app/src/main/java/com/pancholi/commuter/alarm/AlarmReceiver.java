package com.pancholi.commuter.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.pancholi.commuter.activity.BaseActivity;
import com.pancholi.commuter.database.Commute;
import com.pancholi.commuter.database.repositories.CommuteRepository;
import com.pancholi.commuter.database.repositories.DetailRepository;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d(BaseActivity.TAG, "Alarm activated");
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 19);
    calendar.set(Calendar.MINUTE, 15);

    if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
      CommuteAlarm.cancelAlarm(context);
      CommuteAlarm.scheduleAlarm(context);
    } else {
      new AlarmReceiverTask(context, commutes ->
              getDetailsForCommutes(context, commutes)).execute();
    }
  }

  private void getDetailsForCommutes(Context context, List<Commute> commutes) {
    if (commutes == null) {
      Log.d(BaseActivity.TAG, "AlarmReceiver: Retrieved commutes list is null.");
      return;
    }

    DetailRepository detailRepository = new DetailRepository(context);
    Random random = new Random();

    for (Commute commute : commutes) {
      new Handler().postDelayed(() ->
                      new CommuteDetailTask(detailRepository::insert).execute(commute),
              1000 * random.nextInt(30));
    }
  }

  private static class AlarmReceiverTask extends AsyncTask<Void, Void, List<Commute>> {

    private final WeakReference<Context> context;
    private final CommutesRetriever commutesRetriever;

    AlarmReceiverTask(Context context, CommutesRetriever commutesRetriever) {
      this.context = new WeakReference<>(context);
      this.commutesRetriever = commutesRetriever;
    }

    interface CommutesRetriever {
      void commutesRetrieved(List<Commute> commutes);
    }

    @Override
    protected List<Commute> doInBackground(Void... voids) {
      CommuteRepository commuteRepository = new CommuteRepository(context.get());
      return commuteRepository.getAllCommutes();
    }

    @Override
    public void onPostExecute(List<Commute> commutes) {
      commutesRetriever.commutesRetrieved(commutes);
    }
  }
}
