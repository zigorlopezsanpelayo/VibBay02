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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.acl.Group;

import static com.example.zigorlopezsanpelayo.vibbayza.R.id.email;
import static com.example.zigorlopezsanpelayo.vibbayza.R.id.fragmento_articulos;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String nombreUsuario;

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
        getSupportActionBar().setTitle(getIntent().getExtras().getString("emailUsuario"));

        nombreUsuario = getIntent().getExtras().getString("emailUsuario");


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
                                FragmentoLogin.setLogeado(false);
                                break;
                            case R.id.articulos:
                                Fragment fragmentoArticulos = new FragmentoArticulos();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_main, fragmentoArticulos)
                                        .commit();
                                getSupportActionBar().setTitle("Artículos");
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

    public String getNombreUsuario() {
        return nombreUsuario;
    }
}
