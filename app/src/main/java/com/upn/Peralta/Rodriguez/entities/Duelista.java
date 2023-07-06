package com.upn.Peralta.Rodriguez.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Duelistas")
public class Duelista {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nombre;
}
