package com.example.daleel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.daleel.fragments.CategoriesFragment;
import com.example.daleel.fragments.MapsFragment;
import com.example.daleel.fragments.PlaceFragment;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements PlaceFragment.OnItemSelectedListener, MapsFragment.onCloseClickedListener, CategoriesFragment.OnSearchClickListener{

    ImageButton favPageBtn, submitPageBtn, homePageBtn;
    Button langBtn;
    FrameLayout flContainer;
    ViewGroup.LayoutParams params;
    MySQLiteHelper db = new MySQLiteHelper(this);

    enum Pages
    {
        HOME,
        FAVORITES,
        SPECIFIC
    }

    public static final String GET_FAVORITES = "GET_FAVORITES";
    public static final String GET_SPECIFIC = "GET_SPECIFIC";

    Pages page = Pages.HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activateLangButton();

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
                    args.putString("mode", GET_FAVORITES);
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
    public void onPlaceItemSelectedLong(int id) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("ID", id);
        startActivity(intent);
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

    public void onSearchClicked(String searchText, String category)
    {
        if (!searchText.isEmpty() || category != null)
        {
            removeFragment("placeFragment");
            PlaceFragment placeFragment = new PlaceFragment();
            Bundle args = new Bundle();
            args.putString("mode", GET_SPECIFIC);
            args.putString("word", searchText);
            args.putString("category", category);
            placeFragment.setArguments(args);
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.flContainer2, placeFragment, "placeFragment");
            ft2.commit();

            page = Pages.SPECIFIC;
        }
        else if (page != Pages.HOME)
        {
            removeFragment("placeFragment");
            PlaceFragment placeFragment = new PlaceFragment();
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.flContainer2, placeFragment, "placeFragment");
            ft2.commit();

            page = Pages.HOME;
        }
    }

    public void activateLangButton()
    {
        langBtn = findViewById(R.id.langBtn);

        if (getResources().getConfiguration().locale.toString().equalsIgnoreCase("en_US"))
            langBtn.setText("EN");
        else if (getResources().getConfiguration().locale.toString().equals("ar"))
            langBtn.setText("AR");

        langBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getResources().getConfiguration().locale.toString().equalsIgnoreCase("en_US") || getResources().getConfiguration().locale.toString().equalsIgnoreCase("en")){

                    LocaleHelper.setLocaleV(getBaseContext(), "ar");

                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                } else if (getResources().getConfiguration().locale.toString().equalsIgnoreCase("ar")){

                    LocaleHelper.setLocaleV(getBaseContext(), "en_US");

                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }
}