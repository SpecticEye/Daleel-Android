package com.example.daleel;

import static com.example.daleel.DbBitmapUtil.getImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlaceAdapter extends ArrayAdapter<Place> {
    public static final int btn_star_big_off = 17301515;
    public static final int btn_star_big_on = 17301516;
    MySQLiteHelper db = new MySQLiteHelper(getContext());
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
        ImageView image = convertView.findViewById(R.id.image);
        ToggleButton favbtn = convertView.findViewById(R.id.favBtn);

        name.setText(placeList.get(position).getName());
        address.setText(placeList.get(position).getStreet());
        category.setText(placeList.get(position).getCategory());
        Bitmap bitmap = getImage(placeList.get(position).getImage());
        image.setImageBitmap(bitmap);
        if (favbtn.isChecked())
        {
            Drawable drawable = getContext().getResources().getDrawable(btn_star_big_on,null);
            favbtn.setBackground(drawable);
        }

        favbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Drawable drawable = getContext().getResources().getDrawable(btn_star_big_on,null);
                    favbtn.setBackground(drawable);
                    db.addFavorite(placeList.get(position).getId());
                }
                else {
                    Drawable drawable = getContext().getResources().getDrawable(btn_star_big_off,null);
                    favbtn.setBackground(drawable);
                    db.deleteFavorite(placeList.get(position).getId());
                }
            }
        });

        return convertView;
    }
}
