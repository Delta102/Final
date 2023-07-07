package com.upn.Peralta.Rodriguez.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.upn.Peralta.Rodriguez.entities.Carta;
import com.upn.Peralta.Rodriguez.entities.Duelista;

import java.util.List;
@Dao
public interface DuelistaDao {

    @Insert
    void createDuelist(Duelista duelista);

    @Query("SELECT * FROM Duelistas")
    List<Duelista> listarDuelistas();

    @Query("DELETE FROM Duelistas")
    void deleteAllDuelists();

    @Insert
    void createCart(Carta carta);

    @Query("SELECT * FROM Cartas")
    List<Carta> getAllCarts();
    @Query("SELECT * FROM Cartas WHERE idDuelista = :idDuelista")
    List<Carta> getCartasByDuelistaId(int idDuelista);

    @Query("DELETE FROM Cartas")
    void deleteAllCarts();

    @Query("SELECT MAX(id) FROM Duelistas")
    int obtenerUltimoId();

}
