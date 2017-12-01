package com.doozy.bikepowermeter;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
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
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected NavigationView navigationView;
    SharedPreferences prefs = null;

    Chronometer chronometer;
    long timeWhenStopped = 0;

    ArcProgress arcProgressHomePower;


    //from
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;


    //end
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    public void btnHomeStartOnClick(View view) {
        LinearLayout linearLayout = findViewById(R.id.linearLayoutHomeBottomButtons);
        linearLayout.setVisibility(View.VISIBLE);

        view.setVisibility(View.INVISIBLE);

        arcProgressHomePower = findViewById(R.id.arcProgressHomePower);
        chronometer = findViewById(R.id.chronometerHomeDuration);

        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        chronometer.start();

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override
            public void onChronometerTick(Chronometer chronometer) {
                arcProgressHomePower.setProgress((int)Math.abs((Math.sin(((int) (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000))*13)));
            }

        });
    }

    public void btnHomePauseContinueOnClick(View view) {
        Button button = findViewById(R.id.btnHomePauseContinue);
        if (button.getText().toString().equals(getResources().getString(R.string.continue_text))) {
            button.setText(getResources().getString(R.string.pause));
            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            chronometer.start();

        } else {
            button.setText(getResources().getString(R.string.continue_text));
            timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
            chronometer.stop();
        }
    }

    public void getTheInformations(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("itemsInfoProba",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        TextView tvSpeed = (TextView) findViewById(R.id.textViewHomeSpeed);
        String speed= tvSpeed.getText().toString();
        Date date = new Date();
        TextView tvDuration = (TextView)findViewById(R.id.chronometerHomeDuration);
        String duration = tvDuration.getText().toString();
        if(sharedPreferences.getString("infoItems1","")!=""){
            String tmp = sharedPreferences.getString("infoItems1","");
            tmp+=date+"--"+speed+"--"+duration+"!!";
            editor.putString("infoItems1",tmp);
            editor.apply();
        }else{
            editor.putString("infoItems1",date+"--"+speed+"--"+duration+"!!");
            editor.apply();
        }

        //Toast.makeText(view.getContext(),sharedPreferences.getString("infoItems1","").toString(), Toast.LENGTH_LONG).show();

    }

    public void btnHomeStopOnClick(View view) {
        LinearLayout linearLayout = findViewById(R.id.linearLayoutHomeBottomButtons);
        linearLayout.setVisibility(View.INVISIBLE);

        getTheInformations(view);
        Button button = findViewById(R.id.btnHomeStart);
        button.setVisibility(View.VISIBLE);



        chronometer.setBase(SystemClock.elapsedRealtime());
        timeWhenStopped = 0;
        chronometer.stop();
    }

}