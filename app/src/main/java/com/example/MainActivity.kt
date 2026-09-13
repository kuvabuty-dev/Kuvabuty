package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.VaultDatabase
import com.example.data.repository.VaultRepository
import com.example.ui.VaultViewModel
import com.example.ui.VaultViewModelFactory
import com.example.ui.components.DevicePairingDialog
import com.example.ui.components.EncryptionAuditDialog
import com.example.ui.components.FileViewerDialog
import com.example.ui.components.KeyRotationDialog
import com.example.ui.components.NewFileDialog
import com.example.ui.components.VaultTopBar
import com.example.ui.screens.ActivityScreen
import com.example.ui.screens.FilesScreen
import com.example.ui.screens.LockScreen
import com.example.ui.screens.SecurityScreen
import com.example.ui.screens.SyncScreen
import com.example.ui.theme.CyberAccentRed
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberDarkBackground
import com.example.ui.theme.CyberDarkSurface
import com.example.ui.theme.CyberDarkSurfaceVariant
import com.example.ui.theme.CyberPrimary
import com.example.ui.theme.CyberSecondary
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.MyApplicationTheme

enum class VaultNavTab {
  FILES, SYNC, SECURITY, ACTIVITY
}

class MainActivity : ComponentActivity() {

  private val viewModel: VaultViewModel by viewModels {
    val db = VaultDatabase.getDatabase(applicationContext)
    val repo = VaultRepository(db.fileDao(), db.deviceDao(), db.syncLogDao())
    VaultViewModelFactory(repo)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        VaultApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun VaultApp(viewModel: VaultViewModel) {
  val isUnlocked by viewModel.isVaultUnlocked.collectAsStateWithLifecycle()
  val keyFingerprint by viewModel.keyFingerprint.collectAsStateWithLifecycle()

  if (!isUnlocked) {
    LockScreen(
      keyFingerprint = keyFingerprint,
      onUnlock = { passphrase -> viewModel.unlockVault(passphrase) }
    )
    return
  }

  var currentTab by remember { mutableStateOf(VaultNavTab.FILES) }

  val files by viewModel.filteredFiles.collectAsStateWithLifecycle()
  val devices by viewModel.pairedDevices.collectAsStateWithLifecycle()
  val syncLogs by viewModel.syncLogs.collectAsStateWithLifecycle()
  val totalUsedBytes by viewModel.totalStorageBytes.collectAsStateWithLifecycle()
  val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
  val syncProgress by viewModel.syncProgress.collectAsStateWithLifecycle()
  val networkSpeedKbps by viewModel.networkSpeedKbps.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

  // Active dialogs
  val selectedFileForViewer by viewModel.selectedFileForViewer.collectAsStateWithLifecycle()
  val decryptedViewerContent by viewModel.decryptedViewerContent.collectAsStateWithLifecycle()
  val selectedFileForAudit by viewModel.selectedFileForAudit.collectAsStateWithLifecycle()
  val isUploadDialogOpen by viewModel.isUploadDialogOpen.collectAsStateWithLifecycle()
  val isPairingDialogOpen by viewModel.isPairingDialogOpen.collectAsStateWithLifecycle()
  val isKeyRotationDialogOpen by viewModel.isKeyRotationDialogOpen.collectAsStateWithLifecycle()

  val conflictCount = files.count { it.syncStatus == "CONFLICT" }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(CyberDarkBackground),
    topBar = {
      VaultTopBar(
        isSyncing = isSyncing,
        networkSpeedKbps = networkSpeedKbps,
        onLockVault = { viewModel.lockVault() },
        onManualSync = { viewModel.triggerManualSync() }
      )
    },
    bottomBar = {
      NavigationBar(
        modifier = Modifier
          .windowInsetsPadding(WindowInsets.navigationBars)
          .testTag("bottom_navigation_bar"),
        containerColor = CyberDarkSurface,
        tonalElevation = 8.dp
      ) {
        NavigationBarItem(
          selected = currentTab == VaultNavTab.FILES,
          onClick = { currentTab = VaultNavTab.FILES },
          icon = {
            if (conflictCount > 0) {
              BadgedBox(badge = {
                Badge(containerColor = CyberAccentRed) { Text("$conflictCount") }
              }) {
                Icon(
                  if (currentTab == VaultNavTab.FILES) Icons.Filled.Folder else Icons.Outlined.Folder,
                  contentDescription = "Files"
                )
              }
            } else {
              Icon(
                if (currentTab == VaultNavTab.FILES) Icons.Filled.Folder else Icons.Outlined.Folder,
                contentDescription = "Files"
              )
            }
          },
          label = { Text("Files", fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = CyberDarkBackground,
            selectedTextColor = CyberPrimary,
            indicatorColor = CyberPrimary,
            unselectedIconColor = CyberTextMuted,
            unselectedTextColor = CyberTextMuted
          ),
          modifier = Modifier.testTag("tab_files")
        )

        NavigationBarItem(
          selected = currentTab == VaultNavTab.SYNC,
          onClick = { currentTab = VaultNavTab.SYNC },
          icon = {
            Icon(
              if (currentTab == VaultNavTab.SYNC) Icons.Filled.Sync else Icons.Outlined.Sync,
              contentDescription = "Sync"
            )
          },
          label = { Text("Sync", fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = CyberDarkBackground,
            selectedTextColor = CyberSecondary,
            indicatorColor = CyberSecondary,
            unselectedIconColor = CyberTextMuted,
            unselectedTextColor = CyberTextMuted
          ),
          modifier = Modifier.testTag("tab_sync")
        )

        NavigationBarItem(
          selected = currentTab == VaultNavTab.SECURITY,
          onClick = { currentTab = VaultNavTab.SECURITY },
          icon = {
            Icon(
              if (currentTab == VaultNavTab.SECURITY) Icons.Filled.Security else Icons.Outlined.Security,
              contentDescription = "Security"
            )
          },
          label = { Text("Security", fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = CyberDarkBackground,
            selectedTextColor = CyberPrimary,
            indicatorColor = CyberPrimary,
            unselectedIconColor = CyberTextMuted,
            unselectedTextColor = CyberTextMuted
          ),
          modifier = Modifier.testTag("tab_security")
        )

        NavigationBarItem(
          selected = currentTab == VaultNavTab.ACTIVITY,
          onClick = { currentTab = VaultNavTab.ACTIVITY },
          icon = {
            Icon(
              if (currentTab == VaultNavTab.ACTIVITY) Icons.Filled.History else Icons.Outlined.History,
              contentDescription = "Activity"
            )
          },
          label = { Text("Activity", fontSize = 11.sp) },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = CyberDarkBackground,
            selectedTextColor = CyberPrimary,
            indicatorColor = CyberPrimary,
            unselectedIconColor = CyberTextMuted,
            unselectedTextColor = CyberTextMuted
          ),
          modifier = Modifier.testTag("tab_activity")
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentTab) {
        VaultNavTab.FILES -> FilesScreen(
          files = files,
          totalUsedBytes = totalUsedBytes,
          searchQuery = searchQuery,
          selectedCategory = selectedCategory,
          onSearchQueryChange = { viewModel.setSearchQuery(it) },
          onCategorySelected = { viewModel.setCategory(it) },
          onFileClick = { viewModel.openFileViewer(it) },
          onAuditClick = { viewModel.openAudit(it) },
          onToggleFavorite = { viewModel.toggleFavorite(it) },
          onToggleOffline = { viewModel.toggleOffline(it) },
          onResolveConflict = { file, keepLocal -> viewModel.resolveConflict(file, keepLocal) },
          onDeleteFile = { viewModel.deleteFile(it) },
          onUploadClick = { viewModel.openUploadDialog() }
        )

        VaultNavTab.SYNC -> SyncScreen(
          devices = devices,
          isSyncing = isSyncing,
          syncProgress = syncProgress,
          networkSpeedKbps = networkSpeedKbps,
          onManualSync = { viewModel.triggerManualSync() },
          onSimulateRemoteChange = { viewModel.simulateIncomingRemoteChange() },
          onSimulateConflict = { viewModel.simulateConflict() },
          onPairNewDeviceClick = { viewModel.openPairingDialog() }
        )

        VaultNavTab.SECURITY -> SecurityScreen(
          keyFingerprint = keyFingerprint,
          recoverySeed = viewModel.getRecoverySeed(),
          onRotateKeyClick = { viewModel.openKeyRotationDialog() },
          onLockVault = { viewModel.lockVault() }
        )

        VaultNavTab.ACTIVITY -> ActivityScreen(
          logs = syncLogs
        )
      }
    }
  }

  // Active Dialog Overlays
  selectedFileForAudit?.let { file ->
    EncryptionAuditDialog(
      file = file,
      onDismiss = { viewModel.closeAudit() }
    )
  }

  selectedFileForViewer?.let { file ->
    FileViewerDialog(
      file = file,
      decryptedContent = decryptedViewerContent,
      onDismiss = { viewModel.closeFileViewer() }
    )
  }

  if (isUploadDialogOpen) {
    NewFileDialog(
      onDismiss = { viewModel.closeUploadDialog() },
      onUpload = { name, content, category ->
        viewModel.uploadNewFile(name, content, category)
      }
    )
  }

  if (isPairingDialogOpen) {
    DevicePairingDialog(
      onDismiss = { viewModel.closePairingDialog() },
      onPair = { name, type ->
        viewModel.pairNewDevice(name, type)
      }
    )
  }

  if (isKeyRotationDialogOpen) {
    KeyRotationDialog(
      currentFingerprint = keyFingerprint,
      onDismiss = { viewModel.closeKeyRotationDialog() },
      onRotate = { newPassphrase ->
        viewModel.rotateKey(newPassphrase)
      }
    )
  }
}
