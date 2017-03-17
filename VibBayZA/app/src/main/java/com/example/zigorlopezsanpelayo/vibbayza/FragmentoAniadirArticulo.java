package com.example.zigorlopezsanpelayo.vibbayza;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static com.example.zigorlopezsanpelayo.vibbayza.R.menu.main;

public class FragmentoAniadirArticulo extends Fragment {

    String BASE_URL = "http://192.168.0.22:8084/jsonweb/rest/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    protected ServicioArticulo servicio = retrofit.create(ServicioArticulo.class);
    protected Button botonAniadirArticulo;
    protected EditText nombreArticulo;
    protected EditText precioArticulo;
    protected String nombreArticuloS;
    protected String precioArticuloS;
    protected float precioArticuloF;
    private static final int RESULT_LOAD_IMAGE = 1;
    protected ImageView imagenASubir;
    protected String imagenB64 = "";

    public FragmentoAniadirArticulo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragmento_aniadir_articulo, container, false);
        return v;
    }

    public void onViewCreated (View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        imagenASubir = (ImageView) getView().findViewById(R.id.imagenASubir);
        imagenASubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galeria, RESULT_LOAD_IMAGE);
                }
            }
        });
        final String emailUsuario = getActivity().getIntent().getExtras().getString("emailUsuario");
        nombreArticulo = (EditText) v.findViewById(R.id.campo_nombre_articulo);
        precioArticulo = (EditText) v.findViewById(R.id.campo_precio_articulo);
        botonAniadirArticulo = (Button) v.findViewById(R.id.boton_aniadir_articulo);
        botonAniadirArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreArticulo.requestFocus();
                nombreArticuloS = nombreArticulo.getText().toString();
                precioArticuloS = precioArticulo.getText().toString();
                Log.i("String", nombreArticuloS);
                Log.i("String", precioArticuloS);
                if (nombreArticuloS.equals("") || precioArticuloS.equals("") || imagenB64.equals("")) {
                    Toast precioObligatorio = Toast.makeText(getActivity().getApplicationContext(), "Debes rellenar todos los campos", Toast.LENGTH_SHORT);
                    precioObligatorio.show();
                }
                else {
                    nombreArticuloS = nombreArticulo.getText().toString();
                    precioArticuloF = Float.parseFloat(precioArticulo.getText().toString());
                    Articulos articulo = new Articulos(3, nombreArticuloS, imagenB64, emailUsuario, false, precioArticuloF);
                    Call<Articulos> call = servicio.create(articulo);
                    call.enqueue(new Callback<Articulos>() {
                        @Override
                        public void onResponse(Call<Articulos> call, Response<Articulos> response) {
                            Toast articuloSubido = Toast.makeText(getActivity().getApplicationContext(), "Artículo publicado", Toast.LENGTH_SHORT);
                            articuloSubido.show();
                        }
                        @Override
                        public void onFailure(Call<Articulos> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private boolean checkPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, permissionCheck);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imagenSeleccionada = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContext().getContentResolver().query(imagenSeleccionada, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] imagenByte = byteArrayOutputStream.toByteArray();
            imagenB64 = Base64.encodeToString(imagenByte, Base64.DEFAULT);
            imagenASubir.setImageBitmap(bitmap);
            cursor.close();

        }

    }
}