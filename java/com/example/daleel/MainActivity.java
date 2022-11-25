package com.example.daleel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.daleel.fragments.CategoriesFragment;
import com.example.daleel.fragments.DetailsFragment;
import com.example.daleel.fragments.MapsFragment;
import com.example.daleel.fragments.PlaceFragment;

public class MainActivity extends AppCompatActivity implements PlaceFragment.OnItemSelectedListener, MapsFragment.onCloseClickedListener{

    Button favPageBtn, submitPageBtn, homePageBtn;
    FrameLayout flContainer;
    ViewGroup.LayoutParams params;
    MySQLiteHelper db = new MySQLiteHelper(this);

    enum Pages
    {
        HOME,
        FAVORITES
    }

    public static final String GET_FAVORITES = "GET_FAVORITES";

    Pages page = Pages.HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flContainer = findViewById(R.id.flContainer);

        CategoriesFragment categoriesFragment = new CategoriesFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.flContainer, categoriesFragment, "categoriesFragment");
        ft.commit();

        params = flContainer.getLayoutParams();

        PlaceFragment placeFragment = new PlaceFragment();
        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        ft2.add(R.id.flContainer2, placeFragment, "placeFragment");
        ft2.commit();

        homePageBtn = findViewById(R.id.home);
        favPageBtn = findViewById(R.id.favBtn);
        submitPageBtn = findViewById(R.id.add);

        homePageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page != Pages.HOME)
                {
                    CategoriesFragment categoriesFragment = new CategoriesFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.flContainer, categoriesFragment, "categoriesFragment");
                    ft.commit();

                    flContainer.setLayoutParams(params);

                    PlaceFragment placeFragment = new PlaceFragment();
                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                    ft2.replace(R.id.flContainer2, placeFragment, "placeFragment");
                    ft2.commit();

                    page = Pages.HOME;
                }
            }
        });

        favPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page != Pages.FAVORITES) {
                    removeFragment("categoriesFragment");
                    removeFragment("mapsFragment");

                    flContainer.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    PlaceFragment placeFragment = new PlaceFragment();
                    Bundle args = new Bundle();
                    args.putString("favorites", GET_FAVORITES);
                    placeFragment.setArguments(args);
                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                    ft2.replace(R.id.flContainer2, placeFragment, "placeFragment");
                    ft2.commit();

                    page = Pages.FAVORITES;
                }
            }
        });

        submitPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SubmitActivity.class);
                startActivityForResult(intent, 1);
            }});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==1 && data.getBooleanExtra("SUCCESS", false))
        {
            removeFragment("placeFragment");
            PlaceFragment placeFragment = new PlaceFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.flContainer2, placeFragment, "placeFragment");
            ft.commit();
            Toast.makeText(MainActivity.this, "New place added", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPlaceItemSelected(int id) {
        Toast.makeText(this, "Called by Fragment A: position - " + id, Toast.LENGTH_SHORT).show();

        removeFragment("categoriesFragment");
        flContainer.setLayoutParams(params);
        Place place = db.getPlace(id);
        String location = place.getName()+ ", " + place.getStreet() + ", " + place.getPostalCode() + " " + place.getCity() + ", " + place.getCountry();
        MapsFragment mapsFragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString("location", location);
        mapsFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContainer, mapsFragment, "mapsFragment")
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

    @Override
    public void closeMap() {
        removeFragment("mapsFragment");

        if (page == Pages.HOME)
        {
            CategoriesFragment categoriesFragment = new CategoriesFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.flContainer, categoriesFragment, "categoriesFragment");
            ft.commit();
        }
        else
        {
            flContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }

    }

    public void removeFragment(String tag)
    {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    public void clearAllFragments()
    {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }
}