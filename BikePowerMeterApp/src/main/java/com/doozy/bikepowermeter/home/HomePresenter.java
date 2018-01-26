package com.doozy.bikepowermeter.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

import com.doozy.bikepowermeter.data.AppDatabase;
import com.doozy.bikepowermeter.data.Measurement;
import com.doozy.bikepowermeter.data.Ride;
import com.doozy.bikepowermeter.data.Weather;
import com.doozy.bikepowermeter.home.HomeContract.Position;
import com.doozy.bikepowermeter.services.WeatherService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;

import static com.doozy.bikepowermeter.home.HomeContract.Position.AERODYNAMIC;
import static com.doozy.bikepowermeter.home.HomeContract.Position.AGGRESSIVE;
import static com.doozy.bikepowermeter.home.HomeContract.Position.RELAXED;

/**
 * TODO: Description
 */

public class HomePresenter implements HomeContract.Presenter {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates;

    /**
     * Represents a Grade of Hill in percents.
     */
    private double mGradeOfHill;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

    private WeatherService mWeatherService;
    private HomeContract.View mHomeView;
    private AppDatabase mDatabase;
    private SharedPreferences mSharedPreferences;

    private Chronometer mChronometer;

    private double mRiderWeight;
    private double mBikeWeight;
    private double mFrontalArea;
    private double mDragC;
    private double mLossDT;   // drivetrain loss Loss_dt
    private double mCrr; // coefficient of rolling resistance Crr05;
    private double mRho;   // air density (kg / m^3)
    private double mSpeed;     // 35kph for the speed field  ;
    private double mPower;

    private long timeWhenStopped;
    private Position mPosition;
    private Ride mRide;
    private Weather mWeather;
    private boolean paused;

