package com.doozy.bikepowermeter;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by doozy on 25-Nov-17
 */

public class HomeFragment extends Fragment implements LocationListener {
    View myView;
   /* Date date= new Date();
    EditText powerET = (EditText)myView.findViewById(R.id.arcProgressHomePower);
    String power = powerET.getText().toString();
    TextView tvSpeed = (TextView) myView.findViewById(R.id.textViewHomeSpeed);
    String speed= tvSpeed.getText().toString();
    TextView tvDuration = (TextView) myView.findViewById(R.id.chronometerHomeDuration);
    String duration = tvDuration.getText().toString();
    Item item = new Item(date,power,speed,duration);
*/
    //SharedPreferences preferencesF = this.getActivity().getSharedPreferences("MYPREFS", MODE_PRIVATE);
    //SharedPreferences.Editor editor = preferencesF.edit();
    //editor.putString(item.toString);
   // String s= preferencesF.getString("items","").split("\n")[0].toLowerCase();

    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.home_main, container, false);
        getActivity().setTitle(R.string.nav_home);

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }else {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            this.onLocationChanged(null);
        }

        // Set the Text to try this out

        return myView;
    }

    @Override
    public void onLocationChanged(Location location) {
        TextView t = (TextView) myView.findViewById(R.id.textViewHomeSpeed);
        if(location==null){

            t.setText("0 m/s");
        }else{
            float nCurrentSpeed = location.getSpeed();
            t.setText(nCurrentSpeed +" m/s");
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
