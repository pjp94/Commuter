package com.pancholi.commuter.database;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface DetailDao extends BaseDao<Detail> {

  @Query("SELECT * FROM details WHERE commute_id = :commuteId")
  LiveData<List<Detail>> getAllDetailsForCommute(int commuteId);

  @Query("SELECT * FROM details WHERE commute_id = :commuteId ORDER BY date ASC")
  LiveData<List<Detail>> getAllOrderedDetailsForCommute(int commuteId);

  @Query("SELECT * FROM details WHERE commute_id = :commuteId")
  List<Detail> getListDetailsForCommute(int commuteId);

  @Query("SELECT * FROM details WHERE commute_id = :commuteId AND date > :date")
  LiveData<List<Detail>> getDetailsForCommuteAfterDate(int commuteId, Date date);
}
