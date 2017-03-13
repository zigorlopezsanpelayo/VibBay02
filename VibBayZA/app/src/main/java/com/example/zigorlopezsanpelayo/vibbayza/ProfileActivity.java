package com.example.zigorlopezsanpelayo.vibbayza;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.acl.Group;

import static com.example.zigorlopezsanpelayo.vibbayza.R.id.email;
import static com.example.zigorlopezsanpelayo.vibbayza.R.id.fragmento_articulos;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView articulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Fragment fragmentoPrincipal = new FragmentoPrincipal();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragmentoPrincipal)
                .commit();
        getSupportActionBar().setTitle(getIntent().getExtras().getString("nombreUsuario"));


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_profile);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.logout:
                                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(main);
                                break;
                            case R.id.articulos:
                                Fragment fragmentoArticulos = new FragmentoArticulos();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_main, fragmentoArticulos)
                                        .commit();
                                ObtenerArticulos articulos = new ObtenerArticulos();
                                articulos.execute();
                                getSupportActionBar().setTitle("Artícluos");
                                break;
                            case R.id.pujas:
                                Fragment fragmentoPujas = new FragmentoPujas();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_main, fragmentoPujas)
                                        .commit();

                                getSupportActionBar().setTitle("Pujas");
                                break;
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
            case R.id.aniadirArticulo:
                ponerFragAniadirArticulo();
                return true;
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

        if (id == R.id.logout) {

        } else if (id == R.id.articulos) {

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

    public void ponerFragAniadirArticulo() {
        boolean fragmentTransaction = false;
        Fragment fragmentoAniadirArticulo = null;

        fragmentoAniadirArticulo = new FragmentoAniadirArticulo();
        fragmentTransaction = true;

        if(fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, fragmentoAniadirArticulo)
                    .commit();

            getSupportActionBar().setTitle("Añadir artículo");
        }
    }

    private class ObtenerArticulos extends AsyncTask<String,Integer,Boolean> {

        private String[] articulos;

        protected Boolean doInBackground(String... params) {
            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet("http://192.168.0.16:8084/jsonweb/rest/articulos");

            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                articulos = new String[respJSON.length()];

                for(int i=0; i<respJSON.length(); i++) {

                    JSONObject obj = respJSON.getJSONObject(i);

                    final String nombreArt = obj.getString("titulo");
                    final String precio = obj.getString("precio");
                    String emailRest = obj.getString("email");

                    Log.i("string", nombreArt);
                    Log.i("string", emailRest);

                    if (emailRest.equals("{\"email\":\"z\",\"pass\":\"z\"}")) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                articulo = new TextView(ProfileActivity.this);
                                articulo.setText(nombreArt + "  " + precio + "€");
                                articulo.setTextSize(35);
                                articulo.setTextColor(Color.RED);
                                LinearLayout arts = (LinearLayout) findViewById(R.id.fragmento_articulos);
                                arts.addView(articulo);
                            }
                        });
                    }
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

}
