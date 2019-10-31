package com.pancholi.commuter.viewmodel;

import android.app.Application;

import com.pancholi.commuter.database.Detail;
import com.pancholi.commuter.database.DetailAverage;
import com.pancholi.commuter.database.repositories.DetailRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class DetailViewModel extends AndroidViewModel {

  private DetailRepository detailRepository;
  private LiveData<List<DetailAverage>> detailAverages;

  public DetailViewModel(@NonNull Application application) {
    super(application);
    detailRepository = new DetailRepository(application);
  }

  public LiveData<List<DetailAverage>> getAllDetailAveragesForCommute(int commuteId) {
    if (detailAverages == null) {
      detailAverages = detailRepository.getAllDetailAveragesForCommute(commuteId);
    }

    return detailAverages;
  }

  public List<Detail> getListDetailsForCommute(int commuteId) {
    return detailRepository.getListDetailsForCommute(commuteId);
  }
}
