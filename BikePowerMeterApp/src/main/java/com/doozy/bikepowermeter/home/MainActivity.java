package com.doozy.bikepowermeter.home;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import com.doozy.bikepowermeter.firstrun.FirstRunActivity;
import com.doozy.bikepowermeter.history.HistoryFragment;
import com.doozy.bikepowermeter.services.WeatherService;
import com.doozy.bikepowermeter.services.impl.OpenWeatherMapWeatherServiceImpl;
import com.doozy.bikepowermeter.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected NavigationView navigationView;
    SharedPreferences prefs = null;

    WeatherService service;
    //from
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;


    //end
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = new OpenWeatherMapWeatherServiceImpl(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        prefs = getSharedPreferences("com.doozy.bikepowermeter", MODE_PRIVATE);
        //prefs.edit().clear().apply();
        if (prefs.getBoolean("firstRun", true)) {
            Intent intent = new Intent(this, FirstRunActivity.class);
            startActivity(intent);
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, new HomeFragment()).commit();


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //from here
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {

        }

        service.updateWeather(42, 21);

        //getLocation();


    }

    /*   private void getLocation() {
           if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                   PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                   != PackageManager.PERMISSION_GRANTED) {
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
           } else {
               Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

               if (location != null) {
                   double latti = location.getLatitude();
                   double longi = location.getLongitude();
                   Geocoder geocoder;
                   List<Address> addresses = null;
                   geocoder = new Geocoder(this, Locale.getDefault());

                   try {
                       addresses = geocoder.getFromLocation(latti, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

                   String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                   String city = addresses.get(0).getLocality();
                   String state = addresses.get(0).getAdminArea();
                   String country = addresses.get(0).getCountryName();
                   String postalCode = addresses.get(0).getPostalCode();
                   String knownName = addresses.get(0).getFeatureName();
                   Log.d("primer", address + " " + city + " " + state + " " + country + " " + postalCode + " " + knownName + " hahaaha");
                   //   ((EditText)findViewById(R.id.editTextFirstRunYourWeight)).setText("Latitude "+ latti);
                   //   ((EditText)findViewById(R.id.textViewFirstRunBikeWeight)).setText("Longitude "+ latti);
               }
           }
       }

       @Override
       public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NonNull int[] grantResult) {
           super.onRequestPermissionsResult(requestCode, permissions, grantResult);

           switch (requestCode) {
               case REQUEST_LOCATION:
                   getLocation();
                   break;

           }
       }
   */
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
}