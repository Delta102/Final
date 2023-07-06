package com.upn.Peralta.Rodriguez;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.upn.Peralta.Rodriguez.entities.Carta;
import com.upn.Peralta.Rodriguez.services.DuelistaService;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateCartaActivity extends AppCompatActivity {

    LocationManager locationManager;
    double latitud, longitud;
    int idDuelista;

    private static final int OPEN_CAMERA_REQUEST = 1001;
    private static final int LOCATION_PERMISSION_REQUEST = 1003;
    private static final String urlFotoApi = "https://demo-upn.bit2bittest.com/";

    ImageView imgGaleria;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_carta);

        idDuelista = getIntent().getIntExtra("idDuelista", -1);

        Button btnOpenGallery = findViewById(R.id.btnOpenGallery);
        Button btnRegister = findViewById(R.id.btnRegistro);
        imgGaleria = findViewById(R.id.imgGallery);

        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOpenCamera();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerCoordenadas();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_CAMERA_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Log.i("MAIN_APP", "AQUÍ");
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    imgGaleria.setImageBitmap(bitmap);

                    if (bitmap != null) {
                        String imagenBase64 = convertirBitmapB64(bitmap);
                        DuelistaService.ImageToSave imageToSave = new DuelistaService.ImageToSave(imagenBase64);

                        enviarImageApi(imageToSave);
                    }
                }
            }
        }
    }

    void obtenerCoordenadas() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    latitud = location.getLatitude();
                    longitud = location.getLongitude();
                    Log.i("MAIN_APP", "Latitud: " + latitud);
                    Log.i("MAIN_APP", "Longitud: " + longitud);

                    locationManager.removeUpdates(this);


                    registrarCarta();
                }
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permissions, LOCATION_PERMISSION_REQUEST);
        }
    }

    void registrarCarta() {
        EditText txtName = findViewById(R.id.txtCartName);
        EditText txtAttack = findViewById(R.id.txtAttack);
        EditText txtDefense = findViewById(R.id.txtDefense);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://63023858c6dda4f287b57c96.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DuelistaService servicio = retrofit.create(DuelistaService.class);

        Carta carta = new Carta();
        carta.nombreCarta = txtName.getText().toString();
        carta.ptosAtaque = Integer.parseInt(txtAttack.getText().toString());
        carta.ptosDefensa = Integer.parseInt(txtDefense.getText().toString());
        carta.imagen = url;
        carta.latitud = latitud;
        carta.longitud = longitud;
        carta.idDuelista = idDuelista;



        Call<Void> call = servicio.createCart(carta);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("MAIN_APP", "Se creó");
                    Intent intent = new Intent(CreateCartaActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("MAIN_APP", "No sirve");
            }
        });
    }

    String convertirBitmapB64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    void enviarImageApi(DuelistaService.ImageToSave imageToSave) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlFotoApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DuelistaService service = retrofit.create(DuelistaService.class);

        Call<DuelistaService.ImageResponse> call = service.subirImagen(imageToSave);
        call.enqueue(new Callback<DuelistaService.ImageResponse>() {
            @Override
            public void onResponse(Call<DuelistaService.ImageResponse> call, Response<DuelistaService.ImageResponse> response) {
                if (response.isSuccessful()) {
                    url = urlFotoApi + response.body().getUrl();
                    Log.i("MAIN_APP", url);
                } else {
                    Log.i("MAIN_APP", "No se subió");
                }
            }

            @Override
            public void onFailure(Call<DuelistaService.ImageResponse> call, Throwable t) {
                Log.i("MAIN_APP", "La petición falló");
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), OPEN_CAMERA_REQUEST);
    }

    private void handleOpenCamera() {
        if(checkSelfPermission(Manifest.permission.CAMERA)  == PackageManager.PERMISSION_GRANTED)
        {
            Log.i("MAIN_APP", "Tiene permisos para abrir la galería");
            openGallery();
        } else {
            // solicitar el permiso
            Log.i("MAIN_APP", "No tiene permisos para abrir la galería, solicitando");
            String[] permissions = new String[] {Manifest.permission.CAMERA};
            requestPermissions(permissions, 1000);
        }
    }
}