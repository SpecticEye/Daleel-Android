package com.example.daleel.fragments;

import static com.example.daleel.data.Places.places;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

    PlaceAdapter placeAdapter;
    ListView placesListView;
    ArrayList<Place> place_list;
    private OnItemSelectedListener listener;
    MySQLiteHelper db;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    //private String mParam1;

    /*
    public PlaceFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static PlaceFragment newInstance() {
        PlaceFragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new MySQLiteHelper(getContext());
        place_list = new ArrayList<Place>();
        if ((ArrayList<Place>)db.getAllPlaces() != null)
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