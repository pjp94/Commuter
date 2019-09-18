package com.pancholi.commuter;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

  public static final String TAG = "CommuterTag";

  @Override
  public void setContentView(@LayoutRes int layoutRes) {
    super.setContentView(layoutRes);
    ButterKnife.bind(this);
  }
}
