package com.pancholi.commuter.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pancholi.commuter.alarm.DeviceBootReceiver;
import com.pancholi.commuter.database.Detail;
import com.pancholi.commuter.database.repositories.DetailRepository;
import com.pancholi.commuter.viewmodel.CommuteViewModel;
import com.pancholi.commuter.R;
import com.pancholi.commuter.alarm.CommuteAlarm;
import com.pancholi.commuter.commutecard.CommuteListAdapter;
import com.pancholi.commuter.database.Commute;

import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

  private static final int ADD_COMMUTE_REQUEST_CODE = 1;

  private CommuteViewModel commuteViewModel;
  private CommuteListAdapter adapter;

  @BindView(R.id.commuteProgressBar)
  ProgressBar commuteProgressBar;
  @BindView(R.id.commuteList)
  RecyclerView commuteList;
  @BindView(R.id.noCommutesAdded)
  TextView noCommutesAdded;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setCommuteList();
    setViewModel();
  }

  private void setCommuteList() {
    adapter = new CommuteListAdapter(this, getCardMenuItemClickedListener());
    commuteList.setLayoutManager(new LinearLayoutManager(this));
    commuteList.setHasFixedSize(true);
    commuteList.setAdapter(adapter);
  }

  private CommuteListAdapter.CardClickedListener getCardMenuItemClickedListener() {
    return new CommuteListAdapter.CardClickedListener() {
      @Override
      public void onCardClicked(int commuteId) {
        Intent intent = new Intent(MainActivity.this, CommuteDetailActivity.class)
                .putExtra(CommuteDetailActivity.EXTRA_COMMUTE_ID, commuteId);
        startActivity(intent);
      }

      @Override
      public void onEditClicked(Commute commute) {
        Toast.makeText(MainActivity.this, "Edit clicked.", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onDeleteClicked(Commute commute) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(getDeleteMessage(commute.getName()))
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes,
                        (dialog, which) -> commuteViewModel.delete(commute))
                .show();
      }
    };
  }

  private String getDeleteMessage(String name) {
    return getString(name == null || name.isEmpty()
            ? R.string.delete_this_commute
            : R.string.delete_commute_with_name, name);
  }

  private void setViewModel() {
    commuteViewModel = ViewModelProviders.of(this).get(CommuteViewModel.class);
    commuteViewModel.getAllObservableCommutes().observe(this, commutes -> {
      CommuteAlarm.decideAlarm(this, commutes.size() == 1);
      setDeviceBootReceiverEnabled(commutes.isEmpty());
      setViewsAfterCommutesLoad(commutes);
      logDetails(commutes);
    });
  }

  private void setDeviceBootReceiverEnabled(boolean noCommutes) {
    ComponentName receiver = new ComponentName(this, DeviceBootReceiver.class);
    PackageManager packageManager = getPackageManager();
    int flag = noCommutes
            ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            : PackageManager.COMPONENT_ENABLED_STATE_ENABLED;

    packageManager.setComponentEnabledSetting(receiver, flag, PackageManager.DONT_KILL_APP);
  }

  private void setViewsAfterCommutesLoad(List<Commute> commutes) {
    if (commutes.isEmpty()) {
      removeAlarmPreference();
    }

    adapter.setCommutes(commutes);
    commuteProgressBar.setVisibility(View.INVISIBLE);
    commuteList.setVisibility(commutes.isEmpty() ? View.INVISIBLE : View.VISIBLE);
    noCommutesAdded.setVisibility(commutes.isEmpty() ? View.VISIBLE : View.INVISIBLE);
  }

  private void removeAlarmPreference() {
    PreferenceManager
            .getDefaultSharedPreferences(MainActivity.this)
            .edit()
            .remove(CommuteAlarm.SHARED_PREFS_ALARM_SET)
            .apply();
  }

  private void logDetails(List<Commute> commutes) {
    DetailRepository repository = new DetailRepository(this);

    for (Commute commute : commutes) {
      new Thread(() ->
      {
        List<Detail> details = repository.getListDetailsForCommute(commute.getId());

        for (Detail detail : details) {
          Log.d(TAG, detail.toString());
        }
      }).start();
    }
  }

  @OnClick(R.id.buttonAddCommute)
  void addCommute() {
    Intent intent = new Intent(this, AddCommuteActivity.class);
    startActivityForResult(intent, ADD_COMMUTE_REQUEST_CODE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == ADD_COMMUTE_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        Commute commute = data.getParcelableExtra(AddCommuteActivity.EXTRA_COMMUTE);
        commuteViewModel.insert(commute);
      }
    }
  }
}
