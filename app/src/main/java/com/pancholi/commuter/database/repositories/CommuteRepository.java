package com.pancholi.commuter.database.repositories;

import android.content.Context;
import android.os.AsyncTask;

import com.pancholi.commuter.database.Commute;
import com.pancholi.commuter.database.CommuteDao;
import com.pancholi.commuter.database.CommuterDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class CommuteRepository extends BaseRepository<Commute> {

  private CommuteDao commuteDao;

  public CommuteRepository(Context context) {
    commuteDao = CommuterDatabase.getInstance(context).commuteDao();
  }

  public void insert(Commute commute) {
    super.insert(commuteDao, commute);
  }

  public void update(Commute commute) {
    super.update(commuteDao, commute);
  }

  public void delete(Commute commute) {
    super.delete(commuteDao, commute);
  }

  public LiveData<List<Commute>> getAllObservableCommutes() {
    return commuteDao.getAllObservableCommutes();
  }

  public List<Commute> getAllCommutes() {
    return commuteDao.getAllCommutes();
  }

  public void deleteAllCommutes() {
    new DeleteAllTask(commuteDao).execute();
  }

  private static class DeleteAllTask extends AsyncTask<Void, Void, Void> {

    private CommuteDao commuteDao;

    private DeleteAllTask(CommuteDao commuteDao) {
      this.commuteDao = commuteDao;
    }

    @Override
    protected Void doInBackground(Void... params) {
      commuteDao.deleteAllCommutes();
      return null;
    }
  }
}
