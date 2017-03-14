package com.example.zigorlopezsanpelayo.vibbayza;

import android.app.DownloadManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText emailForm;
    EditText passForm;
    EditText buscarForm;
    String emailFormS;
    String passFormS;
    String buscarFormS;
    String buscarFormSLow;
    TextView articulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getMenu().setGroupVisible(7, false);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Fragment fragmentoPrincipal = new FragmentoPrincipal();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragmentoPrincipal)
                .commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {



                        switch (menuItem.getItemId()) {
                            case R.id.login:
                                ponerFragLogin();
                        }



                        drawer.closeDrawers();

                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.buscar:
                ponerFragBusqueda();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.login) {

        } else if (id == R.id.casoUso2) {

        } else if (id == R.id.casoUso3) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void ponerFragBusqueda() {
        boolean fragmentTransaction = false;
        Fragment fragmentoBusqueda = null;

        fragmentoBusqueda = new FragmentoBuscar();
        fragmentTransaction = true;

        if(fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, fragmentoBusqueda)
                    .commit();

            getSupportActionBar().setTitle("Buscar");
        }
    }

    public void ponerFragLogin() {
        boolean fragmentTransaction = false;
        Fragment fragmentoLogin = null;

        fragmentoLogin = new FragmentoLogin();
        fragmentTransaction = true;

        if(fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, fragmentoLogin)
                    .commit();

            getSupportActionBar().setTitle("Login");
        }

    }

    private class ObtenerUsuarios extends AsyncTask<String,Integer,Boolean> {

        private String[] usuarios;

        protected Boolean doInBackground(String... params) {
            boolean resultado = true;
            boolean logeado = false;
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet obtenerUsuarios =
                    new HttpGet("http://192.168.0.16:8084/jsonweb/rest/usuarios");

            obtenerUsuarios.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(obtenerUsuarios);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                usuarios = new String[respJSON.length()];

                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    String emailRest = obj.getString("email");
                    String passRest = obj.getString("pass");

                    if (emailRest.equals(emailFormS) && passRest.equals(passFormS)) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast validacionUsuarioCorrecta = Toast.makeText(getApplicationContext(), "Logeado con éxito", Toast.LENGTH_SHORT);
                                validacionUsuarioCorrecta.show();
                            }
                        });
                        Intent perfil = new Intent(getApplicationContext(), ProfileActivity.class);
                        perfil.putExtra("emailUsuario", emailRest);
                        startActivity(perfil);
                        logeado = true;
                    }
                }
                if (!logeado) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast validacionUsuarioIncorrecta = Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecto", Toast.LENGTH_SHORT);
                            validacionUsuarioIncorrecta.show();
                            emailForm.setText("");
                            passForm.setText("");
                        }
                    });
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resultado = false;
            }

            return resultado;
        }

    }

    public void validar(View v) {

        emailForm = (EditText) findViewById(R.id.email);
        passForm = (EditText) findViewById(R.id.password);
        emailFormS = emailForm.getText().toString();
        passFormS = passForm.getText().toString();

        ObtenerUsuarios datos = new ObtenerUsuarios();
        datos.execute();
    }

    private class ObtenerArticulos extends AsyncTask<String,Integer,Boolean> {

        private String[] articulos;

        protected Boolean doInBackground(String... params) {
            boolean resultado = true;
            boolean encontrado = false;
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet obtenerArticulos =
                    new HttpGet("http://192.168.0.16:8084/jsonweb/rest/articulos");

            obtenerArticulos.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(obtenerArticulos);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                articulos = new String[respJSON.length()];

                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    String titulo = obj.getString("titulo");
                    String tituloLow = titulo.toLowerCase();
                    char[] tituloChar = tituloLow.toCharArray();
                    String tituloParcial = "";

                    for (int j=0; j<tituloChar.length; j++) {
                        tituloParcial = tituloParcial + tituloChar[j];
                        if (buscarFormSLow.equals(tituloParcial)) {
                            Log.i("String", "ENCONTRADO");
                            final String nombreArt = obj.getString("titulo");
                            final String precio = obj.getString("precio");
                            encontrado = true;
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    articulo = new TextView(getApplicationContext());
                                    articulo.setText(nombreArt + "  " + precio + "€");
                                    articulo.setTextSize(35);
                                    articulo.setTextColor(Color.RED);
                                    LinearLayout arts = (LinearLayout) findViewById(R.id.busqueda);
                                    arts.addView(articulo);
                                }
                            });
                        }
                    }
                }
                if (!encontrado) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast articuloNoEncontrado = Toast.makeText(getApplicationContext(), "No hay coincidencias", Toast.LENGTH_SHORT);
                            articuloNoEncontrado.show();
                        }
                    });
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resultado = false;
            }

            return resultado;
        }

    }

    public void buscar(View v) {

        buscarForm = (EditText) findViewById(R.id.campo_buscar);
        buscarFormS = buscarForm.getText().toString();
        buscarFormSLow = buscarFormS.toLowerCase();

        ObtenerArticulos articulos = new ObtenerArticulos();
        articulos.execute();
    }
}
