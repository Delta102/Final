package com.upn.Peralta.Rodriguez.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Duelistas")
public class Duelista implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nombre;
    public boolean sincronizado; // Nuevo campo para indicar si el duelist se ha sincronizado con el web service

    public Duelista() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(boolean sincronizado) {
        this.sincronizado = sincronizado;
    }

    public Duelista(int id, String nombre, boolean sincronizado) {
        this.id = id;
        this.nombre = nombre;
        this.sincronizado = sincronizado;
    }

    protected Duelista(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        sincronizado = in.readByte() != 0;
    }

    public static final Creator<Duelista> CREATOR = new Creator<Duelista>() {
        @Override
        public Duelista createFromParcel(Parcel in) {
            return new Duelista(in);
        }

        @Override
        public Duelista[] newArray(int size) {
            return new Duelista[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeByte((byte) (sincronizado ? 1 : 0));
    }
}