package com.pancholi.commuter.database;

import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "details",
        indices = @Index(value = "commute_id"),
        foreignKeys = @ForeignKey(entity = Commute.class,
                                  parentColumns = "id",
                                  childColumns = "commute_id",
                                  onDelete = CASCADE))
public class Detail {

  @PrimaryKey(autoGenerate = true)
  private int id;
  @ColumnInfo(name = "commute_id")
  private final int commuteId;
  private final Date date;
  private final String time;
  private final long distance; // 1609.34 meters = 1 mile
  private final long duration; // seconds

  public Detail(int commuteId, Date date, String time, long distance, long duration) {
    this.commuteId = commuteId;
    this.date = date;
    this.time = time;
    this.distance = distance;
    this.duration = duration;
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

  public long getDistance() {
    return distance;
  }

  public long getDuration() {
    return duration;
  }

  @Override
  public int hashCode() {
    return Objects.hash(commuteId, date, time, distance, duration);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Detail)) {
      return false;
    }

    Detail detail = (Detail) o;

    return detail.getId() == id &&
            detail.getCommuteId() == commuteId &&
            detail.getDate().equals(date) &&
            detail.getTime().equals(time) &&
            detail.getDistance() == distance &&
            detail.getDuration() ==duration;
  }

  @NonNull
  @Override
  public String toString() {
    return "Detail {" +
            "id=" + id +
            ", commuteId=" + commuteId +
            ", date=" + date +
            ", time=" + time +
            ", distance='" + distance + '\'' +
            ", duration='" + duration + '\'' +
            '}';
  }
}
