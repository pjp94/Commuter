package com.pancholi.commuter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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

  private String origin;
  private String originId;
  private String destination;
  private String destinationId;

  private Drawable startClear;
  private Drawable endClear;

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
  }

  @Override
  protected void onResume() {
    super.onResume();
    setAddressFieldDrawablesVisibility();
  }

  private void setAddressFieldDrawablesVisibility() {
    startClear = ViewUtil.getDrawableFromTextView(startAddress,
            ViewUtil.RIGHT_COMPOUND_DRAWABLE_INDEX);
    endClear = ViewUtil.getDrawableFromTextView(endAddress,
            ViewUtil.RIGHT_COMPOUND_DRAWABLE_INDEX);

    Drawable startDrawable = origin != null && !origin.isEmpty() ? startClear : null;
    Drawable endDrawable = destination != null && !destination.isEmpty() ? endClear : null;

    setDrawableEnd(startAddress, startDrawable);
    setDrawableEnd(endAddress, endDrawable);
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
    savedInstanceState.putString(SAVED_INSTANCE_STATE_ORIGIN, origin);
    savedInstanceState.putString(SAVED_INSTANCE_STATE_ORIGIN_ID, originId);
    savedInstanceState.putString(SAVED_INSTANCE_STATE_DESTINATION, destination);
    savedInstanceState.putString(SAVED_INSTANCE_STATE_DESTINATION_ID, destinationId);
    super.onSaveInstanceState(savedInstanceState);
  }

  @Override
  public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    origin = savedInstanceState.getString(SAVED_INSTANCE_STATE_ORIGIN);
    originId = savedInstanceState.getString(SAVED_INSTANCE_STATE_ORIGIN_ID);
    destination = savedInstanceState.getString(SAVED_INSTANCE_STATE_DESTINATION);
    destinationId = savedInstanceState.getString(SAVED_INSTANCE_STATE_DESTINATION_ID);
    startAddress.setText(origin);
    endAddress.setText(destination);
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

    if (!startClear.isVisible()) {
      getNewStartAddress();
      return;
    }

    if (clearButtonPressed(startAddress, startClear, event)) {
      origin = null;
      originId = null;
      startAddress.setText("");
      setDrawableEnd(startAddress, null);
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

    if (!endClear.isVisible()) {
      getNewEndAddress();
      return;
    }

    if (clearButtonPressed(endAddress, endClear, event)) {
      destination = null;
      destinationId = null;
      endAddress.setText("");
      setDrawableEnd(endAddress, null);
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

  private boolean clearButtonPressed(TextView addressBar,
                                     Drawable clearButton,
                                     MotionEvent event) {
    return event.getRawX() >=
            addressBar.getRight() -
            clearButton.getBounds().width() -
            addressBar.getPaddingRight() -
            addressBar.getCompoundDrawablePadding();
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
      Place place = Autocomplete.getPlaceFromIntent(data);
      origin = getTrimmedAddress(place.getAddress());
      originId = place.getId();
      startAddress.setText(origin);
      setDrawableEnd(startAddress, startClear);
    } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
      logAutocompleteError(data);
    }
  }

  private void handleDestinationResult(int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      Place place = Autocomplete.getPlaceFromIntent(data);
      destination = getTrimmedAddress(place.getAddress());
      destinationId = place.getId();
      endAddress.setText(destination);
      setDrawableEnd(endAddress, endClear);
    } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
      logAutocompleteError(data);
    }
  }

  private String getTrimmedAddress(String address) {
    if (address == null) {
      return null;
    }

    return address.substring(0, address.indexOf(", USA"));
  }

  private void setDrawableEnd(TextView view, Drawable drawable) {
    view.setCompoundDrawablesRelative(null, null, drawable, null);
  }

  private void logAutocompleteError(Intent data) {
    Status status = Autocomplete.getStatusFromIntent(data);
    Log.d(TAG, status.getStatusMessage());
  }

  @OnClick(R.id.buttonSaveCommute)
  void saveCommute() {
    String nameInput = commuteName.getText().toString().trim();
    String name = nameInput.isEmpty() ? null : nameInput;
    boolean avoidTolls = checkBoxAvoidTolls.isChecked();
    boolean avoidHighways = checkBoxAvoidHighways.isChecked();

    Commute commute = new Commute(name, origin, originId, destination,
            destinationId, avoidTolls, avoidHighways);
    Intent intent = new Intent().putExtra(EXTRA_COMMUTE, commute);

    setResult(RESULT_OK, intent);
    finish();
  }
}
