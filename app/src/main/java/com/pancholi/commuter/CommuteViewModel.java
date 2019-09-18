package com.pancholi.commuter;

import android.app.Application;

import com.pancholi.commuter.database.Commute;
import com.pancholi.commuter.database.repositories.CommuteRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CommuteViewModel extends AndroidViewModel {

  private CommuteRepository commuteRepository;
  private LiveData<List<Commute>> commutes;

  public CommuteViewModel(@NonNull Application application) {
    super(application);
    commuteRepository = new CommuteRepository(application);
  }

  public void insert(Commute commute) {
    commuteRepository.insert(commute);
  }

  public void update(Commute commute) {
    commuteRepository.update(commute);
  }

  public void delete(Commute commute) {
    commuteRepository.delete(commute);
  }

  public void deleteAllCommutes() {
    commuteRepository.deleteAllCommutes();
  }

  public LiveData<List<Commute>> getAllCommutes() {
    if (commutes == null) {
      commutes = commuteRepository.getAllCommutes();
    }

    return commutes;
  }
}
