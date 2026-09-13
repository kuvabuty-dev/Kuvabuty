package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.EncryptedFileEntity
import com.example.ui.components.FileCategoryFilterRow
import com.example.ui.components.FileItemCard
import com.example.ui.components.StorageUsageCard
import com.example.ui.theme.CyberAccentGreen
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
fun FilesScreen(
  files: List<EncryptedFileEntity>,
  totalUsedBytes: Long,
  searchQuery: String,
  selectedCategory: String,
  onSearchQueryChange: (String) -> Unit,
  onCategorySelected: (String) -> Unit,
  onFileClick: (EncryptedFileEntity) -> Unit,
  onAuditClick: (EncryptedFileEntity) -> Unit,
  onToggleFavorite: (EncryptedFileEntity) -> Unit,
  onToggleOffline: (EncryptedFileEntity) -> Unit,
  onResolveConflict: (EncryptedFileEntity, Boolean) -> Unit,
  onDeleteFile: (EncryptedFileEntity) -> Unit,
  onUploadClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier.fillMaxSize().background(CyberDarkBackground)) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .testTag("files_list_view"),
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 88.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Hero Banner
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, CyberBorder, RoundedCornerShape(16.dp))
        ) {
          Image(
            painter = painterResource(id = R.drawable.img_vault_hero),
            contentDescription = "Vault Hero",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.horizontalGradient(
                  colors = listOf(
                    CyberDarkBackground.copy(alpha = 0.95f),
                    CyberDarkBackground.copy(alpha = 0.4f)
                  )
                )
              )
          )
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(16.dp),
            verticalArrangement = Arrangement.Center
          ) {
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = CyberPrimary.copy(alpha = 0.2f),
              border = androidx.compose.foundation.BorderStroke(1.dp, CyberPrimary.copy(alpha = 0.4f))
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(CyberAccentGreen)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "ZERO-KNOWLEDGE CLOUD",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = CyberPrimary
                )
              }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "End-to-End Encrypted Storage",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = CyberTextPrimary
            )
            Text(
              text = "Real-time client-side cryptographic synchronization",
              fontSize = 11.sp,
              color = CyberTextSecondary
            )
          }
        }
      }

      // Storage Quota Card
      item {
        StorageUsageCard(
          usedBytes = totalUsedBytes,
          fileCount = files.size
        )
      }

      // Search Bar
      item {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchQueryChange,
          placeholder = { Text("Search encrypted files...", color = CyberTextMuted, fontSize = 13.sp) },
          leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = CyberTextSecondary)
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { onSearchQueryChange("") }) {
                Icon(Icons.Default.Close, contentDescription = "Clear", tint = CyberTextSecondary)
              }
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("file_search_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CyberDarkSurfaceVariant,
            unfocusedContainerColor = CyberDarkSurfaceVariant,
            focusedBorderColor = CyberPrimary,
            unfocusedBorderColor = CyberBorder,
            focusedTextColor = CyberTextPrimary,
            unfocusedTextColor = CyberTextPrimary
          ),
          shape = RoundedCornerShape(12.dp),
          singleLine = true
        )
      }

      // Category Chips
      item {
        FileCategoryFilterRow(
          selectedCategory = selectedCategory,
          onCategorySelected = onCategorySelected
        )
      }

      // Files Section Header
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (selectedCategory == "ALL") "All Encrypted Files (${files.size})" else "$selectedCategory (${files.size})",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = CyberTextSecondary
          )
          Text(
            text = "Tamper-Proof Protected",
            fontSize = 11.sp,
            color = CyberSecondary
          )
        }
      }

      // Empty State
      if (files.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(180.dp)
              .clip(RoundedCornerShape(16.dp))
              .background(CyberDarkSurfaceVariant),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = CyberTextMuted,
                modifier = Modifier.size(36.dp)
              )
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = if (searchQuery.isNotEmpty()) "No encrypted files match '$searchQuery'" else "No files in vault yet",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = CyberTextSecondary
              )
              Text(
                text = "Tap + below to encrypt and upload your first file",
                fontSize = 11.sp,
                color = CyberTextMuted
              )
            }
          }
        }
      } else {
        items(files, key = { it.id }) { file ->
          FileItemCard(
            file = file,
            onViewClick = { onFileClick(file) },
            onAuditClick = { onAuditClick(file) },
            onToggleFavorite = { onToggleFavorite(file) },
            onToggleOffline = { onToggleOffline(file) },
            onResolveConflict = { keepLocal -> onResolveConflict(file, keepLocal) },
            onDelete = { onDeleteFile(file) }
          )
        }
      }
    }

    // Upload FAB
    FloatingActionButton(
      onClick = onUploadClick,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(20.dp)
        .testTag("upload_fab"),
      containerColor = CyberPrimary,
      contentColor = CyberDarkBackground,
      shape = CircleShape
    ) {
      Icon(Icons.Default.Add, contentDescription = "Encrypt and Upload File")
    }
  }
}
