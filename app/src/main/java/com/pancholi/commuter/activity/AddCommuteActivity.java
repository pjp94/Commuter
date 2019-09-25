package com.pancholi.commuter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.pancholi.commuter.R;
import com.pancholi.commuter.commutecard.AddressClearDrawable;
import com.pancholi.commuter.commutecard.AddressInfo;
import com.pancholi.commuter.database.Commute;
import com.pancholi.commuter.googlemaps.MapsUtil;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

public class AddCommuteActivity extends BaseActivity {

  static final String EXTRA_COMMUTE = "com.pancholi.commuter.EXTRA_COMMUTE";

  private static final String SAVED_INSTANCE_STATE_ORIGIN = "savedInstanceStateOrigin";
  private static final String SAVED_INSTANCE_STATE_ORIGIN_ID = "savedInstanceOriginId";
  private static final String SAVED_INSTANCE_STATE_DESTINATION = "savedInstanceStateDestination";
  private static final String SAVED_INSTANCE_STATE_DESTINATION_ID = "savedInstanceStateDestinationId";

  private static final int AUTOCOMPLETE_START_REQUEST_CODE = 1;
  private static final int AUTOCOMPLETE_END_REQUEST_CODE = 2;

  private static final List<Place.Field> PLACE_FIELDS =
          Arrays.asList(Place.Field.ID, Place.Field.ADDRESS);

  private AddressInfo startAddressInfo;
  private AddressInfo endAddressInfo;

  private AddressClearDrawable startClear;
  private AddressClearDrawable endClear;

