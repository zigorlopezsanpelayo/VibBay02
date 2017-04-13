package com.example.zigorlopezsanpelayo.vibbayza;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentoLogin extends Fragment {

    protected Button botonLogin;
    protected EditText emailForm;
    protected EditText passForm;
    protected String emailFormS;
    protected String passFormS;
    protected boolean logeado;

    DatabaseReference refUsuarios =
            FirebaseDatabase.getInstance().getReference()
                    .child("usuarios");

    public FragmentoLogin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment =
        View v = inflater.inflate(R.layout.fragmento_login, container, false);
        return v;
    }

    public void onViewCreated (View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        emailForm = (EditText) v.findViewById(R.id.email);
        passForm = (EditText) v.findViewById(R.id.password);
        botonLogin = (Button) v.findViewById(R.id.botonLogin);
        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailForm.requestFocus();
                emailFormS = emailForm.getText().toString().toLowerCase();
                passFormS = passForm.getText().toString().toLowerCase();

                        refUsuarios.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String email = (String) snapshot.child("email").getValue();
                                    String pass = snapshot.child("pass").getValue().toString();

                                    if (email.equals(emailFormS) && pass.equals(passFormS)) {
                                        Toast validacionUsuarioCorrecta = Toast.makeText(getActivity().getApplicationContext(), "Logeado con éxito", Toast.LENGTH_SHORT);
                                        validacionUsuarioCorrecta.show();
                                        Intent perfil = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
                                        perfil.putExtra("emailUsuario", email);
                                        startActivity(perfil);
                                        logeado = true;
                                    }
                                }

                                if (!logeado) {
                                    Toast validacionUsuarioIncorrecta = Toast.makeText(getActivity().getApplicationContext(), "Usuario o contraseña incorrecto", Toast.LENGTH_SHORT);
                                    validacionUsuarioIncorrecta.show();
                                    emailForm.setText("");
                                    passForm.setText("");
                                }

                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

            }
        });
    }
}

