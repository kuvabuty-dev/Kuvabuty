package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeviceEntity
import com.example.ui.components.DeviceCard
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

@Composable
fun SyncScreen(
  devices: List<DeviceEntity>,
  isSyncing: Boolean,
  syncProgress: Float,
  networkSpeedKbps: Int,
  onManualSync: () -> Unit,
  onSimulateRemoteChange: () -> Unit,
  onSimulateConflict: () -> Unit,
  onPairNewDeviceClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "radar_pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.85f,
    targetValue = 1.25f,
    animationSpec = infiniteRepeatable(
      animation = tween(1500, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scale"
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(CyberDarkBackground)
      .testTag("sync_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Real-Time Cross-Device Status Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .testTag("sync_status_card"),
        colors = CardDefaults.cardColors(containerColor = CyberDarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(
          brush = Brush.verticalGradient(
            colors = listOf(
              if (isSyncing) CyberAccentAmber.copy(alpha = 0.6f) else CyberPrimary.copy(alpha = 0.5f),
              CyberBorder
            )
          )
        )
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(
                    if (isSyncing) CyberAccentAmber.copy(alpha = 0.15f) else CyberPrimary.copy(alpha = 0.15f)
                  )
                  .border(
                    1.dp,
                    if (isSyncing) CyberAccentAmber else CyberPrimary,
                    CircleShape
                  ),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = if (isSyncing) Icons.Default.CloudSync else Icons.Default.CloudDone,
                  contentDescription = null,
                  tint = if (isSyncing) CyberAccentAmber else CyberPrimary,
                  modifier = Modifier
                    .size(24.dp)
                    .then(if (isSyncing) Modifier.scale(pulseScale) else Modifier)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = if (isSyncing) "REAL-TIME SYNCHRONIZING..." else "REAL-TIME SYNC ACTIVE",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp,
                  color = if (isSyncing) CyberAccentAmber else CyberSecondary
                )
                Text(
                  text = "${devices.count { it.isOnline }} Connected Endpoints",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = CyberTextPrimary
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(10.dp),
              color = CyberDarkSurfaceElevated,
              border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.Speed,
                  contentDescription = null,
                  tint = CyberPrimary,
                  modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "$networkSpeedKbps KB/s",
                  fontSize = 11.sp,
                  fontFamily = FontFamily.Monospace,
                  color = CyberPrimary,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          LinearProgressIndicator(
            progress = { if (isSyncing) syncProgress else 1f },
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(RoundedCornerShape(3.dp)),
            color = if (isSyncing) CyberAccentAmber else CyberSecondary,
            trackColor = CyberDarkSurfaceVariant
          )

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = if (isSyncing) "Transferring and verifying encrypted data blocks across peer devices..."
            else "All cryptographic hashes verified across active device mesh.",
            fontSize = 12.sp,
            color = CyberTextSecondary
          )
        }
      }
    }

    // Cross-Device Simulation Actions
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CyberDarkSurfaceVariant),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(
          brush = androidx.compose.ui.graphics.SolidColor(CyberBorder)
        )
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.NetworkCheck, contentDescription = null, tint = CyberPrimary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Real-Time Sync Sandbox",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = CyberTextPrimary
            )
          }

          Text(
            text = "Trigger simulated live events from your paired remote devices to inspect real-time sync in action:",
            fontSize = 12.sp,
            color = CyberTextSecondary,
            modifier = Modifier.padding(vertical = 8.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = onSimulateRemoteChange,
              enabled = !isSyncing,
              modifier = Modifier
                .weight(1f)
                .testTag("simulate_remote_sync_button"),
              colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = CyberDarkBackground),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Remote Edit", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
              onClick = onSimulateConflict,
              enabled = !isSyncing,
              modifier = Modifier
                .weight(1f)
                .testTag("simulate_conflict_button"),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Warning, contentDescription = null, tint = CyberAccentRed, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Emit Conflict", fontSize = 11.sp, color = CyberAccentRed)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedButton(
            onClick = onManualSync,
            enabled = !isSyncing,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("force_sync_button"),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = CyberSecondary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Force Full Peer Mesh Sync", fontSize = 12.sp, color = CyberSecondary)
          }
        }
      }
    }

    // Paired Devices Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Devices, contentDescription = null, tint = CyberSecondary, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Paired Endpoints (${devices.size})",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = CyberTextPrimary
          )
        }

        OutlinedButton(
          onClick = onPairNewDeviceClick,
          modifier = Modifier.testTag("pair_new_device_button"),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Pair Device", fontSize = 11.sp)
        }
      }
    }

    // Paired Device Cards
    items(devices, key = { it.id }) { device ->
      DeviceCard(device = device)
    }

    // Zero Knowledge Note
    item {
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = CyberDarkSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            tint = CyberAccentGreen,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "P2P ZERO-KNOWLEDGE RELAY",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = CyberAccentGreen
            )
            Text(
              text = "Encrypted blobs stream across devices via end-to-end encrypted protocol. The relay server cannot read filenames, contents, or directory trees.",
              fontSize = 11.sp,
              color = CyberTextSecondary
            )
          }
        }
      }
    }
  }
}
