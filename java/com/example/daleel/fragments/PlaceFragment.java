package com.example.daleel.fragments;

import static com.example.daleel.MainActivity.GET_FAVORITES;
import static com.example.daleel.MainActivity.GET_SPECIFIC;
import static com.example.daleel.data.Places.places;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.daleel.MainActivity;
import com.example.daleel.MySQLiteHelper;
import com.example.daleel.Place;
import com.example.daleel.PlaceAdapter;
import com.example.daleel.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class PlaceFragment extends Fragment {

    TextView header;
    PlaceAdapter placeAdapter;
    ListView placesListView;
    ArrayList<Place> place_list;
    private OnItemSelectedListener listener;
    MySQLiteHelper db;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "mode";
    private static final String ARG_PARAM2 = "word";
    private static final String ARG_PARAM3 = "category";

    // TODO: Rename and change types of parameters
    private String mMode;
    private String mWord;
    private String mCategory;


    public PlaceFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static PlaceFragment newInstance(String mode, String word, String category) {
        PlaceFragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        if (mode != null)
            args.putString(ARG_PARAM1, mode);
        if (word != null)
            args.putString(ARG_PARAM2, word);
        if (category != null)
            args.putString(ARG_PARAM3, category);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("Range")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMode = getArguments().getString(ARG_PARAM1);
            mWord = getArguments().getString(ARG_PARAM2);
            mCategory = getArguments().getString(ARG_PARAM3);
        }

        db = new MySQLiteHelper(getContext());
        place_list = new ArrayList<Place>();
        placeAdapter = new PlaceAdapter(getContext(), R.layout.place_item, place_list);

        if (mMode == GET_SPECIFIC)
        {
            Cursor cursor = db.searchForPlaces(mWord, mCategory);

            Log.d("place_list", "--------------");

            // Only process a non-null cursor with rows.
            if (cursor != null && cursor.getCount() > 0) {
                // You must move the cursor to the first item.
                cursor.moveToLast();
                int id;
                do {
                    // Get the value from the column for the current cursor.
                    id = cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.KEY_ID));
                    place_list.add(db.getPlace(id));
                } while (cursor.moveToPrevious()); // Returns true or false
                cursor.close();
            } // You should add some handling of null case. Right now, nothing happens.
        }
        else if (mMode == GET_FAVORITES)
            place_list.addAll((ArrayList<Place>)db.getAllFavorites());
        else
            place_list.addAll((ArrayList<Place>)db.getAllPlaces());

        placeAdapter = new PlaceAdapter(getContext(), R.layout.place_item, place_list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_place, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        placesListView = getView().findViewById(R.id.placesList);
        header = getView().findViewById(R.id.header);
        String result_string = "Results for: ";
        String favorites_string = "Favorites";


        if (getContext().getResources().getConfiguration().locale.toString().equals("ar"))
        {
            result_string = "نتائج: ";
            favorites_string = "الأماكن";
        }

        if (mMode == GET_SPECIFIC)
        {
            if (!mWord.isEmpty())
                header.setText(result_string + mWord);
            else
                header.setText(result_string + mCategory + "s");
        }
        else if (mMode == GET_FAVORITES){

            header.setText(favorites_string);
            header.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        placesListView.setAdapter(placeAdapter);

        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // go to activity to load details fragment
                //listener.onPlaceItemSelected(position); // (3) Communicate with Activity using Listener
            }
        });

        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onPlaceItemSelected(place_list.get(position).getId()); // (3) Communicate with Activity using Listener
            }
        });

        placesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                listener.onPlaceItemSelectedLong(place_list.get(position).getId()); // (3) Communicate with Activity using Listener
                return true;
            }
        });
    }

    //--OnItemSelectedListener listener;
    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            this.listener = (OnItemSelectedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement TitleFragment.OnItemSelectedListener");
        }
    }

    public interface OnItemSelectedListener {
        void onPlaceItemSelected(int position);
        void onPlaceItemSelectedLong(int position);
    }

}