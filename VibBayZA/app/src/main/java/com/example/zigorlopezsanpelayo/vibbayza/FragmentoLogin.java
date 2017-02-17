package com.example.zigorlopezsanpelayo.vibbayza;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentoLogin extends Fragment {

    public FragmentoLogin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista =  inflater.inflate(R.layout.fragmento_login, container, false);

        ((Button) vista.findViewById(R.id.aceptar))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        pulsadoBoton(v);
                    }
                });

        return vista;
    }
}