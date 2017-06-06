package com.example.zigorlopezsanpelayo.vibbayza;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;


public class FragmentoBuscar extends Fragment {

    protected Button botonBuscar;
    protected EditText buscarForm;
    protected String buscarFormS;
    protected String titulo;
    protected boolean encontrado;
    protected TextView articuloEncontrado;
    protected LinearLayout arts;
    protected ImageView imagenArticulo;
    protected double pujaMaxima;
    protected long numPujas = 1;
    protected String numPujasS = "1";
    protected Button botonPujar;

    DatabaseReference refArticulos =
            FirebaseDatabase.getInstance().getReference()
                    .child("articulos");

    DatabaseReference refPujas =
            FirebaseDatabase.getInstance().getReference()
                    .child("pujas");

    public FragmentoBuscar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragmento_buscar, container, false);
        return v;
    }

    public void onViewCreated (View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        buscarForm = (EditText) v.findViewById(R.id.campo_buscar);
        buscarForm.requestFocus();
        botonBuscar = (Button) v.findViewById(R.id.boton_buscar);
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarForm.requestFocus();
                encontrado = false;
                arts = (LinearLayout) getView().findViewById(R.id.encontrados);
                arts.removeAllViews();
                buscarFormS = buscarForm.getText().toString().toLowerCase();

                refPujas.getParent().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap: dataSnapshot.getChildren()) {
                            if (snap.getKey().equals("pujas")) {
                                numPujas = snap.getChildrenCount() + 1;
                                numPujasS = Long.toString(numPujas);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                refArticulos.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.child("estado").getValue().toString().equals("false")) {
                                titulo = (String) snapshot.child("titulo").getValue();
                                final double precio = ((Number)snapshot.child("precio").getValue()).doubleValue();
                                final String propietario = (String) snapshot.child("email").getValue();
                                final String nombreImagen = (String) snapshot.child("nombreImagen").getValue();
                                pujaMaxima = (double) snapshot.child("pujaMaxima").getValue();

                                char[] tituloChar = titulo.toLowerCase().toCharArray();
                                String tituloParcial = "";
                                for (int j=0; j<tituloChar.length; j++) {
                                    tituloParcial = tituloParcial + tituloChar[j];
                                    if (buscarFormS.equals(tituloParcial)) {

                                        String imagenB64 = (String) snapshot.child("nombreImagen").getValue();
                                        byte[] imagenByte = Base64.decode(imagenB64, Base64.DEFAULT);
                                        Bitmap imagen = BitmapFactory.decodeByteArray(imagenByte , 0, imagenByte.length);
                                        imagenArticulo = new ImageView(getActivity().getApplicationContext());
                                        imagenArticulo.setImageBitmap(imagen);
                                        encontrado = true;
                                        articuloEncontrado = new TextView(getActivity().getApplicationContext());
                                        articuloEncontrado.setText(titulo + "  " + precio + "€");
                                        articuloEncontrado.setTextSize(20);
                                        articuloEncontrado.setTextColor(Color.parseColor("#000000"));
                                        LinearLayout art = new LinearLayout(getActivity().getApplicationContext());
                                        botonPujar = new Button(getActivity().getApplicationContext());
                                        botonPujar.setText("Pujar");
                                        botonPujar.setBackgroundColor(Color.parseColor("#F8BBD0"));
                                        botonPujar.setPadding(10, 10, 10, 10);
                                        botonPujar.setTag(titulo);
                                        botonPujar.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (FragmentoLogin.getLogeado()) {
                                                    refPujas.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                                                if (snapshot.child("titulo").getValue().toString().equals(titulo)) {
                                                                    if ((Double.parseDouble(snapshot.child("cantidad").getValue().toString()) ) > pujaMaxima) {
                                                                        pujaMaxima = Double.parseDouble(snapshot.child("cantidad").getValue().toString());
                                                                    }
                                                                }

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                                    alertDialog.setTitle("Puja");
                                                    alertDialog.setMessage("Introduce tu puja");

                                                    final EditText cantidadPuja = new EditText(getContext());
                                                    cantidadPuja.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                                            LinearLayout.LayoutParams.MATCH_PARENT);
                                                    cantidadPuja.setLayoutParams(lp);
                                                    alertDialog.setView(cantidadPuja);

                                                    alertDialog.setPositiveButton("Pujar",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    try {
                                                                        Double.parseDouble(cantidadPuja.getText().toString());
                                                                    }
                                                                    catch (Exception e){

                                                                    }
                                                                    DecimalFormat decim = new DecimalFormat("0.00");
                                                                    double puja = Double.parseDouble(cantidadPuja.getText().toString());
                                                                    puja = Double.parseDouble(decim.format(puja));
                                                                    String nombreUsuario = ((ProfileActivity)getActivity()).getNombreUsuario();
                                                                    if (nombreUsuario.equals(propietario)) {
                                                                        Toast articuloPropio = Toast.makeText(getActivity().getApplicationContext(), "No puedes pujar por tus propios artículos", Toast.LENGTH_SHORT);
                                                                        articuloPropio.show();
                                                                    }
                                                                    else if (String.valueOf(puja).equals("")) {
                                                                        Toast pujaVacia = Toast.makeText(getActivity().getApplicationContext(), "Debes introducir una cantidad", Toast.LENGTH_SHORT);
                                                                        pujaVacia.show();
                                                                    }
                                                                    else if (!(puja < precio) && (puja > pujaMaxima)) {
                                                                        aniadirPuja(numPujasS, nombreUsuario, puja, titulo);
                                                                        Toast pujaExitosa = Toast.makeText(getActivity().getApplicationContext(), "Puja realizada correctamente", Toast.LENGTH_SHORT);
                                                                        pujaExitosa.show();

                                                                        boolean fragmentTransaction = false;

                                                                        FragmentoPrincipal fragmentoPrincipal = new FragmentoPrincipal();
                                                                        fragmentTransaction = true;

                                                                        if(fragmentTransaction) {
                                                                            getFragmentManager().beginTransaction().addToBackStack("fragmentoBuscar");
                                                                            Fragment fragmentPrevio = getFragmentManager().findFragmentByTag("fragmentoBuscar");
                                                                            getFragmentManager().beginTransaction()
                                                                                    .replace(R.id.content_main, fragmentoPrincipal)
                                                                                    .commit();

                                                                            ((ProfileActivity)getActivity()).getSupportActionBar().setTitle("Pujas");

                                                                            getFragmentManager().beginTransaction().remove(fragmentPrevio);
                                                                        }
                                                                    }
                                                                    else if (((puja < pujaMaxima)) && ((puja != 0))) {
                                                                        Toast pujaMinima = Toast.makeText(getActivity().getApplicationContext(), "la puja debe ser mayor que " + pujaMaxima + " €", Toast.LENGTH_SHORT);
                                                                        pujaMinima.show();
                                                                    }
                                                                    else {
                                                                        Toast pujaMinima = Toast.makeText(getActivity().getApplicationContext(), "la puja mínima es de " + precio + " €", Toast.LENGTH_SHORT);
                                                                        pujaMinima.show();
                                                                    }
                                                                }
                                                            });

                                                    alertDialog.setNegativeButton("Cancelar",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.cancel();
                                                                }
                                                            });

                                                    alertDialog.show();
                                                }
                                                else {
                                                    Toast debesLogearte = Toast.makeText(getActivity().getApplicationContext(), "Debes logearte para poder pujar", Toast.LENGTH_SHORT);
                                                    debesLogearte.show();

                                                    boolean fragmentTransaction = false;

                                                    FragmentoLogin fragmentoLogin = new FragmentoLogin();
                                                    fragmentTransaction = true;

                                                    if(fragmentTransaction) {
                                                        getFragmentManager().beginTransaction().addToBackStack("fragmentoBuscar");
                                                        Fragment fragmentPrevio = getFragmentManager().findFragmentByTag("fragmentoBuscar");
                                                        getFragmentManager().beginTransaction()
                                                                .replace(R.id.content_main, fragmentoLogin)
                                                                .commit();

                                                        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Login");

                                                        getFragmentManager().beginTransaction().remove(fragmentPrevio);
                                                    }
                                                }
                                            }
                                        });
                                        art.setOrientation(LinearLayout.VERTICAL);
                                        art.setGravity(Gravity.CENTER_HORIZONTAL);
                                        art.setBackgroundColor(Color.parseColor("#E3F2FD"));
                                        art.addView(articuloEncontrado);
                                        art.addView(imagenArticulo);
                                        art.addView(botonPujar);
                                        imagenArticulo.getLayoutParams().height = 350;
                                        imagenArticulo.getLayoutParams().width = 500;
                                        imagenArticulo.setImageBitmap(imagen);
                                        arts.addView(art);

                                    }
                                }
                            }

                        }
                        if (!encontrado) {
                            Toast articuloNoEncontrado = Toast.makeText(getActivity().getApplicationContext(), "No hay coincidencias", Toast.LENGTH_SHORT);
                            articuloNoEncontrado.show();
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }

    private void aniadirPuja(String pujaId, String email, double cantidad, String titulo) {
        Puja puja = new Puja(email, cantidad, titulo);
        refPujas.child(pujaId).setValue(puja);
    }


}