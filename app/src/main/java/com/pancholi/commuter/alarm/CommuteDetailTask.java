package com.pancholi.commuter.alarm;

import android.os.AsyncTask;
import android.util.Log;

import com.pancholi.commuter.activity.BaseActivity;
import com.pancholi.commuter.googlemaps.DistanceService;
import com.pancholi.commuter.database.Commute;
import com.pancholi.commuter.database.Detail;
import com.pancholi.commuter.googlemaps.MapsUtil;
import com.pancholi.commuter.googlemaps.json.Element;
import com.pancholi.commuter.googlemaps.json.MapsResponse;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommuteDetailTask extends AsyncTask<Commute, Void, Detail> {

  private final DetailRetriever detailRetriever;

  public interface DetailRetriever {
    void detailRetrieved(Detail detail);
  }

  CommuteDetailTask(DetailRetriever detailRetriever) {
    this.detailRetriever = detailRetriever;
  }

  @Override
  protected Detail doInBackground(Commute... commutes) {
    Log.d(BaseActivity.TAG, "Retrieving commute details");
    Commute commute = commutes[0];

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    DistanceService distanceService = retrofit.create(DistanceService.class);
    Call<MapsResponse> distanceCall = distanceService.getDistance(getQueryMap(commute));
    MapsResponse mapsResponse = null;

    try {
      Response<MapsResponse> response = distanceCall.execute();
      mapsResponse = response.body();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (mapsResponse == null) {
      return null;
    }

    return getDetailFromResponse(mapsResponse, commute.getId());
  }

  private Detail getDetailFromResponse(MapsResponse response, int commuteId) {
    Element element = response.getRow().getElement();
    String distance = element.getDistance().getText();
    String duration = element.getDurationInTraffic().getText();
    Date date = new Date();
    long time = System.currentTimeMillis();

    return new Detail(commuteId, date, time, distance, duration);
  }

  private Map<String, String> getQueryMap(Commute commute) {
    String originId = commute.getOriginId();
    String destinationId = commute.getDestinationId();
    boolean avoidTolls = commute.getAvoidToll();
    boolean avoidHighways = commute.getAvoidHighway();

    Map<String, String> map = new HashMap<>();
    map.put("origins", "place_id:" + originId);
    map.put("destinations", "place_id:" + destinationId);
    map.put("units", "imperial");
    map.put("departure_time", "now");
    map.put("key", MapsUtil.API_KEY);

    if (avoidTolls) {
      map.put("avoid", "tolls");
    }

    if (avoidHighways) {
      map.put("avoid", "highways");
    }

    return map;
  }

  @Override
  public void onPostExecute(Detail detail) {
    Log.d(BaseActivity.TAG, detail.toString());
    detailRetriever.detailRetrieved(detail);
  }
}
