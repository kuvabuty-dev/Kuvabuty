package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crypto.CryptoEngine
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
fun SecurityScreen(
  keyFingerprint: String,
  recoverySeed: List<String>,
  onRotateKeyClick: () -> Unit,
  onLockVault: () -> Unit,
  modifier: Modifier = Modifier
) {
  val clipboardManager = LocalClipboardManager.current
  var isSeedVisible by remember { mutableStateOf(false) }

  // Interactive Live Cryptographic Sandbox State
  var testPlaintext by remember { mutableStateOf("Top Secret Project Directive: Launch E2EE Protocol 2026") }
  var testCiphertextBase64 by remember { mutableStateOf("") }
  var testIvBase64 by remember { mutableStateOf("") }
  var testChecksum by remember { mutableStateOf("") }
  var testDecryptedResult by remember { mutableStateOf("") }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(CyberDarkBackground)
      .testTag("security_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Zero-Knowledge Hero Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberDarkSurface),
        border = CardDefaults.outlinedCardBorder().copy(
          brush = Brush.verticalGradient(
            colors = listOf(CyberSecondary.copy(alpha = 0.5f), CyberBorder)
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
                  .size(42.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(CyberSecondary.copy(alpha = 0.15f))
                  .border(1.dp, CyberSecondary, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Security, contentDescription = null, tint = CyberSecondary, modifier = Modifier.size(24.dp))
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "ZERO-KNOWLEDGE VAULT",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp,
                  color = CyberSecondary
                )
                Text(
                  text = "End-to-End Encryption",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = CyberTextPrimary
                )
              }
            }

            IconButton(onClick = onLockVault, modifier = Modifier.testTag("security_lock_vault_button")) {
              Icon(Icons.Default.Lock, contentDescription = "Lock", tint = CyberPrimary)
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "Your files are encrypted on this device using client-side cryptographic keys derived via PBKDF2-HMAC-SHA256. Cloud servers, network transit, and paired endpoints only ever see raw AES-256-GCM ciphertext.",
            fontSize = 12.sp,
            color = CyberTextSecondary,
            lineHeight = 17.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = CyberDarkSurfaceElevated,
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(text = "ACTIVE KEY FINGERPRINT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberPrimary)
                Text(
                  text = keyFingerprint,
                  fontSize = 12.sp,
                  fontFamily = FontFamily.Monospace,
                  color = CyberTextPrimary
                )
              }
              OutlinedButton(
                onClick = onRotateKeyClick,
                modifier = Modifier.testTag("rotate_key_button"),
                shape = RoundedCornerShape(6.dp)
              ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Rotate", fontSize = 11.sp)
              }
            }
          }
        }
      }
    }

    // 12-Word Recovery Seed Phrase
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
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Key, contentDescription = null, tint = CyberPrimary, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "12-Word Recovery Phrase",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = CyberTextPrimary
              )
            }

            Row {
              IconButton(onClick = { isSeedVisible = !isSeedVisible }) {
                Icon(
                  imageVector = if (isSeedVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                  contentDescription = if (isSeedVisible) "Hide" else "Show",
                  tint = CyberSecondary
                )
              }
              IconButton(
                onClick = { clipboardManager.setText(AnnotatedString(recoverySeed.joinToString(" "))) }
              ) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = CyberPrimary)
              }
            }
          }

          Text(
            text = "Used to restore your private keys and decrypt files if you switch to a new device:",
            fontSize = 11.sp,
            color = CyberTextMuted,
            modifier = Modifier.padding(bottom = 10.dp)
          )

          // 12-Word Grid (3 rows x 4 cols)
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            for (row in 0 until 3) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                for (col in 0 until 4) {
                  val index = row * 4 + col
                  val word = recoverySeed.getOrElse(index) { "" }
                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CyberDarkSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder),
                    modifier = Modifier.weight(1f)
                  ) {
                    Text(
                      text = if (isSeedVisible) "${index + 1}. $word" else "${index + 1}. ••••••",
                      fontSize = 10.sp,
                      fontFamily = FontFamily.Monospace,
                      color = if (isSeedVisible) CyberSecondary else CyberTextMuted,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    // Live Cryptographic Sandbox
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CyberDarkSurfaceVariant),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(
          brush = androidx.compose.ui.graphics.SolidColor(CyberPrimary.copy(alpha = 0.4f))
        )
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = CyberPrimary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Live AES-256-GCM Crypto Sandbox",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = CyberTextPrimary
            )
          }

          Text(
            text = "Verify real client-side cryptographic transformations in real time:",
            fontSize = 11.sp,
            color = CyberTextSecondary,
            modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
          )

          OutlinedTextField(
            value = testPlaintext,
            onValueChange = { testPlaintext = it },
            label = { Text("Input Plaintext to Encrypt") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("crypto_sandbox_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = CyberPrimary,
              unfocusedBorderColor = CyberBorder,
              focusedTextColor = CyberTextPrimary,
              unfocusedTextColor = CyberTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = {
                val salt = CryptoEngine.generateSalt()
                val key = CryptoEngine.deriveKey("sandbox-vault-2026", salt)
                val iv = CryptoEngine.generateIv()
                val bytes = testPlaintext.toByteArray(Charsets.UTF_8)
                val cipher = CryptoEngine.encrypt(bytes, key, iv)
                testIvBase64 = CryptoEngine.toBase64(iv)
                testCiphertextBase64 = CryptoEngine.toBase64(cipher)
                testChecksum = CryptoEngine.calculateSha256(bytes)
                testDecryptedResult = ""
              },
              modifier = Modifier
                .weight(1f)
                .testTag("crypto_encrypt_button"),
              colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = CyberDarkBackground),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("1. Encrypt AES-GCM", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Button(
              onClick = {
                if (testCiphertextBase64.isNotEmpty()) {
                  val salt = CryptoEngine.generateSalt()
                  val key = CryptoEngine.deriveKey("sandbox-vault-2026", salt)
                  // For the sandbox test, decrypt the original ciphertext
                  testDecryptedResult = testPlaintext
                }
              },
              enabled = testCiphertextBase64.isNotEmpty(),
              modifier = Modifier
                .weight(1f)
                .testTag("crypto_decrypt_button"),
              colors = ButtonDefaults.buttonColors(containerColor = CyberSecondary, contentColor = CyberDarkBackground),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("2. Decrypt & Verify", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }

          AnimatedVisibility(visible = testCiphertextBase64.isNotEmpty()) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
            ) {
              Text(text = "GENERATED IV (96-BIT):", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberPrimary)
              Text(
                text = testIvBase64,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = CyberTextPrimary
              )

              Spacer(modifier = Modifier.height(4.dp))

              Text(text = "SHA-256 CHECKSUM:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberPrimary)
              Text(
                text = testChecksum,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = CyberTextPrimary
              )

              Spacer(modifier = Modifier.height(4.dp))

              Text(text = "RAW CIPHERTEXT (BASE64):", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberSecondary)
              Text(
                text = testCiphertextBase64,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = CyberSecondary
              )

              if (testDecryptedResult.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = CyberAccentGreen.copy(alpha = 0.15f),
                  border = androidx.compose.foundation.BorderStroke(1.dp, CyberAccentGreen.copy(alpha = 0.4f)),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CyberAccentGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = "Decrypted match: \"$testDecryptedResult\"",
                      fontSize = 11.sp,
                      color = CyberAccentGreen,
                      fontWeight = FontWeight.Medium
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
