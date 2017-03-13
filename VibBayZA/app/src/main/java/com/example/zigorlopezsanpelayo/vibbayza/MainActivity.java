package com.example.zigorlopezsanpelayo.vibbayza;

import android.app.DownloadManager;
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
    String emailFormS;
    String passFormS;

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

    private class ObtenerDatos extends AsyncTask<String,Integer,Boolean> {

        private String[] usuarios;

        protected Boolean doInBackground(String... params) {
            boolean resul = true;
            boolean logeado = false;
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet("http://10.111.17.185:8084/jsonweb/rest/usuarios");

            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                usuarios = new String[respJSON.length()];

                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);

                    String emailRest = obj.getString("email");
                    String passRest = obj.getString("pass");

                    if (emailRest.equals(emailFormS) && passRest.equals(passFormS)) {
                        Log.i("string","valido");
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast validacionUsuarioCorrecta = Toast.makeText(getApplicationContext(), "Logeado con éxito", Toast.LENGTH_SHORT);
                                validacionUsuarioCorrecta.show();
                            }
                        });
                        Intent perfil = new Intent(getApplicationContext(), ProfileActivity.class);
                        perfil.putExtra("nombreUsuario", emailRest);
                        startActivity(perfil);
                        logeado = true;
                    }
                }
                if (!logeado) {
                    Log.i("string","no valido");
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
                resul = false;
            }

            return resul;
        }

    }

    public void validar(View v) {

        emailForm = (EditText) findViewById(R.id.email);
        passForm = (EditText) findViewById(R.id.password);
        emailFormS = emailForm.getText().toString();
        passFormS = passForm.getText().toString();

        ObtenerDatos datos = new ObtenerDatos();
        datos.execute();
    }
}
