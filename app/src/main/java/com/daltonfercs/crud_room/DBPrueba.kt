package com.daltonfercs.crud_room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Usuario::class],
    version = 1
)
abstract class DBPrueba: RoomDatabase() {
    abstract fun daoUsuario(): DaoUsuario
}