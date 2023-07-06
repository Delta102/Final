package com.upn.Peralta.Rodriguez;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CartaDetalleActivity extends AppCompatActivity {
    int idDuelista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta_detalle);

        idDuelista = getIntent().getIntExtra("idDuelista", -1);

    }
}