  @BindView(R.id.addCommuteStartAddress)
  TextView startAddress;
  @BindView(R.id.addCommuteEndAddress)
  TextView endAddress;
  @BindView(R.id.addCommuteName)
  EditText commuteName;
  @BindView(R.id.addCommuteAvoidTolls)
  CheckBox checkBoxAvoidTolls;
  @BindView(R.id.addCommuteAvoidHighways)
  CheckBox checkBoxAvoidHighways;
  @BindView(R.id.buttonSaveCommute)
  Button saveCommute;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_commute);

    Places.initialize(this, MapsUtil.API_KEY);
    setAddressInfoAndDrawables();
  }

  @Override
  protected void onResume() {
    super.onResume();
    setAddressClearDrawablesVisibility();
  }

  private void setAddressInfoAndDrawables() {
    startAddressInfo = new AddressInfo();
    endAddressInfo = new AddressInfo();
    startClear = new AddressClearDrawable(startAddress);
    endClear = new AddressClearDrawable(endAddress);
  }

  private void setAddressClearDrawablesVisibility() {
    String origin = startAddressInfo.getAddress();
    String destination = endAddressInfo.getAddress();

    startClear.setDrawableEnd(origin != null && !origin.isEmpty());
    endClear.setDrawableEnd(destination != null && !destination.isEmpty());
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
    savedInstanceState.putString(SAVED_INSTANCE_STATE_ORIGIN, startAddressInfo.getAddress());
    savedInstanceState.putString(SAVED_INSTANCE_STATE_ORIGIN_ID, startAddressInfo.getAddressId());
    savedInstanceState.putString(SAVED_INSTANCE_STATE_DESTINATION, endAddressInfo.getAddress());
    savedInstanceState.putString(SAVED_INSTANCE_STATE_DESTINATION_ID, endAddressInfo.getAddressId());
    super.onSaveInstanceState(savedInstanceState);
  }

  @Override
  public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    startAddressInfo.setAddress(savedInstanceState.getString(SAVED_INSTANCE_STATE_ORIGIN));
    startAddressInfo.setAddressId(savedInstanceState.getString(SAVED_INSTANCE_STATE_ORIGIN_ID));
    endAddressInfo.setAddress(savedInstanceState.getString(SAVED_INSTANCE_STATE_DESTINATION));
    endAddressInfo.setAddressId(savedInstanceState.getString(SAVED_INSTANCE_STATE_DESTINATION_ID));
    startAddress.setText(startAddressInfo.getAddress());
    endAddress.setText(endAddressInfo.getAddress());
  }

  @OnClick(R.id.buttonBack)
  void backButtonPressed() {
    setResult(RESULT_CANCELED);
    super.onBackPressed();
  }

  @OnTextChanged({R.id.addCommuteStartAddress, R.id.addCommuteEndAddress})
  void addressTextChanged() {
    boolean enabled = !startAddress.getText().toString().isEmpty()
            && !endAddress.getText().toString().isEmpty();

    saveCommute.setEnabled(enabled);
    saveCommute.setBackgroundColor(getResources().getColor(
            enabled ? R.color.colorPrimary : R.color.disabled));
    saveCommute.setTextColor(getResources().getColor(
            enabled ? android.R.color.white : R.color.disabledText));
  }

  @OnTouch(R.id.addCommuteStartAddress)
  void startAddressTouched(MotionEvent event) {
    if (event.getAction() != MotionEvent.ACTION_UP) {
      return;
    }

    if (startClear.notVisible()) {
      getNewStartAddress();
      return;
    }

    if (startClear.isDrawablePressed(event)) {
      startAddress.setText("");
      startAddressInfo.clearValues();
      startClear.setDrawableEnd(false);
    } else {
      getNewStartAddress();
    }
  }

  private void getNewStartAddress() {
    startAutocompleteActivity(AUTOCOMPLETE_START_REQUEST_CODE);
  }

  @OnTouch(R.id.addCommuteEndAddress)
  void endAddressTouched(MotionEvent event) {
    if (event.getAction() != MotionEvent.ACTION_UP) {
      return;
    }

    if (endClear.notVisible()) {
      getNewEndAddress();
      return;
    }

    if (endClear.isDrawablePressed(event)) {
      endAddress.setText("");
      endAddressInfo.clearValues();
      endClear.setDrawableEnd(false);
    } else {
      getNewEndAddress();
    }
  }

  private void getNewEndAddress() {
    startAutocompleteActivity(AUTOCOMPLETE_END_REQUEST_CODE);
  }

  private void startAutocompleteActivity(int requestCode) {
    Intent intent = new Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, PLACE_FIELDS)
            .setCountry("US")
            .build(this);
    startActivityForResult(intent, requestCode);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == AUTOCOMPLETE_START_REQUEST_CODE) {
      handleOriginResult(resultCode, data);
    } else {
      handleDestinationResult(resultCode, data);
    }
  }

  private void handleOriginResult(int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      setAddressValues(Autocomplete.getPlaceFromIntent(data),
              startAddress, startClear, startAddressInfo);
    } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
      logAutocompleteError(data);
    }
  }

  private void handleDestinationResult(int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      setAddressValues(Autocomplete.getPlaceFromIntent(data),
              endAddress, endClear, endAddressInfo);
    } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
      logAutocompleteError(data);
    }
  }

  private void setAddressValues(Place place, TextView addressBar,
                                AddressClearDrawable clearDrawable,
                                AddressInfo addressInfo) {
    addressInfo.setAddress(getTrimmedAddress(place.getAddress()));
    addressInfo.setAddressId(place.getId());
    addressBar.setText(addressInfo.getAddress());
    clearDrawable.setDrawableEnd(true);
  }

  private String getTrimmedAddress(String address) {
    if (address == null) {
      return null;
    }

    return address.substring(0, address.indexOf(", USA"));
  }

  private void logAutocompleteError(Intent data) {
    Status status = Autocomplete.getStatusFromIntent(data);
    Log.d(TAG, status.getStatusMessage());
  }

  @OnClick(R.id.buttonSaveCommute)
  void saveCommute() {
    String nameInput = commuteName.getText().toString().trim();
    String name = nameInput.isEmpty() ? null : nameInput;
    String origin = startAddressInfo.getAddress();
    String originId = startAddressInfo.getAddressId();
    String destination = endAddressInfo.getAddress();
    String destinationId = endAddressInfo.getAddressId();
    boolean avoidTolls = checkBoxAvoidTolls.isChecked();
    boolean avoidHighways = checkBoxAvoidHighways.isChecked();

    Commute commute = new Commute(name, origin, originId, destination,
            destinationId, avoidTolls, avoidHighways);
    Intent intent = new Intent().putExtra(EXTRA_COMMUTE, commute);

    setResult(RESULT_OK, intent);
    finish();
  }
}
