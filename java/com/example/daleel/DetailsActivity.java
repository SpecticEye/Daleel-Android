package com.example.daleel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.daleel.fragments.CategoriesFragment;
import com.example.daleel.fragments.MapsFragment;

public class DetailsActivity extends AppCompatActivity implements MapsFragment.onCloseClickedListener{

    int placeID;
    MySQLiteHelper db = new MySQLiteHelper(this);
    Place place;
    Button back, show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        
        Intent intent = getIntent();
        placeID = intent.getIntExtra("ID", 0);
        createMap(placeID);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        show = findViewById(R.id.showMap);
        show.setEnabled(false);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("mapsFragment");

                createMap(placeID);
                show.setEnabled(false);
            }
        });

    }

    @Override
    public void closeMap() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("mapsFragment");

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();

            show.setEnabled(true);
        }
    }

    public void createMap(int id) {
        place = db.getPlace(id);
        String location = place.getName()+ ", " + place.getStreet() + ", " + place.getPostalCode() + " " + place.getCity() + ", " + place.getCountry();
        MapsFragment mapsFragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString("location", location);
        mapsFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flContainer2, mapsFragment, "mapsFragment")
                .addToBackStack(null)
                .commit();
    }
}