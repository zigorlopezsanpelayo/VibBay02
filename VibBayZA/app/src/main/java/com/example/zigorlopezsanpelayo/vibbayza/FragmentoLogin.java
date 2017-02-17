package com.example.zigorlopezsanpelayo.vibbayza;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentoLogin extends Fragment {

    public FragmentoLogin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista =  inflater.inflate(R.layout.fragmento_login, container, false);

        ((Button) vista.findViewById(R.id.boton_aceptar))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.i("String", "Comprobar datos en BD Fierbase");
                    }
                });

        ((Button) vista.findViewById(R.id.boton_cancelar))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((MainActivity) getActivity()).ponerFragPrincipal();
                    }
                });
        return vista;
    }

}