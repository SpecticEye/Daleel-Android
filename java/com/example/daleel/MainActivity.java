package com.example.daleel;

import static com.example.daleel.data.Places.places;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.daleel.fragments.DetailsFragment;
import com.example.daleel.fragments.MapsFragment;
import com.example.daleel.fragments.PlaceFragment;

public class MainActivity extends AppCompatActivity implements PlaceFragment.OnItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlaceFragment placeFragment = new PlaceFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.flContainer, placeFragment);
        ft.commit();


    }

    @Override
    public void onPlaceItemSelected(int position) {
        Toast.makeText(this, "Called by Fragment A: position - " + position, Toast.LENGTH_SHORT).show();

        MapsFragment mapsFragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString("location", places[position][1]);
        mapsFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContainer2, mapsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onPlaceItemSelectedLong(int position) {
        Toast.makeText(this, "Called by Fragment A: position - " + position, Toast.LENGTH_SHORT).show();

        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        detailsFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContainer, detailsFragment)
                .addToBackStack(null)
                .commit();
    }
}