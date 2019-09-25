package com.pancholi.commuter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pancholi.commuter.viewmodel.CommuteViewModel;
import com.pancholi.commuter.R;
import com.pancholi.commuter.alarm.CommuteAlarm;
import com.pancholi.commuter.commutecard.CommuteCardMenuClickListener;
import com.pancholi.commuter.commutecard.CommuteListAdapter;
import com.pancholi.commuter.database.Commute;

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

  private CommuteCardMenuClickListener.CardMenuItemClickedListener getCardMenuItemClickedListener() {
    return new CommuteCardMenuClickListener.CardMenuItemClickedListener() {
      @Override
      public void onEditClicked() {
        Toast.makeText(MainActivity.this, "Edit clicked.", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onDeleteClicked() {
        Toast.makeText(MainActivity.this, "Delete clicked", Toast.LENGTH_SHORT).show();
      }
    };
  }

  private void setViewModel() {
    commuteViewModel = ViewModelProviders.of(this).get(CommuteViewModel.class);
    commuteViewModel.getAllObservableCommutes().observe(this, commutes -> {
      boolean isEmpty = commutes.isEmpty();

      CommuteAlarm.decideAlarm(this, !isEmpty);
      adapter.setCommutes(commutes);
      commuteProgressBar.setVisibility(View.INVISIBLE);
      commuteList.setVisibility(isEmpty ? View.INVISIBLE : View.VISIBLE);
      noCommutesAdded.setVisibility(isEmpty ? View.VISIBLE : View.INVISIBLE);
    });
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
