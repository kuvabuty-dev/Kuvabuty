package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TabletMac
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeviceEntity
import com.example.data.model.EncryptedFileEntity
import com.example.ui.theme.CyberAccentAmber
import com.example.ui.theme.CyberAccentBlue
import com.example.ui.theme.CyberAccentGreen
import com.example.ui.theme.CyberAccentRed
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberDarkBackground
import com.example.ui.theme.CyberDarkSurface
import com.example.ui.theme.CyberDarkSurfaceElevated
import com.example.ui.theme.CyberDarkSurfaceVariant
import com.example.ui.theme.CyberPrimary
import com.example.ui.theme.CyberSecondary
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultTopBar(
  isSyncing: Boolean,
  networkSpeedKbps: Int,
  onLockVault: () -> Unit,
  onManualSync: () -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "sync_spin")
  val angle by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = LinearEasing)
    ),
    label = "spin"
  )

  TopAppBar(
    modifier = modifier.testTag("vault_top_bar"),
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = CyberDarkSurface,
      titleContentColor = CyberTextPrimary
    ),
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(CyberPrimary.copy(alpha = 0.15f))
            .border(1.dp, CyberPrimary.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Outlined.Shield,
            contentDescription = "Shield",
            tint = CyberPrimary,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "VaultSync",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = CyberTextPrimary
          )
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(if (isSyncing) CyberAccentAmber else CyberAccentGreen)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = if (isSyncing) "Syncing (${networkSpeedKbps} KB/s)" else "E2EE Cloud Synced",
              fontSize = 10.sp,
              color = if (isSyncing) CyberAccentAmber else CyberAccentGreen,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }
    },
    actions = {
      IconButton(
        onClick = onManualSync,
        modifier = Modifier.testTag("manual_sync_button")
      ) {
        Icon(
          imageVector = Icons.Default.Sync,
          contentDescription = "Sync",
          tint = if (isSyncing) CyberPrimary else CyberTextSecondary,
          modifier = if (isSyncing) Modifier.rotate(angle) else Modifier
        )
      }
      IconButton(
        onClick = onLockVault,
        modifier = Modifier.testTag("lock_vault_button")
      ) {
        Icon(
          imageVector = Icons.Default.Lock,
          contentDescription = "Lock Vault",
          tint = CyberPrimary
        )
      }
    }
  )
}

@Composable
fun StorageUsageCard(
  usedBytes: Long,
  totalCapacityBytes: Long = 107_374_182_400L, // 100 GB
  fileCount: Int,
  modifier: Modifier = Modifier
) {
  val usedMb = usedBytes / (1024f * 1024f)
  val totalGb = totalCapacityBytes / (1024f * 1024f * 1024f)
  val usedFraction = (usedBytes.toFloat() / totalCapacityBytes.toFloat()).coerceIn(0.01f, 1f)

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("storage_usage_card"),
    colors = CardDefaults.cardColors(containerColor = CyberDarkSurfaceVariant),
    shape = RoundedCornerShape(16.dp),
    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CyberBorder))
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "ENCRYPTED STORAGE QUOTA",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = CyberPrimary,
            letterSpacing = 1.sp
          )
          Text(
            text = "%.1f MB of %.0f GB used".format(usedMb, totalGb),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = CyberTextPrimary
          )
        }
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = CyberSecondary.copy(alpha = 0.15f),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberSecondary.copy(alpha = 0.4f))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = CyberSecondary,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "AES-256-GCM",
              fontSize = 10.sp,
              color = CyberSecondary,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      LinearProgressIndicator(
        progress = { usedFraction },
        modifier = Modifier
          .fillMaxWidth()
          .height(8.dp)
          .clip(RoundedCornerShape(4.dp)),
        color = CyberPrimary,
        trackColor = CyberDarkSurfaceElevated
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "$fileCount items protected",
          fontSize = 12.sp,
          color = CyberTextSecondary
        )
        Text(
          text = "Zero-Knowledge Active",
          fontSize = 12.sp,
          color = CyberAccentGreen,
          fontWeight = FontWeight.Medium
        )
      }
    }
  }
}

