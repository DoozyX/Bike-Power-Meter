package com.doozy.bikepowermeter.history;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.doozy.bikepowermeter.R;
import com.doozy.bikepowermeter.data.Ride;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * TODO: Description
 */

public class HistoryFragment extends Fragment implements HistoryContract.View  {
    View myView;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView listView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHash;
    private List<Ride> tmpListRides;
    private HistoryContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("itemsInfoProba", Context.MODE_PRIVATE);
        tmpListRides = new ArrayList<>();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String preferencesStr = sharedPreferences.getString("infoItems1","").toString();
        if(preferencesStr!=""){

            String[] parts = preferencesStr.split("!!");

            for (String p: parts) {
                String[] parts1 = p.split("--");
                Ride i=new Ride();
                tmpListRides.add(i);
            }
            Collections.reverse(tmpListRides);

            listDataHeader = new ArrayList<>();
            listHash = new HashMap<>();


            for (Ride i : tmpListRides) {
                listDataHeader.add(i.getStartDate().toString());
                List<String> lista = new ArrayList<>();
                lista.add(i.toString());
                listHash.put(i.getStartDate().toString(),lista);
            }

        }

        new HistoryPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.history_main,container,false);

        return myView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.nav_history);
        //  lv = (ExpandableListView) view.findViewById(R.id.expListView);
        //lv.setAdapter(new ExpandableListAdapter(groups, children));
        // lv.setGroupIndicator(null);
        listView = (ExpandableListView) view.findViewById(R.id.lvExp);
//ova mozi da ne e vaka
        listAdapter = new ExpandableListAdapter(view.getContext(), listDataHeader, listHash);
        listView.setAdapter(listAdapter);
        listView.setGroupIndicator(null);
    }

    private void initData() {
    }

    @Override
    public void setPresenter(HistoryContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
