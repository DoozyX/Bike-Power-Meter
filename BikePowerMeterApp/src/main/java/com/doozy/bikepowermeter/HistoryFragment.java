package com.doozy.bikepowermeter;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.doozy.bikepowermeter.data.Item;

import java.util.ArrayList;
import java.util.Collections;
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
    private List<Item> tmpListItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("itemsInfoProba", Context.MODE_PRIVATE);
        tmpListItems = new ArrayList<Item>();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String preferencesStr = sharedPreferences.getString("infoItems1","").toString();
        if(preferencesStr!=""){

            String[] parts = preferencesStr.split("!!");
            for (String p:parts) {
                Log.d("kristina", "haha "+p+" haha");
                // Toast.makeText(getActivity(),p,Toast.LENGTH_LONG);
            }

            for (String p: parts) {
                String[] parts1 = p.split("--");
                Item i=new Item(parts1[0],"10 watts", parts1[1],parts1[2]);
                tmpListItems.add(i);
            }
            Collections.reverse(tmpListItems);

            listDataHeader = new ArrayList<>();
            listHash = new HashMap<>();


            for (Item i : tmpListItems) {
                listDataHeader.add(i.getDate());
                List<String> lista = new ArrayList<>();
                lista.add(i.toString());
                listHash.put(i.getDate(),lista);
            }

        }


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
}