@Composable
fun FileCategoryFilterRow(
  selectedCategory: String,
  onCategorySelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val categories = listOf("ALL", "DOCUMENTS", "NOTES", "ARCHIVES", "MEDIA")

  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    categories.forEach { category ->
      val isSelected = selectedCategory == category
      FilterChip(
        selected = isSelected,
        onClick = { onCategorySelected(category) },
        label = {
          Text(
            text = category,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
          )
        },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = CyberPrimary,
          selectedLabelColor = CyberDarkBackground,
          containerColor = CyberDarkSurfaceVariant,
          labelColor = CyberTextSecondary
        ),
        border = FilterChipDefaults.filterChipBorder(
          enabled = true,
          selected = isSelected,
          borderColor = CyberBorder,
          selectedBorderColor = CyberPrimary
        )
      )
    }
  }
}

@Composable
fun FileItemCard(
  file: EncryptedFileEntity,
  onViewClick: () -> Unit,
  onAuditClick: () -> Unit,
  onToggleFavorite: () -> Unit,
  onToggleOffline: () -> Unit,
  onResolveConflict: (keepLocal: Boolean) -> Unit,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier
) {
  var menuExpanded by remember { mutableStateOf(false) }

  val icon: ImageVector = when (file.category) {
    "DOCUMENTS" -> Icons.Default.Description
    "NOTES" -> Icons.Default.Note
    "ARCHIVES" -> Icons.Default.Archive
    "MEDIA" -> Icons.Default.Image
    else -> Icons.Default.Folder
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("file_item_${file.id}")
      .clickable { onViewClick() },
    colors = CardDefaults.cardColors(containerColor = CyberDarkSurfaceVariant),
    shape = RoundedCornerShape(14.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = androidx.compose.ui.graphics.SolidColor(
        if (file.syncStatus == "CONFLICT") CyberAccentRed else CyberBorder
      )
    )
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(CyberPrimary.copy(alpha = 0.12f))
            .border(1.dp, CyberPrimary.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = file.category,
            tint = CyberPrimary,
            modifier = Modifier.size(22.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = file.name,
              fontWeight = FontWeight.SemiBold,
              fontSize = 14.sp,
              color = CyberTextPrimary,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
              modifier = Modifier.weight(1f, fill = false)
            )
            if (file.isFavorite) {
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Starred",
                tint = CyberAccentAmber,
                modifier = Modifier.size(14.dp)
              )
            }
          }

          Text(
            text = "${file.encryptedName} • ${formatFileSize(file.sizeBytes)} • v${file.version}",
            fontSize = 11.sp,
            color = CyberTextMuted,
            fontFamily = FontFamily.Monospace,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        // Status badge
        SyncStatusBadge(status = file.syncStatus)

        Box {
          IconButton(
            onClick = { menuExpanded = true },
            modifier = Modifier.testTag("file_menu_${file.id}")
          ) {
            Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = "Menu",
              tint = CyberTextSecondary
            )
          }

          DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            modifier = Modifier.background(CyberDarkSurfaceElevated)
          ) {
            DropdownMenuItem(
              text = { Text("Decrypt & View", color = CyberTextPrimary) },
              leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = CyberPrimary) },
              onClick = {
                menuExpanded = false
                onViewClick()
              }
            )
            DropdownMenuItem(
              text = { Text("Inspect Encryption", color = CyberTextPrimary) },
              leadingIcon = { Icon(Icons.Default.Security, contentDescription = null, tint = CyberSecondary) },
              onClick = {
                menuExpanded = false
                onAuditClick()
              }
            )
            DropdownMenuItem(
              text = { Text(if (file.isFavorite) "Unstar" else "Star", color = CyberTextPrimary) },
              leadingIcon = {
                Icon(
                  if (file.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                  contentDescription = null,
                  tint = CyberAccentAmber
                )
              },
              onClick = {
                menuExpanded = false
                onToggleFavorite()
              }
            )
            DropdownMenuItem(
              text = { Text(if (file.isOfflineAvailable) "Remove Offline Copy" else "Make Available Offline", color = CyberTextPrimary) },
              leadingIcon = { Icon(Icons.Default.CloudDone, contentDescription = null, tint = CyberAccentGreen) },
              onClick = {
                menuExpanded = false
                onToggleOffline()
              }
            )
            DropdownMenuItem(
              text = { Text("Delete Encrypted File", color = CyberAccentRed) },
              leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null, tint = CyberAccentRed) },
              onClick = {
                menuExpanded = false
                onDelete()
              }
            )
          }
        }
      }

      // Conflict resolution banner if file has conflict
      AnimatedVisibility(visible = file.syncStatus == "CONFLICT") {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(CyberAccentRed.copy(alpha = 0.15f))
            .border(1.dp, CyberAccentRed.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(10.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Warning,
              contentDescription = null,
              tint = CyberAccentRed,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Cross-Device Edit Conflict Detected",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = CyberAccentRed
            )
          }
          Text(
            text = "Modified concurrently on ${file.modifiedByDevice}. Choose version to keep:",
            fontSize = 11.sp,
            color = CyberTextPrimary,
            modifier = Modifier.padding(vertical = 4.dp)
          )
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            OutlinedButton(
              onClick = { onResolveConflict(false) },
              modifier = Modifier.padding(end = 8.dp)
            ) {
              Text("Keep Remote", fontSize = 11.sp)
            }
            androidx.compose.material3.Button(
              onClick = { onResolveConflict(true) }
            ) {
              Text("Keep Local", fontSize = 11.sp)
            }
          }
        }
      }
    }
  }
}

