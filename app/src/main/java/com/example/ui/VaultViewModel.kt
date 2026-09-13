package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.DeviceEntity
import com.example.data.model.EncryptedFileEntity
import com.example.data.model.SyncLogEntity
import com.example.data.repository.VaultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VaultViewModel(private val repository: VaultRepository) : ViewModel() {

  val isSyncing: StateFlow<Boolean> = repository.isSyncing
  val syncProgress: StateFlow<Float> = repository.syncProgress
  val networkSpeedKbps: StateFlow<Int> = repository.networkSpeedKbps
  val isVaultUnlocked: StateFlow<Boolean> = repository.isVaultUnlocked
  val keyFingerprint: StateFlow<String> = repository.keyFingerprint
  val pairedDevices: StateFlow<List<DeviceEntity>> = repository.pairedDevices
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
  val syncLogs: StateFlow<List<SyncLogEntity>> = repository.syncLogs
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedCategory = MutableStateFlow("ALL")
  val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

  // Filtered files flow
  val filteredFiles: StateFlow<List<EncryptedFileEntity>> = combine(
    repository.allFiles,
    _selectedCategory,
    _searchQuery
  ) { allFiles, category, query ->
    allFiles.filter { file ->
      val matchesCategory = (category == "ALL") || (file.category == category)
      val matchesQuery = query.isBlank() || file.name.contains(query, ignoreCase = true)
      matchesCategory && matchesQuery
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val totalStorageBytes: StateFlow<Long> = repository.totalStorageBytes
    .combine(repository.allFiles) { bytes, files ->
      bytes ?: files.sumOf { it.sizeBytes }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 52_428_800L) // Default 50MB

  // Dialog & Active Inspection States
  private val _selectedFileForViewer = MutableStateFlow<EncryptedFileEntity?>(null)
  val selectedFileForViewer: StateFlow<EncryptedFileEntity?> = _selectedFileForViewer.asStateFlow()

  private val _decryptedViewerContent = MutableStateFlow("")
  val decryptedViewerContent: StateFlow<String> = _decryptedViewerContent.asStateFlow()

  private val _selectedFileForAudit = MutableStateFlow<EncryptedFileEntity?>(null)
  val selectedFileForAudit: StateFlow<EncryptedFileEntity?> = _selectedFileForAudit.asStateFlow()

  private val _isUploadDialogOpen = MutableStateFlow(false)
  val isUploadDialogOpen: StateFlow<Boolean> = _isUploadDialogOpen.asStateFlow()

  private val _isPairingDialogOpen = MutableStateFlow(false)
  val isPairingDialogOpen: StateFlow<Boolean> = _isPairingDialogOpen.asStateFlow()

  private val _isKeyRotationDialogOpen = MutableStateFlow(false)
  val isKeyRotationDialogOpen: StateFlow<Boolean> = _isKeyRotationDialogOpen.asStateFlow()

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setCategory(category: String) {
    _selectedCategory.value = category
  }

  fun openUploadDialog() {
    _isUploadDialogOpen.value = true
  }

  fun closeUploadDialog() {
    _isUploadDialogOpen.value = false
  }

  fun openPairingDialog() {
    _isPairingDialogOpen.value = true
  }

  fun closePairingDialog() {
    _isPairingDialogOpen.value = false
  }

  fun openKeyRotationDialog() {
    _isKeyRotationDialogOpen.value = true
  }

  fun closeKeyRotationDialog() {
    _isKeyRotationDialogOpen.value = false
  }

  fun uploadNewFile(name: String, content: String, category: String) {
    viewModelScope.launch {
      repository.encryptAndSaveFile(name, content, category)
      _isUploadDialogOpen.value = false
    }
  }

  fun openFileViewer(file: EncryptedFileEntity) {
    _selectedFileForViewer.value = file
    _decryptedViewerContent.value = repository.decryptFile(file)
  }

  fun closeFileViewer() {
    _selectedFileForViewer.value = null
    _decryptedViewerContent.value = ""
  }

  fun openAudit(file: EncryptedFileEntity) {
    _selectedFileForAudit.value = file
  }

  fun closeAudit() {
    _selectedFileForAudit.value = null
  }

  fun deleteFile(file: EncryptedFileEntity) {
    viewModelScope.launch {
      repository.deleteFile(file)
      if (_selectedFileForViewer.value?.id == file.id) {
        closeFileViewer()
      }
      if (_selectedFileForAudit.value?.id == file.id) {
        closeAudit()
      }
    }
  }

  fun toggleFavorite(file: EncryptedFileEntity) {
    viewModelScope.launch {
      repository.toggleFavorite(file)
    }
  }

  fun toggleOffline(file: EncryptedFileEntity) {
    viewModelScope.launch {
      repository.toggleOffline(file)
    }
  }

  fun resolveConflict(file: EncryptedFileEntity, keepLocal: Boolean) {
    viewModelScope.launch {
      repository.resolveConflict(file, keepLocal)
    }
  }

  fun triggerManualSync() {
    viewModelScope.launch {
      repository.triggerManualSync()
    }
  }

  fun simulateIncomingRemoteChange() {
    viewModelScope.launch {
      repository.simulateIncomingRemoteChange()
    }
  }

  fun simulateConflict() {
    viewModelScope.launch {
      repository.simulateConflict()
    }
  }

  fun pairNewDevice(name: String, type: String) {
    viewModelScope.launch {
      repository.pairNewDevice(name, type)
      _isPairingDialogOpen.value = false
    }
  }

  fun lockVault() {
    repository.lockVault()
  }

  fun unlockVault(passphrase: String): Boolean {
    return repository.unlockVault(passphrase)
  }

  fun rotateKey(newPassphrase: String) {
    repository.rotateMasterKey(newPassphrase)
    _isKeyRotationDialogOpen.value = false
  }

  fun getMasterPassphrase(): String = repository.getMasterPassphrase()
  fun getRecoverySeed(): List<String> = repository.getRecoverySeed()
}

class VaultViewModelFactory(private val repository: VaultRepository) : ViewModelProvider.Factory {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(VaultViewModel::class.java)) {
      return VaultViewModel(repository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
