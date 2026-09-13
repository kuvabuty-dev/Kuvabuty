package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SyncLogEntity
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
import com.example.ui.theme.CyberTertiary
import com.example.ui.theme.CyberTextMuted
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActivityScreen(
  logs: List<SyncLogEntity>,
  modifier: Modifier = Modifier
) {
  val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(CyberDarkBackground)
      .testTag("activity_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Real-Time Sync Stream",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = CyberTextPrimary
          )
          Text(
            text = "Live cross-device cryptographic event ledger",
            fontSize = 11.sp,
            color = CyberTextSecondary
          )
        }

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = CyberSecondary.copy(alpha = 0.15f),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberSecondary.copy(alpha = 0.4f))
        ) {
          Text(
            text = "${logs.size} EVENTS",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = CyberSecondary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }
    }

    if (logs.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(CyberDarkSurfaceVariant),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "No sync activity recorded yet.",
            color = CyberTextMuted,
            fontSize = 13.sp
          )
        }
      }
    } else {
      items(logs, key = { it.id }) { log ->
        val (icon, color) = when (log.eventType) {
          "UPLOAD" -> Pair(Icons.Default.CloudUpload, CyberAccentBlue)
          "DOWNLOAD" -> Pair(Icons.Default.CloudDownload, CyberPrimary)
          "REMOTE_UPDATE" -> Pair(Icons.Default.Sync, CyberSecondary)
          "CONFLICT_DETECTED" -> Pair(Icons.Default.Warning, CyberAccentRed)
          "CONFLICT_RESOLVE" -> Pair(Icons.Default.CheckCircle, CyberAccentGreen)
          "KEY_VERIFIED" -> Pair(Icons.Default.Security, CyberAccentGreen)
          "DEVICE_PAIRED" -> Pair(Icons.Default.Devices, CyberTertiary)
          else -> Pair(Icons.Default.CloudDone, CyberPrimary)
        }

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("activity_log_${log.id}"),
          colors = CardDefaults.cardColors(containerColor = CyberDarkSurfaceVariant),
          shape = RoundedCornerShape(12.dp),
          border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(CyberBorder)
          )
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.Top
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color.copy(alpha = 0.12f))
                .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(8.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = icon,
                contentDescription = log.eventType,
                tint = color,
                modifier = Modifier.size(18.dp)
              )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = log.fileName,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 13.sp,
                  color = CyberTextPrimary
                )
                Text(
                  text = timeFormat.format(Date(log.timestamp)),
                  fontSize = 10.sp,
                  fontFamily = FontFamily.Monospace,
                  color = CyberTextMuted
                )
              }

              Text(
                text = "${log.eventType} • ${log.deviceName}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = color
              )

              Text(
                text = log.details,
                fontSize = 11.sp,
                color = CyberTextSecondary,
                modifier = Modifier.padding(top = 2.dp)
              )
            }
          }
        }
      }
    }
  }
}
