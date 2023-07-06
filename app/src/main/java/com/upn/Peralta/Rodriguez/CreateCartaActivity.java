package com.upn.Peralta.Rodriguez;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class CreateCartaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_carta);

        int idDuelista = getIntent().getIntExtra("idDuelista", -1);
        
    }
}