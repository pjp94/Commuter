package com.pancholi.commuter.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface CommuteDao extends BaseDao<Commute> {

  @Query("SELECT * FROM commutes")
  LiveData<List<Commute>> getAllObservableCommutes();

  @Query("SELECT * FROM commutes")
  List<Commute> getAllCommutes();

  @Query("DELETE FROM commutes")
  void deleteAllCommutes();
}
