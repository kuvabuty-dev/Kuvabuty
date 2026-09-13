package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.DeviceEntity
import com.example.data.model.EncryptedFileEntity
import com.example.data.model.SyncLogEntity

@Database(
  entities = [EncryptedFileEntity::class, DeviceEntity::class, SyncLogEntity::class],
  version = 1,
  exportSchema = false
)
abstract class VaultDatabase : RoomDatabase() {
  abstract fun fileDao(): FileDao
  abstract fun deviceDao(): DeviceDao
  abstract fun syncLogDao(): SyncLogDao

  companion object {
    @Volatile
    private var INSTANCE: VaultDatabase? = null

    fun getDatabase(context: Context): VaultDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          VaultDatabase::class.java,
          "vault_sync_database"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
