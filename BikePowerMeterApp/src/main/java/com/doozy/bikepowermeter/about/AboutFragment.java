package com.doozy.bikepowermeter.about;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doozy.bikepowermeter.R;

/**
 * Created by doozy on 25-Nov-17
 */

public class AboutFragment extends Fragment {
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.about_main,container,false);
        getActivity().setTitle(R.string.nav_about);
        return myView;
    }
}
