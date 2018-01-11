package com.doozy.bikepowermeter.home;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.doozy.bikepowermeter.R;
import com.doozy.bikepowermeter.about.AboutFragment;
import com.doozy.bikepowermeter.data.Measurement;
import com.doozy.bikepowermeter.firstrun.FirstRunActivity;
import com.doozy.bikepowermeter.history.HistoryFragment;
import com.doozy.bikepowermeter.services.WeatherService;
import com.doozy.bikepowermeter.services.impl.OpenWeatherMapWeatherServiceImpl;
import com.doozy.bikepowermeter.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener{

    protected NavigationView navigationView;
    WeatherService service;
    static final int REQUEST_LOCATION = 1;
    LocationManager lm;
    int cnt=0;
    SharedPreferences prefs = null;
    SharedPreferences.Editor editor;
    String ride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("com.doozy.bikepowermeter", MODE_PRIVATE);
        editor = prefs.edit();

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ride=prefs.getString("thisRide","0");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else{
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (prefs.getString(ride+"canWrite","0").equals("true")) {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                cnt++;
            }
        }

        this.onLocationChanged(null);

        service = new OpenWeatherMapWeatherServiceImpl(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //prefs.edit().clear().apply();
        if (prefs.getBoolean("firstRun", true)) {
            Intent intent = new Intent(this, FirstRunActivity.class);
            startActivity(intent);
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, new HomeFragment()).commit();


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        service.updateWeather(42, 21);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fragmentManager.beginTransaction().replace(R.id.content_main, new SettingsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_home) {
            // Handle the camera action
            fragmentManager.beginTransaction().replace(R.id.content_main, new HomeFragment()).commit();
        } else if (id == R.id.nav_history) {
            fragmentManager.beginTransaction().replace(R.id.content_main, new HistoryFragment()).commit();
        } else if (id == R.id.nav_settings) {
            fragmentManager.beginTransaction().replace(R.id.content_main, new SettingsFragment()).commit();
        } else if (id == R.id.nav_about) {
            fragmentManager.beginTransaction().replace(R.id.content_main, new AboutFragment()).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        //Updates everySecond, it's passing the current speed to the given ride

        double speed;
        if(location == null){
            speed = -1.0;
        }else{
            float nCurrentSpeed = location.getSpeed();
            cnt++;
            speed = nCurrentSpeed;
            String tmp="";
            if(!prefs.getString("thisRide","0").equals("0"))
                tmp=prefs.getString("thisRide","0");
            editor.remove("thisRide");
            tmp+=" "+nCurrentSpeed;
            editor.putString("thisRide",tmp);
            editor.apply();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}