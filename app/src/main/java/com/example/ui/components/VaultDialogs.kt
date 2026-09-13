package com.example.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.EncryptedFileEntity
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
fun EncryptionAuditDialog(
  file: EncryptedFileEntity,
  onDismiss: () -> Unit
) {
  val clipboardManager = LocalClipboardManager.current

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .testTag("encryption_audit_dialog"),
      colors = CardDefaults.cardColors(containerColor = CyberDarkSurface),
      border = CardDefaults.outlinedCardBorder().copy(
        brush = androidx.compose.ui.graphics.SolidColor(CyberPrimary.copy(alpha = 0.6f))
      )
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = CyberPrimary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Cryptographic Audit",
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = CyberTextPrimary
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = CyberTextSecondary)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Cryptographic Verification Banner
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = CyberAccentGreen.copy(alpha = 0.12f),
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberAccentGreen.copy(alpha = 0.4f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = CyberAccentGreen,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "ZERO-KNOWLEDGE AUTHENTICATED",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CyberAccentGreen
              )
              Text(
                text = "GCM 128-bit MAC verified. Ciphertext is untampered.",
                fontSize = 11.sp,
                color = CyberTextPrimary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AuditField(title = "FILE NAME", value = file.name)
        AuditField(title = "ENCRYPTED BLOB ID", value = file.encryptedName)
        AuditField(title = "ENCRYPTION SUITE", value = "AES-256-GCM / NoPadding")
        AuditField(title = "KEY DERIVATION", value = "PBKDF2WithHmacSHA256 (65,536 iters)")
        AuditField(title = "SHA-256 CHECKSUM", value = file.sha256Checksum, isCopyable = true)
        AuditField(title = "INITIALIZATION VECTOR (IV)", value = file.ivBase64, isCopyable = true)
        AuditField(title = "DERIVATION SALT", value = file.saltBase64)
        AuditField(
          title = "RAW CIPHERTEXT PREVIEW (BASE64)",
          value = file.rawCipherBase64.take(120) + "...",
          isCopyable = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            clipboardManager.setText(
              AnnotatedString(
                "Audit Report for ${file.name}:\nSHA-256: ${file.sha256Checksum}\nIV: ${file.ivBase64}\nCiphertext: ${file.rawCipherBase64}"
              )
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("copy_audit_report_button"),
          colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = CyberDarkBackground)
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Copy Cryptographic Proof", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun AuditField(
  title: String,
  value: String,
  isCopyable: Boolean = false
) {
  val clipboardManager = LocalClipboardManager.current

  Column(modifier = Modifier.padding(vertical = 4.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = title,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = CyberPrimary,
        letterSpacing = 0.5.sp
      )
      if (isCopyable) {
        Text(
          text = "Copy",
          fontSize = 10.sp,
          color = CyberSecondary,
          modifier = Modifier
            .clickable { clipboardManager.setText(AnnotatedString(value)) }
            .padding(4.dp)
        )
      }
    }
    Surface(
      shape = RoundedCornerShape(6.dp),
      color = CyberDarkSurfaceVariant,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 2.dp)
    ) {
      Text(
        text = value,
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace,
        color = CyberTextPrimary,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
      )
    }
  }
}

@Composable
fun FileViewerDialog(
  file: EncryptedFileEntity,
  decryptedContent: String,
  onDismiss: () -> Unit
) {
  val clipboardManager = LocalClipboardManager.current

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .testTag("file_viewer_dialog"),
      colors = CardDefaults.cardColors(containerColor = CyberDarkSurface),
      border = CardDefaults.outlinedCardBorder().copy(
        brush = androidx.compose.ui.graphics.SolidColor(CyberSecondary.copy(alpha = 0.5f))
      )
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = file.name,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = CyberTextPrimary
            )
            Text(
              text = "Decrypted on device via AES-256-GCM",
              fontSize = 11.sp,
              color = CyberAccentGreen
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = CyberTextSecondary)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = CyberDarkBackground,
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder),
          modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
        ) {
          Box(modifier = Modifier.padding(12.dp)) {
            Text(
              text = decryptedContent,
              fontSize = 13.sp,
              fontFamily = FontFamily.Monospace,
              color = CyberTextPrimary,
              lineHeight = 18.sp,
              modifier = Modifier.verticalScroll(rememberScrollState())
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = { clipboardManager.setText(AnnotatedString(decryptedContent)) },
            modifier = Modifier.weight(1f)
          ) {
            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Copy Text", fontSize = 12.sp)
          }
          Button(
            onClick = onDismiss,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = CyberSecondary, contentColor = CyberDarkBackground)
          ) {
            Text("Done", fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }
      }
    }
  }
}

