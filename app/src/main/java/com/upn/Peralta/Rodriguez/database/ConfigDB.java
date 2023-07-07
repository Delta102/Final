package com.upn.Peralta.Rodriguez.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.upn.Peralta.Rodriguez.dao.CartaDao;
import com.upn.Peralta.Rodriguez.dao.DuelistaDao;
import com.upn.Peralta.Rodriguez.entities.Carta;
import com.upn.Peralta.Rodriguez.entities.Duelista;

@Database(entities = {Duelista.class, Carta.class}, version = 11)
public abstract class ConfigDB extends RoomDatabase {
    public abstract DuelistaDao duelistaDao();

    public static ConfigDB getInstance(Context context){
        return Room.databaseBuilder(context, ConfigDB.class, "AppDataBase")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }
}