@Composable
fun SyncStatusBadge(status: String) {
  val (bgColor, textColor, label, icon) = when (status) {
    "SYNCED" -> Quadruple(CyberAccentGreen.copy(alpha = 0.15f), CyberAccentGreen, "Synced", Icons.Default.CheckCircle)
    "SYNCING" -> Quadruple(CyberAccentAmber.copy(alpha = 0.15f), CyberAccentAmber, "Syncing", Icons.Default.CloudSync)
    "PENDING_UPLOAD" -> Quadruple(CyberAccentBlue.copy(alpha = 0.15f), CyberAccentBlue, "Pending", Icons.Default.CloudUpload)
    "CONFLICT" -> Quadruple(CyberAccentRed.copy(alpha = 0.15f), CyberAccentRed, "Conflict", Icons.Default.Warning)
    else -> Quadruple(CyberTextMuted.copy(alpha = 0.15f), CyberTextMuted, "Local", Icons.Default.Folder)
  }

  Surface(
    shape = RoundedCornerShape(12.dp),
    color = bgColor,
    modifier = Modifier.padding(horizontal = 4.dp)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = textColor,
        modifier = Modifier.size(11.dp)
      )
      Spacer(modifier = Modifier.width(3.dp))
      Text(
        text = label,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = textColor
      )
    }
  }
}

@Composable
fun DeviceCard(
  device: DeviceEntity,
  modifier: Modifier = Modifier
) {
  val deviceIcon = when (device.deviceType) {
    "PHONE" -> Icons.Default.PhoneAndroid
    "TABLET" -> Icons.Default.TabletMac
    "LAPTOP" -> Icons.Default.Laptop
    else -> Icons.Default.Laptop
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("device_card_${device.id}"),
    colors = CardDefaults.cardColors(
      containerColor = if (device.isCurrentDevice) CyberDarkSurfaceElevated else CyberDarkSurfaceVariant
    ),
    shape = RoundedCornerShape(14.dp),
    border = CardDefaults.outlinedCardBorder().copy(
      brush = androidx.compose.ui.graphics.SolidColor(
        if (device.isCurrentDevice) CyberPrimary.copy(alpha = 0.5f) else CyberBorder
      )
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(
            if (device.isOnline) CyberSecondary.copy(alpha = 0.12f) else CyberDarkBackground
          )
          .border(
            1.dp,
            if (device.isOnline) CyberSecondary.copy(alpha = 0.4f) else CyberBorder,
            RoundedCornerShape(10.dp)
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = deviceIcon,
          contentDescription = device.deviceType,
          tint = if (device.isOnline) CyberSecondary else CyberTextMuted,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = device.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = CyberTextPrimary
          )
          if (device.isCurrentDevice) {
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = CyberPrimary.copy(alpha = 0.2f)
            ) {
              Text(
                text = "THIS DEVICE",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = CyberPrimary,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
              )
            }
          }
        }

        Text(
          text = "${device.ipAddress} • Fingerprint: ${device.keyFingerprint}",
          fontSize = 11.sp,
          color = CyberTextMuted,
          fontFamily = FontFamily.Monospace,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (device.isOnline) CyberAccentGreen.copy(alpha = 0.15f) else CyberDarkBackground
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(6.dp)
              .clip(CircleShape)
              .background(if (device.isOnline) CyberAccentGreen else CyberTextMuted)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (device.isOnline) "Connected" else "Offline",
            fontSize = 10.sp,
            color = if (device.isOnline) CyberAccentGreen else CyberTextMuted,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }
  }
}

private fun formatFileSize(bytes: Long): String {
  return when {
    bytes >= 1024 * 1024 -> "%.1f MB".format(bytes / (1024f * 1024f))
    bytes >= 1024 -> "%.1f KB".format(bytes / 1024f)
    else -> "$bytes B"
  }
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
