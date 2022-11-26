package com.example.daleel.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.daleel.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsFragment extends Fragment {

    private static final String ARG_PARAM1 = "location";
    private String mParam1;
    private LatLng location;
    private Button close;
    private onCloseClickedListener listener;

    public static MapsFragment newInstance(int param1) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        //Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);


        supportMapFragment.getMapAsync(new OnMapReadyCallback() {

            public void onMapReady(GoogleMap googleMap) {
            location = getLocationFromAddress(mParam1);
            //Initialize marker options
            MarkerOptions markerOptions = new MarkerOptions();
            //Set position of marker
            markerOptions.position(location);
            //Set title of marker
            markerOptions.title(mParam1);
            //Remove all marker
            googleMap.clear();
            //Animate to zoom the marker
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    location, 15
            ));
            googleMap.addMarker(markerOptions);

            }
        });

        return view;
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState)
    {
        close = getView().findViewById(R.id.closeBtn);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.closeMap();
            }
        });
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(getContext());
        List<Address> address; LatLng p1 = null;
        try { address = coder.getFromLocationName(strAddress, 5);
            if (address == null) { return null; } Address location = address.get(0); location.getLatitude(); location.getLongitude();
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return p1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MapsFragment.onCloseClickedListener){      // context instanceof YourActivity
            this.listener = (MapsFragment.onCloseClickedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement TitleFragment.OnItemSelectedListener");
        }
    }

    public interface onCloseClickedListener {
        void closeMap();
    }

}