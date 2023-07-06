package com.upn.Peralta.Rodriguez;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.upn.Peralta.Rodriguez.adapters.CartaAdapter;
import com.upn.Peralta.Rodriguez.entities.Carta;

import java.util.ArrayList;
import java.util.List;

public class CartaDetalleActivity extends AppCompatActivity {

    private RecyclerView rvCartas;
    private CartaAdapter cartaAdapter;
    private List<Carta> cartas;
    private int idDuelista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta_detalle);

        rvCartas = findViewById(R.id.rvCartas);
        rvCartas.setLayoutManager(new LinearLayoutManager(this));

        cartas = getIntent().getParcelableArrayListExtra("cartas");
        idDuelista = getIntent().getIntExtra("idDuelista", -1);

        List<Carta> cartasFiltradas = filtrarCartasPorIdDuelista(cartas, idDuelista);

        cartaAdapter = new CartaAdapter(cartasFiltradas);
        rvCartas.setAdapter(cartaAdapter);
    }

    private List<Carta> filtrarCartasPorIdDuelista(List<Carta> cartas, int idDuelista) {
        List<Carta> cartasFiltradas = new ArrayList<>();
        for (Carta carta : cartas) {
            if (carta.idDuelista == idDuelista) {
                cartasFiltradas.add(carta);
            }
        }
        return cartasFiltradas;
    }
}