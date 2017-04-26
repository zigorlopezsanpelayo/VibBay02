package com.example.zigorlopezsanpelayo.vibbayza;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.renderscript.ScriptIntrinsicBLAS.RIGHT;
import static android.view.Gravity.LEFT;

public class FragmentoPujas extends Fragment {

    protected TextView pujaEncontrada;

    DatabaseReference refPujas =
            FirebaseDatabase.getInstance().getReference()
                    .child("pujas");

    public FragmentoPujas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmento_pujas, container, false);

    }

    @Override
    public void onViewCreated (View v, Bundle savedInstanceState) {
        final String emailUsuario = getActivity().getIntent().getExtras().getString("emailUsuario");
        refPujas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String nombreArticuloPujado = (String) snapshot.child("titulo").getValue();
                    String cantidad = (String) snapshot.child("cantidad").getValue();
                    String pujador = (String) snapshot.child("email").getValue();

                    if (pujador.equals(emailUsuario)) {
                        pujaEncontrada = new TextView(getActivity().getApplicationContext());
                        pujaEncontrada.setText(nombreArticuloPujado+ "    " + cantidad + "€");
                        pujaEncontrada.setBackgroundColor(Color.WHITE);
                        pujaEncontrada.setTextColor(Color.BLACK);
                        pujaEncontrada.setTextSize(30);

                        LinearLayout pujas = (LinearLayout) getView().findViewById(R.id.pujas_realizadas);
                        LinearLayout puja = new LinearLayout(getActivity().getApplicationContext());

                        puja.setOrientation(LinearLayout.HORIZONTAL);
                        puja.setBackgroundColor(Color.parseColor("#E3F2FD"));
                        puja.addView(pujaEncontrada);
                        pujas.addView(puja);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}