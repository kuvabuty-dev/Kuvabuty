package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DeviceEntity
import com.example.data.model.EncryptedFileEntity
import com.example.data.model.SyncLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {
  @Query("SELECT * FROM encrypted_files ORDER BY lastModifiedTime DESC")
  fun getAllFiles(): Flow<List<EncryptedFileEntity>>

  @Query("SELECT * FROM encrypted_files WHERE category = :category ORDER BY lastModifiedTime DESC")
  fun getFilesByCategory(category: String): Flow<List<EncryptedFileEntity>>

  @Query("SELECT * FROM encrypted_files WHERE name LIKE '%' || :query || '%' ORDER BY lastModifiedTime DESC")
  fun searchFiles(query: String): Flow<List<EncryptedFileEntity>>

  @Query("SELECT * FROM encrypted_files WHERE id = :id LIMIT 1")
  suspend fun getFileById(id: Long): EncryptedFileEntity?

  @Query("SELECT * FROM encrypted_files WHERE cloudId = :cloudId LIMIT 1")
  suspend fun getFileByCloudId(cloudId: String): EncryptedFileEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFile(file: EncryptedFileEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFiles(files: List<EncryptedFileEntity>)

  @Update
  suspend fun updateFile(file: EncryptedFileEntity)

  @Delete
  suspend fun deleteFile(file: EncryptedFileEntity)

  @Query("DELETE FROM encrypted_files WHERE id = :id")
  suspend fun deleteFileById(id: Long)

  @Query("SELECT SUM(sizeBytes) FROM encrypted_files")
  fun getTotalBytes(): Flow<Long?>

  @Query("SELECT * FROM encrypted_files WHERE syncStatus = :status")
  fun getFilesWithStatus(status: String): Flow<List<EncryptedFileEntity>>

  @Query("SELECT COUNT(*) FROM encrypted_files")
  suspend fun getFileCount(): Int
}

@Dao
interface DeviceDao {
  @Query("SELECT * FROM devices ORDER BY isCurrentDevice DESC, lastSeenTime DESC")
  fun getAllDevices(): Flow<List<DeviceEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDevice(device: DeviceEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDevices(devices: List<DeviceEntity>)

  @Update
  suspend fun updateDevice(device: DeviceEntity)

  @Delete
  suspend fun deleteDevice(device: DeviceEntity)

  @Query("SELECT COUNT(*) FROM devices")
  suspend fun getDeviceCount(): Int
}

@Dao
interface SyncLogDao {
  @Query("SELECT * FROM sync_logs ORDER BY timestamp DESC LIMIT :limit")
  fun getRecentLogs(limit: Int = 50): Flow<List<SyncLogEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLog(log: SyncLogEntity)

  @Query("DELETE FROM sync_logs")
  suspend fun clearLogs()
}
