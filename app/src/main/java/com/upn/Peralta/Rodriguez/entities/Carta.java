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

    public int getIdCarta() {
        return idCarta;
    }

    public void setIdCarta(int idCarta) {
        this.idCarta = idCarta;
    }

    public String getNombreCarta() {
        return nombreCarta;
    }

    public void setNombreCarta(String nombreCarta) {
        this.nombreCarta = nombreCarta;
    }

    public int getPtosAtaque() {
        return ptosAtaque;
    }

    public void setPtosAtaque(int ptosAtaque) {
        this.ptosAtaque = ptosAtaque;
    }

    public int getPtosDefensa() {
        return ptosDefensa;
    }

    public void setPtosDefensa(int ptosDefensa) {
        this.ptosDefensa = ptosDefensa;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getIdDuelista() {
        return idDuelista;
    }

    public void setIdDuelista(int idDuelista) {
        this.idDuelista = idDuelista;
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
        ptosAtaque = in.readInt();
        ptosDefensa = in.readInt();
        latitud = in.readDouble();
        longitud = in.readDouble();
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
