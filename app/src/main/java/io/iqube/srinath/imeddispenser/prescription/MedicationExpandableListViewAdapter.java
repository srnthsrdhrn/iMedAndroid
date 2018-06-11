package io.iqube.srinath.imeddispenser.prescription;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import io.iqube.srinath.imeddispenser.R;

public class MedicationExpandableListViewAdapter extends BaseExpandableListAdapter {
    List<String> medicationList;
    HashMap<String, List<String>> scheduleList;
    Context context;

    public MedicationExpandableListViewAdapter(List<String> medicationList, HashMap<String, List<String>> scheduleList, Context context) {
        this.medicationList = medicationList;
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return medicationList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return scheduleList.get(medicationList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return scheduleList.get(medicationList.get(groupPosition));
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return scheduleList.get(medicationList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        Log.i("Group Position",groupPosition+"");
        return groupPosition;

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        Log.i("Get child ID",groupPosition+" "+childPosition);
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.medication_list_item, parent,false);
            TextView tv = convertView.findViewById(R.id.composition_name);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setText(medicationList.get(groupPosition));
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.schedule_list_item, parent,false);
            TextView scheduletv = convertView.findViewById(R.id.schedule);
            TextView counttv = convertView.findViewById(R.id.count);
            String[] data = scheduleList.get(medicationList.get(groupPosition)).get(childPosition).split("-");
            scheduletv.setText(data[0]);
            counttv.setText(data[1]
            );
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
