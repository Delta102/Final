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
    public Duelista() {
    }

    public Duelista(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    protected Duelista(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
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
    }
}