    @SuppressLint("MissingPermission")
    HomePresenter(@NonNull HomeContract.View homeView, WeatherService weatherService, SharedPreferences sharedPreferences, Context context) {
        mContext = context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mSettingsClient = LocationServices.getSettingsClient(mContext);

        mWeatherService = weatherService;

        mCurrentLocation = new Location("asd");

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mCurrentLocation = location;
                            mWeatherService.updateWeather(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                        }
                    }
                });

        paused = false;
        timeWhenStopped = 0;
        mGradeOfHill = 0;

        mContext = context;
        mHomeView = homeView;
        mChronometer = mHomeView.getChmDuration();
        mRide = new Ride();
        mDatabase = AppDatabase.getAppDatabase(mContext);
        mHomeView.setPresenter(this);
        mPosition = RELAXED;

        mLossDT = 3;
        mCrr = 0.005;
        mRho = 1.226;
        mGradeOfHill = 0;
        mSpeed = 35;
        mDragC = 0.63;
        mFrontalArea = 0.5;
        mBikeWeight = 8;
        mRiderWeight = 75;
        mPower = 0;


        mRequestingLocationUpdates = false;

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        mSharedPreferences = sharedPreferences;

        mWeather = mWeatherService.getWeather();
    }

    public void start() {
        mWeather = mWeatherService.getWeather();
    }

    private void calculatePower() {
        //calculate FGravity
        double fGravity = 9.8067 *
                (mRiderWeight + mBikeWeight) *
                Math.sin(Math.atan(mGradeOfHill / 100.0));

        // calculate fRolling
        double fRolling = 9.8067 *
                (mRiderWeight + mBikeWeight) *
                Math.cos(Math.atan(mGradeOfHill / 100.0)) *
                (mCrr);

        // calculate fDrag
        double fDrag = 0.5 *
                (mFrontalArea) *
                (mDragC) *
                (mRho) *
                (mSpeed * 1000.0 / 3600.0) *
                (mSpeed * 1000.0 / 3600.0);


        // Calculates and returns the power needed to achieve the given
        // velocity.  <params> is a dictionary containing the rider and
        // environmental parameters, as returned by ScrapeParameters(), i.e.,
        // all in metric units.  'velocity' is in km/h.  Returns power in
        // watts.


        double totalForce = fGravity + fRolling + fDrag;

        // calculate necessary wheelPower
        double wheelPower = totalForce * (mSpeed * 1000.0 / 3600.0);

        // calculate necessary leg power
        mPower = wheelPower / (1.0 - (mLossDT / 100.0));
    }

    private void setRiderInfoFromSP() {
        mRiderWeight = mSharedPreferences.getInt("riderWeight", 70);
        mBikeWeight = mSharedPreferences.getInt("bikeWeight", 7);
        switch (mSharedPreferences.getInt("bikeTires", 0)) {
            case 0:
                mCrr = 0.005;
                break;
            case 1:
                mCrr = 0.003;
                break;
            default:
                mCrr = 0.005;
                Log.e(TAG, "Wrong Tires");

        }

        switch (mPosition) {
            case RELAXED:
                mFrontalArea = 0.509;
                break;
            case AGGRESSIVE:
                mFrontalArea = 0.409;
                break;
            case AERODYNAMIC:
                mFrontalArea = 0.309;
            default:
                mFrontalArea = 0.509;
                Log.e(TAG, "Wrong Position");
        }

    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (mRequestingLocationUpdates) {
                    float distance = mCurrentLocation.distanceTo(locationResult.getLastLocation());
                    double rise = locationResult.getLastLocation().getAltitude() - mCurrentLocation.getAltitude();
                    double run = (distance * distance) - (rise * rise);
                    mGradeOfHill = rise / run * 100;
                    mCurrentLocation = locationResult.getLastLocation();
                    mSpeed = mCurrentLocation.getSpeed();
                    calculatePower();
                    mRide.addMeasurement(new Measurement(mSpeed, mPower));
                    updateUI();
                }
                //Toast.makeText(mContext, mCurrentLocation + " ", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void updateUI() {
        mHomeView.setArcPower((int) mPower);
        mHomeView.setSpeed(mSpeed);
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                    }
                });


    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied.");
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }
                    }
                });
    }

    @Override
    public void setPosition(Position position) {
        if (position == RELAXED) {
            mPosition = RELAXED;
        } else if (position == AGGRESSIVE) {
            mPosition = AGGRESSIVE;
        } else {
            mPosition = AERODYNAMIC;
        }
    }

    @Override
    public void startRide() {
        mRequestingLocationUpdates = true;
        startLocationUpdates();
        setRiderInfoFromSP();

        paused = false;
        mHomeView.setPauseButton();
        mHomeView.showPauseStopLayout();
        mHomeView.hideStartButton();

        mChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        mChronometer.start();

        mRide = new Ride();
        mRide.setStartDate(Calendar.getInstance().getTime().toString());
        mRide.setWeather(mWeather);
        mWeather = mWeatherService.getWeather();
        if (mWeather != null) {
            mRho = mWeather.getRho();
        }
    }

    @Override
    public void pauseOrContinueRide() {
        if (paused) {
            mHomeView.setPauseButton();
            mChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            mChronometer.start();
            mRequestingLocationUpdates = true;
            startLocationUpdates();
            paused = false;
        } else {
            mHomeView.setContinueButton();
            timeWhenStopped = mChronometer.getBase() - SystemClock.elapsedRealtime();
            mChronometer.stop();
            mRequestingLocationUpdates = false;
            stopLocationUpdates();
            paused = true;
        }

    }

    @Override
    public void stopRide() {
        mHomeView.hidePauseStopLayout();
        mHomeView.showStartButton();
        mRide.calculateAndSetAverages();
        mRide.setEndDate(Calendar.getInstance().getTime().toString());
        mRide.setWeather(mWeather);
        mRide.setDuration((int)(SystemClock.elapsedRealtime() - mChronometer.getBase())/1000);

        mDatabase.rideDao().insertRide(mRide);

        mChronometer.setBase(SystemClock.elapsedRealtime());
        timeWhenStopped = 0;
        mChronometer.stop();
        mRequestingLocationUpdates = false;
        stopLocationUpdates();
    }
}