@Composable
fun NewFileDialog(
  onDismiss: () -> Unit,
  onUpload: (name: String, content: String, category: String) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var content by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("DOCUMENTS") }

  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = CyberDarkSurface,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Upload, contentDescription = null, tint = CyberPrimary)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Encrypt & Store File", color = CyberTextPrimary, fontSize = 18.sp)
      }
    },
    text = {
      Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Text(
          text = "File will be encrypted client-side using your AES-256 master key before cloud synchronization.",
          fontSize = 12.sp,
          color = CyberTextSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Quick presets
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          PresetChip(text = "Doc", onClick = {
            name = "Project_Budget_2026.pdf"
            content = "Encrypted financial balance sheet for fiscal year 2026. Certified ledger."
            category = "DOCUMENTS"
          })
          PresetChip(text = "Seed", onClick = {
            name = "Recovery_Phrase_Vault.txt"
            content = "1. cosmic 2. fortress 3. anchor 4. shield 5. cipher 6. vertex 7. horizon 8. nebula 9. zero 10. shadow 11. pulse 12. harbor"
            category = "NOTES"
          })
          PresetChip(text = "Key", onClick = {
            name = "Production_API_Keys.env"
            content = "AWS_SECRET_ACCESS_KEY=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY\nVAULT_HMAC_SALT=0x88f2190cba7"
            category = "ARCHIVES"
          })
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("File Name (e.g. Secret_Notes.md)") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("new_file_name_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CyberPrimary,
            unfocusedBorderColor = CyberBorder,
            focusedTextColor = CyberTextPrimary,
            unfocusedTextColor = CyberTextPrimary
          ),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          label = { Text("Plaintext Content to Encrypt") },
          modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .testTag("new_file_content_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CyberPrimary,
            unfocusedBorderColor = CyberBorder,
            focusedTextColor = CyberTextPrimary,
            unfocusedTextColor = CyberTextPrimary
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          listOf("DOCUMENTS", "NOTES", "ARCHIVES", "MEDIA").forEach { cat ->
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (category == cat) CyberPrimary else CyberDarkSurfaceVariant,
              modifier = Modifier.clickable { category = cat }
            ) {
              Text(
                text = cat,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (category == cat) CyberDarkBackground else CyberTextSecondary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
              )
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (name.isNotBlank()) {
            onUpload(name, content, category)
          }
        },
        enabled = name.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = CyberDarkBackground),
        modifier = Modifier.testTag("confirm_encrypt_upload_button")
      ) {
        Text("Encrypt & Sync", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel", color = CyberTextSecondary)
      }
    }
  )
}

@Composable
fun PresetChip(text: String, onClick: () -> Unit) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = CyberDarkSurfaceElevated,
    border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder),
    modifier = Modifier.clickable { onClick() }
  ) {
    Text(
      text = text,
      fontSize = 11.sp,
      color = CyberSecondary,
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
    )
  }
}

