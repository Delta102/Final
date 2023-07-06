package com.upn.Peralta.Rodriguez;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateCartaActivity extends AppCompatActivity {

    private static final int OPEN_GALLERY_REQUEST = 1002;
    private static final int LOCATION_PERMISSION_REQUEST = 1003;
    private static final String urlFotoApi = "https://demo-upn.bit2bittest.com/";

    ImageView imgGaleria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_carta);

        int idDuelista = getIntent().getIntExtra("idDuelista", -1);

        Button btnOpenGallery = findViewById(R.id.btnOpenGallery);
        imgGaleria = findViewById(R.id.imgGallery);

        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    openGallery();
                else {
                    Log.i("MAIN_APP", "NO HAY PERMISOS");
                    String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, 2000);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_GALLERY_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
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
                }
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), OPEN_GALLERY_REQUEST);
    }
}