package com.pancholi.commuter.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

  private CommuteListAdapter.CardMenuItemClickedListener getCardMenuItemClickedListener() {
    return new CommuteListAdapter.CardMenuItemClickedListener() {
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
      setViewsAfterCommutesLoad(commutes);
//      logDetails(commutes);
    });
  }

  private void setViewsAfterCommutesLoad(List<Commute> commutes) {
    adapter.setCommutes(commutes);
    commuteProgressBar.setVisibility(View.INVISIBLE);
    commuteList.setVisibility(commutes.isEmpty() ? View.INVISIBLE : View.VISIBLE);
    noCommutesAdded.setVisibility(commutes.isEmpty() ? View.VISIBLE : View.INVISIBLE);
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
