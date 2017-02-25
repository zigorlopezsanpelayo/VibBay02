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

public class FragmentoAniadirArticulo extends Fragment {

    public FragmentoAniadirArticulo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmento_aniadir_articulo, container, false);
    }

}