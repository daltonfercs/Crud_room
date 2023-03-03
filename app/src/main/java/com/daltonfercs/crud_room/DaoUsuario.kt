package com.daltonfercs.crud_room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DaoUsuario {
    @Query("SELECT * FROM usuarios")
    suspend fun obtenerUsuario(): MutableList<Usuario>

    @Insert
    suspend fun addUsuario(usuario: Usuario)

    @Query("UPDATE usuarios set pais=:pais WHERE usuario=:usuario")
    suspend fun updateUsuario(usuario:String, pais:String)

    @Query("DELETE FROM usuarios WHERE usuario=:usuario")
    suspend fun deleteUsuario(usuario: String)
}