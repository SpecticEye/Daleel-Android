package com.example.daleel;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlaceAdapter extends ArrayAdapter<Place> {
    // provide adapter with the required data
    private ArrayList<Place> placeList; // copy of the data from the main activity
    public PlaceAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Place> objects) {
        // context will be the main activity
        // resource is the link to the book list item layout
        // objects is the array of books
        super(context, resource, (List<Place>) objects);
        this.placeList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.place_item, parent, false);
        // get old data from existing view
        TextView name = convertView.findViewById(R.id.name);
        TextView address = convertView.findViewById(R.id.address);
        TextView category = convertView.findViewById(R.id.category);

        name.setText(placeList.get(position).getName());
        address.setText(placeList.get(position).getAddress());
        category.setText(placeList.get(position).getCategory());

        return convertView;
    }
}
