package com.doozy.bikepowermeter.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RideDao     {
    @Insert
    void insertRide(Ride ride);

    @Query("SELECT * FROM rides")
    List<Ride> getRides();
}
