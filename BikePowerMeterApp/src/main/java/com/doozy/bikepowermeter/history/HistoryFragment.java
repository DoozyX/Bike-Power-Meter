package com.doozy.bikepowermeter.history;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.doozy.bikepowermeter.R;
import com.doozy.bikepowermeter.data.AppDatabase;
import com.doozy.bikepowermeter.data.Ride;

/**
 * TODO: Description
 */

public class HistoryFragment extends Fragment implements HistoryContract.View  {
    private View myView;

    private ExpandableListAdapter mListAdapter;
    private ExpandableListView mExpListView;
    private List<String> mListDataHeader;
    private HashMap<String, List<String>> mListDataChild;
    private List<Ride> mListRides;

    private HistoryContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new HistoryPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.history_main,container,false);
        super.onCreate(savedInstanceState);

        // get the listview
        mExpListView = myView.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        mListAdapter = new ExpandableListAdapter(myView.getContext(), mListDataHeader, mListDataChild);

        // setting list adapter
        mExpListView.setAdapter(mListAdapter);

        // Listview Group click listener
        mExpListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        mExpListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        // Listview Group collasped listener
        mExpListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // Listview on child click listener
        mExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        return myView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.nav_history);
    }

    @Override
    public void setPresenter(HistoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /*
    * Preparing the list data
    */
    private void prepareListData() {
        mListDataHeader = new ArrayList<>();
        mListDataChild = new HashMap<>();

        List<Ride> allRides = AppDatabase.getAppDatabase(myView.getContext()).rideDao().getRides();
        for (Ride ride : allRides) {
            mListDataHeader.add(ride.getStartDate());
            List<String> lista = new ArrayList<>();
            lista.add(ride.toString());
            mListDataChild.put(ride.getStartDate(),lista);
        }
    }
}
