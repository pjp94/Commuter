package com.pancholi.commuter.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "commutes",
        indices = {@Index(value = {"origin", "destination"}, unique = true)})
public class Commute implements Parcelable {

  @PrimaryKey(autoGenerate = true)
  private int id;
  private final String name;
  @NonNull
  private final String origin;
  @NonNull
  @ColumnInfo(name = "origin_id")
  private final String originId;
  @NonNull
  private final String destination;
  @NonNull
  @ColumnInfo(name = "destination_id")
  private final String destinationId;
  @ColumnInfo(name = "avoid_toll")
  private final boolean avoidToll;
  @ColumnInfo(name = "avoid_highway")
  private final boolean avoidHighway;

  public Commute(String name, @NonNull String origin, @NonNull String originId,
                 @NonNull String destination, @NonNull String destinationId,
                 boolean avoidToll, boolean avoidHighway) {
    this.name = name;
    this.origin = origin;
    this.originId = originId;
    this.destination = destination;
    this.destinationId = destinationId;
    this.avoidToll = avoidToll;
    this.avoidHighway = avoidHighway;
  }

  private Commute(Parcel source) {
    name = source.readString();
    origin = Objects.requireNonNull(source.readString());
    originId = Objects.requireNonNull(source.readString());
    destination = Objects.requireNonNull(source.readString());
    destinationId = Objects.requireNonNull(source.readString());
    avoidToll = source.readInt() == 1;
    avoidHighway = source.readInt() == 1;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  @NonNull
  public String getOrigin() {
    return origin;
  }

  @NonNull
  public String getOriginId() {
    return originId;
  }

  @NonNull
  public String getDestination() {
    return destination;
  }

  @NonNull
  public String getDestinationId() {
    return destinationId;
  }

  public String getName() {
    return name;
  }

  public boolean getAvoidToll() {
    return avoidToll;
  }

  public boolean getAvoidHighway() {
    return avoidHighway;
  }

  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

    @Override
    public Object createFromParcel(Parcel source) {
      return new Commute(source);
    }

    @Override
    public Object[] newArray(int size) {
      return new Commute[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeString(origin);
    dest.writeString(originId);
    dest.writeString(destination);
    dest.writeString(destinationId);
    dest.writeInt(avoidToll ? 1 : 0);
    dest.writeInt(avoidHighway ? 1 : 0);
  }
}
