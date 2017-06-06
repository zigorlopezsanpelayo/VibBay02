package com.example.zigorlopezsanpelayo.vibbayza;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
    protected double precioInicial;
    protected ImageView imagenArticulo;
    protected double pujaMasAlta = 0;
    protected String imagenB64 = "";
    protected LinearLayout pujas;
    protected LinearLayout puja;

    DatabaseReference refArticulos =
            FirebaseDatabase.getInstance().getReference()
                    .child("articulos");

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
        refPujas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final String nombreArticuloPujado = (String) snapshot.child("titulo").getValue();
                    double cantidad = Double.parseDouble(snapshot.child("cantidad").getValue().toString());
                    String pujador = (String) snapshot.child("email").getValue();
                    String emailUsuario = FragmentoLogin.getEmailLogeado();

                    if (pujador.equals(emailUsuario)) {
                        refArticulos.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                    if (nombreArticuloPujado.equals(snapshot.child("titulo").getValue().toString())) {
                                        precioInicial = (double) snapshot.child("precio").getValue();
                                        imagenB64 = (String) snapshot.child("nombreImagen").getValue();

                                        refPujas.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot2) {
                                                for (DataSnapshot snapshot: dataSnapshot2.getChildren()) {
                                                    double cantidad = ((Number)snapshot.child("cantidad").getValue()).doubleValue();
                                                    if (cantidad > pujaMasAlta) {
                                                        pujaMasAlta = cantidad;

                                                        pujaEncontrada = new TextView(getActivity().getApplicationContext());
                                                        pujaEncontrada.setText(nombreArticuloPujado + "\nPRECIO INICIAL: " + precioInicial + "\nTU PUJA MAS ALTA: " + cantidad + " €" + "\nPUJA MAS ALTA: " + pujaMasAlta + " €");
                                                        pujaEncontrada.setBackgroundColor(Color.WHITE);
                                                        pujaEncontrada.setTextColor(Color.BLACK);
                                                        pujaEncontrada.setTextSize(30);
                                                        byte[] imagenByte = Base64.decode(imagenB64, Base64.DEFAULT);
                                                        Bitmap imagen = BitmapFactory.decodeByteArray(imagenByte , 0, imagenByte.length);
                                                        imagenArticulo = new ImageView(getActivity().getApplicationContext());

                                                        pujas = (LinearLayout) getView().findViewById(R.id.pujas_realizadas);
                                                        puja = new LinearLayout(getActivity().getApplicationContext());

                                                        puja.setOrientation(LinearLayout.HORIZONTAL);
                                                        puja.setBackgroundColor(Color.parseColor("#E3F2FD"));
                                                        puja.addView(pujaEncontrada);
                                                        puja.addView(imagenArticulo);
                                                        imagenArticulo.getLayoutParams().height = 350;
                                                        imagenArticulo.getLayoutParams().width = 500;
                                                        imagenArticulo.setImageBitmap(imagen);
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
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}