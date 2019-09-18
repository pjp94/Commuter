package com.pancholi.commuter.database.repositories;

import android.os.AsyncTask;

import com.pancholi.commuter.database.BaseDao;

abstract class BaseRepository<T> {

  void insert(BaseDao<T> dao, T object) {
    new InsertTask<>(dao).execute(object);
  }

  void update(BaseDao<T> dao, T object) {
    new UpdateTask<>(dao).execute(object);
  }

  void delete(BaseDao<T> dao, T object) {
    new DeleteTask<>(dao).execute(object);
  }

  private static class InsertTask<T> extends AsyncTask<T, Void, Void> {

    private BaseDao<T> dao;

    private InsertTask(BaseDao<T> dao) {
      this.dao = dao;
    }

    @SafeVarargs
    @Override
    protected final Void doInBackground(T... objects) {
      dao.insert(objects[0]);
      return null;
    }
  }

  private static class UpdateTask<T> extends AsyncTask<T, Void, Void> {

    private BaseDao<T> dao;

    private UpdateTask(BaseDao<T> dao) {
      this.dao = dao;
    }

    @SafeVarargs
    @Override
    protected final Void doInBackground(T... objects) {
      dao.update(objects[0]);
      return null;
    }
  }

  private static class DeleteTask<T> extends AsyncTask<T, Void, Void> {

    private BaseDao<T> dao;

    private DeleteTask(BaseDao<T> dao) {
      this.dao = dao;
    }

    @SafeVarargs
    @Override
    protected final Void doInBackground(T... objects) {
      dao.delete(objects[0]);
      return null;
    }
  }
}
