package com.seawindsolution.podphotographer.Fragemnt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seawindsolution.podphotographer.R;

import java.util.ArrayList;

/**
 * Created by Ronak Gopani on 19/11/19 at 9:55 AM.
 */
class GridAdapter extends ArrayAdapter {

    ArrayList arrayList = new ArrayList<>();

    GridAdapter(Context context, int textViewResourceId, ArrayList objects) {
         super(context, textViewResourceId, objects);
        arrayList = objects;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_items, parent, false);
        }

        TextView text = view.findViewById(R.id.r_1);

        text.setText((CharSequence) arrayList.get(position));

        return view;
    }
}