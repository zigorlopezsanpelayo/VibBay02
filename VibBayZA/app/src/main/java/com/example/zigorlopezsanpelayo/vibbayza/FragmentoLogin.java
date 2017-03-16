package com.example.zigorlopezsanpelayo.vibbayza;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentoLogin extends Fragment {

    String BASE_URL = "http://192.168.0.22:8084/jsonweb/rest/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    protected ServicioUsuario servicio = retrofit.create(ServicioUsuario.class);
    protected Button botonLogin;
    protected EditText emailForm;
    protected EditText passForm;
    protected String emailFormS;
    protected String passFormS;
    protected boolean logeado;

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
                Call<List<Usuario>> call = servicio.findAll();
                call.enqueue(new Callback<List<Usuario>>() {
                    @Override
                    public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                        List<Usuario> usuarios = response.body();
                        for (final Usuario usuario : usuarios) {
                            if (usuario.getEmail().equals(emailFormS) && usuario.getPass().equals(passFormS)) {
                                Toast validacionUsuarioCorrecta = Toast.makeText(getActivity().getApplicationContext(), "Logeado con éxito", Toast.LENGTH_SHORT);
                                validacionUsuarioCorrecta.show();
                                Intent perfil = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
                                perfil.putExtra("emailUsuario", usuario.getEmail());
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
                    public void onFailure(Call<List<Usuario>> call, Throwable t) {

                    }
                });
            }
        });
    }
}