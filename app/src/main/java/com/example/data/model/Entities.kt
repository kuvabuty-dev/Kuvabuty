package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "encrypted_files")
data class EncryptedFileEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val cloudId: String,
  val name: String,
  val encryptedName: String,
  val category: String, // DOCUMENTS, MEDIA, ARCHIVES, NOTES
  val mimeType: String,
  val sizeBytes: Long,
  val encryptedSizeBytes: Long,
  val sha256Checksum: String,
  val ivBase64: String,
  val saltBase64: String,
  val rawCipherBase64: String,
  val decryptedSnippet: String,
  val isFavorite: Boolean = false,
  val isOfflineAvailable: Boolean = true,
  val syncStatus: String = "SYNCED", // SYNCED, SYNCING, PENDING_UPLOAD, REMOTE_MODIFIED, CONFLICT
  val version: Int = 1,
  val lastModifiedTime: Long = System.currentTimeMillis(),
  val lastSyncedTime: Long = System.currentTimeMillis(),
  val modifiedByDevice: String = "Pixel 9 Pro"
)

@Entity(tableName = "devices")
data class DeviceEntity(
  @PrimaryKey val id: String,
  val name: String,
  val deviceType: String, // PHONE, TABLET, LAPTOP, DESKTOP
  val isCurrentDevice: Boolean = false,
  val isOnline: Boolean = true,
  val lastSeenTime: Long = System.currentTimeMillis(),
  val syncState: String = "SYNCHRONIZED", // SYNCHRONIZED, SYNCING, PAUSED, OFFLINE
  val ipAddress: String = "192.168.1.100",
  val keyFingerprint: String
)

@Entity(tableName = "sync_logs")
data class SyncLogEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val timestamp: Long = System.currentTimeMillis(),
  val eventType: String, // UPLOAD, DOWNLOAD, REMOTE_UPDATE, CONFLICT_DETECTED, KEY_VERIFIED, DEVICE_PAIRED
  val fileName: String,
  val deviceName: String,
  val details: String,
  val isSuccess: Boolean = true
)
