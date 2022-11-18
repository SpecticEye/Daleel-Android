package com.example.daleel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

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


    }
}