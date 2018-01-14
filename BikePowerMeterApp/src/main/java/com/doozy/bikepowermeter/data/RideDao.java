package com.doozy.bikepowermeter.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface RideDao     {
    @Insert
    void insertRide(Ride ride);
}
