package com.example.zigorlopezsanpelayo.vibbayza;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static com.example.zigorlopezsanpelayo.vibbayza.R.menu.main;

public class FragmentoArticulos extends Fragment {

    public FragmentoArticulos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmento_articulos, container, false);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.setGroupVisible(R.id.grupoAniadirArticulo, true);
    }

}