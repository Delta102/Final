package com.upn.Peralta.Rodriguez.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

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

}
