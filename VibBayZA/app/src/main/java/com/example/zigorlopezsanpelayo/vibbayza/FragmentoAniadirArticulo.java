package com.example.zigorlopezsanpelayo.vibbayza;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.text.DecimalFormat;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.DOWNLOAD_SERVICE;

public class FragmentoAniadirArticulo extends Fragment {

    protected Button botonAniadirArticulo;
    protected EditText nombreArticulo;
    protected EditText precioArticulo;
    protected String nombreArticuloS;
    protected String precioArticuloS;
    protected double precioArticuloD;
    private static final int RESULT_LOAD_IMAGE = 1;
    protected ImageView imagenASubir;
    protected String imagenB64 = "";
    protected long numArts = 1;
    protected String numArtsS = "1";



    DatabaseReference refArticulos =
            FirebaseDatabase.getInstance().getReference()
                    .child("articulos");

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

        

        refArticulos.getParent().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    if (snap.getKey().equals("articulos")) {
                        numArts = snap.getChildrenCount() + 1;
                        numArtsS = Long.toString(numArts);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        nombreArticulo.requestFocus();
        precioArticulo = (EditText) v.findViewById(R.id.campo_precio_articulo);

        botonAniadirArticulo = (Button) v.findViewById(R.id.boton_aniadir_articulo);
        botonAniadirArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nombreArticulo.requestFocus();
                nombreArticuloS = nombreArticulo.getText().toString();
                precioArticuloS = precioArticulo.getText().toString();

                if (nombreArticuloS.equals("") || precioArticuloS.equals("") || imagenB64.equals("")) {
                    Toast campoVacio = Toast.makeText(getActivity().getApplicationContext(), "Debes rellenar todos los campos", Toast.LENGTH_SHORT);
                    campoVacio.show();
                }
                else {
                    DecimalFormat decim = new DecimalFormat("0.00");
                    nombreArticuloS = nombreArticulo.getText().toString();
                    precioArticuloD = Double.parseDouble(precioArticulo.getText().toString());
                    precioArticuloD = Double.parseDouble(decim.format(precioArticuloD));

                    aniadirArticulo(numArtsS, nombreArticuloS, imagenB64, emailUsuario, false, precioArticuloD);
                    Toast articuloAniadido = Toast.makeText(getActivity().getApplicationContext(), "Artículo añadido correctamente", Toast.LENGTH_SHORT);
                    articuloAniadido.show();

                    boolean fragmentTransaction = false;

                    FragmentoAniadirArticulo fragmentoAniadirArticulo = new FragmentoAniadirArticulo();
                    fragmentTransaction = true;

                    if(fragmentTransaction) {
                        getFragmentManager().beginTransaction().addToBackStack("aniadirArticulo");
                        Fragment fragmentPrevio = getFragmentManager().findFragmentByTag("aniadirArticulo");
                        getFragmentManager().beginTransaction()
                                .replace(R.id.content_main, fragmentoAniadirArticulo)
                                .commit();

                        ((ProfileActivity)getActivity()).getSupportActionBar().setTitle("Pujas");
                        getFragmentManager().beginTransaction().remove(fragmentPrevio);
                    }
                    
                }
            }
        });
    }

    private boolean checkPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, permissionCheck);
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
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
            Bitmap bitmapOrinignal = BitmapFactory.decodeFile(imagePath, options);

            Bitmap imagenReescalada = imagenReescalada(bitmapOrinignal);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imagenReescalada.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] imagenByte = byteArrayOutputStream.toByteArray();
            imagenB64 = Base64.encodeToString(imagenByte, Base64.DEFAULT);
            imagenASubir.setImageBitmap(imagenReescalada);
            cursor.close();

        }
    }

    public Bitmap imagenReescalada (Bitmap bitmap) {
        float anchoEscalado = ((float) 500) / bitmap.getWidth();
        float altoEscalado = ((float) 350) / bitmap.getHeight();
        Matrix matriz = new Matrix();
        matriz.postScale(anchoEscalado, altoEscalado);
        Bitmap bitmapReescaclado = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matriz, true);
        return bitmapReescaclado;
    }

    private void aniadirArticulo(String artId, String titulo, String foto, String propietario, boolean estado, double precio) {
        Articulos articulo = new Articulos(titulo, foto, propietario, estado, precio, 0.99);
        refArticulos.child(artId).setValue(articulo);
    }

}