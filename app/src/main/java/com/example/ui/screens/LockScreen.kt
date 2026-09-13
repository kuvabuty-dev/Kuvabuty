package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
fun LockScreen(
  keyFingerprint: String,
  onUnlock: (passphrase: String) -> Boolean,
  modifier: Modifier = Modifier
) {
  var passphrase by remember { mutableStateOf("") }
  var hasError by remember { mutableStateOf(false) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(CyberDarkBackground)
      .testTag("lock_screen"),
    contentAlignment = Alignment.Center
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.9f)
        .clip(RoundedCornerShape(24.dp)),
      colors = CardDefaults.cardColors(containerColor = CyberDarkSurface),
      border = CardDefaults.outlinedCardBorder().copy(
        brush = Brush.verticalGradient(
          colors = listOf(CyberPrimary.copy(alpha = 0.6f), CyberBorder)
        )
      )
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Vault Icon Graphic
        Box(
          modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(CyberPrimary.copy(alpha = 0.12f))
            .border(2.dp, CyberPrimary.copy(alpha = 0.4f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(id = R.drawable.img_app_icon),
            contentDescription = "Vault Icon",
            modifier = Modifier.size(60.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
          )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
          text = "VAULTSYNC",
          fontSize = 20.sp,
          fontWeight = FontWeight.Black,
          letterSpacing = 2.sp,
          color = CyberTextPrimary
        )

        Text(
          text = "Zero-Knowledge End-to-End Encrypted Cloud",
          fontSize = 11.sp,
          color = CyberTextSecondary,
          modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = CyberDarkSurfaceVariant,
          border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Security, contentDescription = null, tint = CyberSecondary, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "KEY: $keyFingerprint",
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace,
              color = CyberSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
          value = passphrase,
          onValueChange = {
            passphrase = it
            hasError = false
          },
          label = { Text("Master Passphrase") },
          visualTransformation = PasswordVisualTransformation(),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("vault_passphrase_input"),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CyberPrimary,
            unfocusedBorderColor = CyberBorder,
            focusedTextColor = CyberTextPrimary,
            unfocusedTextColor = CyberTextPrimary
          ),
          singleLine = true
        )

        if (hasError) {
          Text(
            text = "Invalid master key. Try again or use quick unlock.",
            color = CyberAccentRed,
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 6.dp)
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = {
            val success = onUnlock(passphrase)
            if (!success) hasError = true
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("unlock_vault_button"),
          colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = CyberDarkBackground),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Unlock Secure Vault", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
          onClick = { onUnlock("admin") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("quick_unlock_button"),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Fingerprint, contentDescription = null, tint = CyberSecondary, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Biometric / Quick Demo Unlock", color = CyberSecondary, fontSize = 13.sp)
        }
      }
    }
  }
}
