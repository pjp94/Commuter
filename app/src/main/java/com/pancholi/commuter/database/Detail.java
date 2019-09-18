package com.pancholi.commuter.database;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "details",
        indices = @Index(value = "commute_id"),
        foreignKeys = @ForeignKey(entity = Commute.class,
                                  parentColumns = "id",
                                  childColumns = "commute_id"))
public class Detail {

  @PrimaryKey(autoGenerate = true)
  private int id;
  @ColumnInfo(name = "commute_id")
  private int commuteId;
  private Date date;
  private String time;
  private String duration;
  private float distance;

  public Detail(int commuteId, Date date, String time, String duration, float distance) {
    this.commuteId = commuteId;
    this.date = date;
    this.time = time;
    this.duration = duration;
    this.distance = distance;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public int getCommuteId() {
    return commuteId;
  }

  public Date getDate() {
    return date;
  }

  public String getTime() {
    return time;
  }

  public String getDuration() {
    return duration;
  }

  public float getDistance() {
    return distance;
  }
}
