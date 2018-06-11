package io.iqube.srinath.imeddispenser.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.iqube.srinath.imeddispenser.R;
import io.iqube.srinath.imeddispenser.models.Schedule;
import io.realm.Realm;
import io.realm.RealmResults;

public class ScheduleListFragment extends Fragment {
    Realm realm;
    ExpandableListView scheduleExpandableListView;
    HashMap<String, List<String>> medicationList;
    List<String> scheduleList;
    ScheduleExpandableListViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_schedule_list, container, false);
        realm = Realm.getDefaultInstance();
        RealmResults<Schedule> realmResults = realm.where(Schedule.class).findAll();
        scheduleList = new ArrayList<>();
        scheduleList.add("Before Breakfast");
        scheduleList.add("After Breakfast");
        scheduleList.add("Before Lunch");
        scheduleList.add("After Lunch");
        scheduleList.add("Before Dinner");
        scheduleList.add("After Dinner");
        medicationList = new HashMap<>();
        for (int i = 0; i < realmResults.size(); i++) {
            Schedule schedule = realmResults.get(i);
            int slot = schedule.getSlot();
            String s = scheduleList.get(slot);
            List<String> composition = medicationList.get(s);
            if (composition == null) {
                composition = new ArrayList<>();
                composition.add(schedule.getComposition().getName() + "-" + schedule.getQty());
                medicationList.put(s,composition);
            } else {
                for (int j = 0; j < composition.size(); j++) {
                    String comp = composition.get(j).split("-")[0];
                    int qty = Integer.parseInt(composition.get(j).split("-")[1]);
                    if (comp.equals(schedule.getComposition().getName())) {
                        qty += schedule.getQty();
                        composition.set(j, comp + "-" + qty);
                    }
                }
            }
        }
        adapter = new ScheduleExpandableListViewAdapter(scheduleList, medicationList, getContext());
        scheduleExpandableListView = v.findViewById(R.id.schedule_list);
        scheduleExpandableListView.setAdapter(adapter);
        return v;
    }
}
