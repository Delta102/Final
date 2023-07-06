package com.upn.Peralta.Rodriguez.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cartas")
public class Carta implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int idCarta;
    public String nombreCarta;
    public int ptosAtaque;
    public int ptosDefensa;
    public String imagen;
    public double latitud;
    public double longitud;
    public int idDuelista;

    public Carta() {
    }

    public Carta(int idCarta, String nombreCarta, int ptosAtaque, int ptosDefensa, String imagen,
                 double latitud, double longitud, int idDuelista) {
        this.idCarta = idCarta;
        this.nombreCarta = nombreCarta;
        this.ptosAtaque = ptosAtaque;
        this.ptosDefensa = ptosDefensa;
        this.imagen = imagen;
        this.latitud = latitud;
        this.longitud = longitud;
        this.idDuelista = idDuelista;
    }

    protected Carta(Parcel in) {
        idCarta = in.readInt();
        idDuelista = in.readInt();
        nombreCarta = in.readString();
        imagen = in.readString();
        ptosAtaque = Integer.parseInt(in.readString());
        ptosDefensa = Integer.parseInt(in.readString());
        latitud = Double.parseDouble(in.readString());
        longitud = Double.parseDouble(in.readString());
    }
    public static final Creator<Carta> CREATOR = new Creator<Carta>() {
        @Override
        public Carta createFromParcel(Parcel in) {
            return new Carta(in);
        }

        @Override
        public Carta[] newArray(int size) {
            return new Carta[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idCarta);
        dest.writeInt(idDuelista);
        dest.writeString(nombreCarta);
        dest.writeString(imagen);
        dest.writeInt(ptosAtaque);
        dest.writeInt(ptosDefensa);
        dest.writeDouble(latitud);
        dest.writeDouble(longitud);
    }
}
