package com.pancholi.commuter.database.repositories;

import android.content.Context;

import com.pancholi.commuter.database.CommuterDatabase;
import com.pancholi.commuter.database.Detail;
import com.pancholi.commuter.database.DetailDao;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;

public class DetailRepository extends BaseRepository<Detail> {

  private DetailDao detailDao;

  public DetailRepository(Context context) {
    detailDao = CommuterDatabase.getInstance(context).detailDao();
  }

  public void insert(Detail detail) {
    super.insert(detailDao, detail);
  }

  public void update(Detail detail) {
    super.update(detailDao, detail);
  }

  public void delete(Detail detail) {
    super.delete(detailDao, detail);
  }

  public LiveData<List<Detail>> getAllDetailsForCommute(int commuteId) {
    return detailDao.getAllDetailsForCommute(commuteId);
  }

  public LiveData<List<Detail>> getAllDetailsForCommuteAfterDate(int commuteId, Date date) {
    return detailDao.getDetailsForCommuteAfterDate(commuteId, date);
  }
}
