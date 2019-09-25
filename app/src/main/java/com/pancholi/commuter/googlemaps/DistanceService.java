package com.pancholi.commuter.googlemaps;

import com.pancholi.commuter.googlemaps.json.MapsResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface DistanceService {

  @GET("maps/api/distancematrix/json")
  Call<MapsResponse> getDistance(@QueryMap Map<String, String> options);
}
