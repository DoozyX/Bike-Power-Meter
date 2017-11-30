package com.doozy.bikepowermeter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by doozy on 25-Nov-17
 */

public class HistoryFragment extends Fragment {
    View myView;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView listView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHash;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

//Tuka ke gi zema od shared preferences ke ima lista so itemi i vo foreach ke gi izminva
        Date date=new Date();
        Item item=new Item(date, "0:0:30","0:0:30","30");
        Item item1=new Item(date, "0:0:20","0:0:20","30");
        Item item2=new Item(date, "0:0:10","0:0:40","50");
        listDataHeader.add(item.getDate().toString());
        listDataHeader.add(item1.getDate().toString());
        listDataHeader.add(item2.getDate().toString());


        List<String> lista = new ArrayList<>();
        lista.add(item.toString());

        List<String> lista1 = new ArrayList<>();
        lista1.add(item1.toString());

        List<String> lista2 = new ArrayList<>();
        lista2.add(item2.toString());

        listHash.put(listDataHeader.get(0),lista);
        listHash.put(listDataHeader.get(1),lista1);
        listHash.put(listDataHeader.get(2),lista2);


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
}