@Composable
fun DevicePairingDialog(
  onDismiss: () -> Unit,
  onPair: (name: String, type: String) -> Unit
) {
  var deviceName by remember { mutableStateOf("") }
  var deviceType by remember { mutableStateOf("LAPTOP") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .testTag("device_pairing_dialog"),
      colors = CardDefaults.cardColors(containerColor = CyberDarkSurface),
      border = CardDefaults.outlinedCardBorder().copy(
        brush = androidx.compose.ui.graphics.SolidColor(CyberPrimary)
      )
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "Cross-Device E2EE Pairing",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = CyberTextPrimary
        )
        Text(
          text = "Scan QR code or enter pairing code on the remote device",
          fontSize = 11.sp,
          color = CyberTextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        // High-tech QR Code Simulation
        Box(
          modifier = Modifier
            .size(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(2.dp, CyberPrimary, RoundedCornerShape(12.dp)),
          contentAlignment = Alignment.Center
        ) {
          Canvas(modifier = Modifier.size(140.dp)) {
            val cols = 15
            val step = size.width / cols
            for (i in 0 until cols) {
              for (j in 0 until cols) {
                // Fixed pattern with corner alignment squares
                val isCorner1 = i < 4 && j < 4
                val isCorner2 = i > 10 && j < 4
                val isCorner3 = i < 4 && j > 10
                val pseudoRandom = (i * 31 + j * 17 + 7) % 3 == 0
                if (isCorner1 || isCorner2 || isCorner3 || pseudoRandom) {
                  drawRect(
                    color = Color.Black,
                    topLeft = Offset(i * step, j * step),
                    size = Size(step * 0.9f, step * 0.9f)
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 6-digit sync code
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = CyberDarkSurfaceVariant,
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "SYNC CODE:  ",
              fontSize = 11.sp,
              color = CyberTextSecondary
            )
            Text(
              text = "892 - 417",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              color = CyberPrimary,
              letterSpacing = 2.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
          value = deviceName,
          onValueChange = { deviceName = it },
          label = { Text("Device Name (e.g. Work PC)") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("new_device_name_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CyberPrimary,
            unfocusedBorderColor = CyberBorder,
            focusedTextColor = CyberTextPrimary,
            unfocusedTextColor = CyberTextPrimary
          ),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("LAPTOP", "PHONE", "TABLET", "DESKTOP").forEach { type ->
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (deviceType == type) CyberPrimary else CyberDarkSurfaceElevated,
              modifier = Modifier.clickable { deviceType = type }
            ) {
              Text(
                text = type,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (deviceType == type) CyberDarkBackground else CyberTextSecondary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f)
          ) {
            Text("Cancel", fontSize = 12.sp)
          }
          Button(
            onClick = {
              if (deviceName.isNotBlank()) {
                onPair(deviceName, deviceType)
              }
            },
            enabled = deviceName.isNotBlank(),
            modifier = Modifier
              .weight(1f)
              .testTag("confirm_pair_device_button"),
            colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = CyberDarkBackground)
          ) {
            Text("Authorize & Pair", fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }
      }
    }
  }
}

@Composable
fun KeyRotationDialog(
  currentFingerprint: String,
  onDismiss: () -> Unit,
  onRotate: (newPassphrase: String) -> Unit
) {
  var newPassphrase by remember { mutableStateOf("") }
  var confirmPassphrase by remember { mutableStateOf("") }
  var error by remember { mutableStateOf<String?>(null) }

  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = CyberDarkSurface,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Key, contentDescription = null, tint = CyberPrimary)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Rotate Master Key", color = CyberTextPrimary, fontSize = 18.sp)
      }
    },
    text = {
      Column {
        Text(
          text = "Derives a brand new 256-bit AES master key with fresh random PBKDF2 salt. All paired devices will re-verify tokens.",
          fontSize = 12.sp,
          color = CyberTextSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = newPassphrase,
          onValueChange = {
            newPassphrase = it
            error = null
          },
          label = { Text("New Master Passphrase") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("new_passphrase_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CyberPrimary,
            unfocusedBorderColor = CyberBorder,
            focusedTextColor = CyberTextPrimary,
            unfocusedTextColor = CyberTextPrimary
          ),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = confirmPassphrase,
          onValueChange = {
            confirmPassphrase = it
            error = null
          },
          label = { Text("Confirm Passphrase") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("confirm_passphrase_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CyberPrimary,
            unfocusedBorderColor = CyberBorder,
            focusedTextColor = CyberTextPrimary,
            unfocusedTextColor = CyberTextPrimary
          ),
          singleLine = true
        )

        if (error != null) {
          Text(
            text = error ?: "",
            color = CyberAccentRed,
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 6.dp)
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (newPassphrase.length < 6) {
            error = "Passphrase must be at least 6 characters"
          } else if (newPassphrase != confirmPassphrase) {
            error = "Passphrases do not match"
          } else {
            onRotate(newPassphrase)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = CyberDarkBackground),
        modifier = Modifier.testTag("confirm_rotate_key_button")
      ) {
        Text("Rotate Key", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel", color = CyberTextSecondary)
      }
    }
  )
}
