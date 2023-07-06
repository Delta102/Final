package com.upn.Peralta.Rodriguez.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cartas")
public class Carta {
    @PrimaryKey(autoGenerate = true)
    public int idCarta;
    public String nombreCarta;
    public int ptosAtaque;
    public int ptosDefensa;
    public String imagen;
    public float latitud;
    public float longitud;
    public int idDuelista;
}
