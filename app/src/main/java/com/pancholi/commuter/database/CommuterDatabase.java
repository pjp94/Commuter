package com.pancholi.commuter.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Commute.class, Detail.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class CommuterDatabase extends RoomDatabase {

  private static final String NAME = "CommuterDatabase";

  private static CommuterDatabase instance;

  public abstract CommuteDao commuteDao();
  public abstract DetailDao detailDao();

  public static synchronized CommuterDatabase getInstance(Context context) {
    if (instance == null) {
      instance = Room.databaseBuilder(context, CommuterDatabase.class, NAME)
                    .fallbackToDestructiveMigration()
                    .build();
    }

    return instance;
  }
}
