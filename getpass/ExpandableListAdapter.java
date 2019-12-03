package com.tomi5548319.getpass;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(childPosition == 0){
                convertView = infalInflater.inflate(R.layout.list_item0, null);
            }else if(childPosition == 1 || childPosition == 2 || childPosition == 3){
                convertView = infalInflater.inflate(R.layout.list_item1, null);
            }else if(childPosition == 4 || childPosition == 5){
                convertView = infalInflater.inflate(R.layout.list_item2, null);
            }else if(childPosition == 6){
                convertView = infalInflater.inflate(R.layout.list_item3, null);
            }

        }

        // 0
        if(childPosition == 0){
            TextView textView0 = (TextView) convertView.findViewById(R.id.textView_list_item0);
            textView0.setText(childText);

        }else if(childPosition == 1 || childPosition == 2 || childPosition == 3){ // 1
            Switch switch1 = (Switch) convertView.findViewById(R.id.switch_list_item1);
            switch1.setText(childText + "                                                   ");

        }else if(childPosition == 4 || childPosition == 5){ // 2

            char[] childTextCharArray = childText.toCharArray();
            boolean foundComma = false;
            int commaPosition = 0;

            for(int i=0; i<childTextCharArray.length && !foundComma; i++){
                if(childTextCharArray[i] == ','){
                    foundComma = true;
                    commaPosition = i;
                }
            }

            char[] part1 = new char[commaPosition];
            char[] part2 = new char[childTextCharArray.length - commaPosition - 1];

            for(int i=0; i<part1.length; i++){
                part1[i] = childTextCharArray[i];
            }

            for(int i=0; i<part2.length; i++){
                part2[i] = childTextCharArray[i + commaPosition + 1];
            }

            Switch switch2 = (Switch) convertView.findViewById(R.id.switch_list_item2);
            switch2.setText(new String(part1) + "                       ");

            TextView textView2 = (TextView) convertView.findViewById(R.id.textView_list_item2);
            textView2.setText(new String(part2));

        }else if(childPosition == 6){ // 3
            TextView textView3 = (TextView) convertView.findViewById(R.id.textView_list_item3);
            textView3.setText(childText);
        }

        /*TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);*/

        //txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        if(childPosition == 0){
            return 0;
        }else if(childPosition == 1 || childPosition == 2 || childPosition == 3){
            return 1;
        }else if(childPosition == 4 || childPosition == 5){
            return 2;
        }else if(childPosition == 6){
            return 3;
        }else{
            return 0;
        }
    }

    @Override
    public int getChildTypeCount() {
        return 4;
    }

}
