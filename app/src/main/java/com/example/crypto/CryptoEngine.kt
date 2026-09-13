package com.example.crypto

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object CryptoEngine {
  private const val ALGORITHM = "AES"
  private const val TRANSFORMATION = "AES/GCM/NoPadding"
  private const val GCM_TAG_LENGTH_BITS = 128
  private const val IV_LENGTH_BYTES = 12
  private const val SALT_LENGTH_BYTES = 16
  private const val PBKDF2_ITERATIONS = 65536
  private const val KEY_LENGTH_BITS = 256

  private val secureRandom = SecureRandom()

  fun generateSalt(): ByteArray {
    val salt = ByteArray(SALT_LENGTH_BYTES)
    secureRandom.nextBytes(salt)
    return salt
  }

  fun generateIv(): ByteArray {
    val iv = ByteArray(IV_LENGTH_BYTES)
    secureRandom.nextBytes(iv)
    return iv
  }

  fun deriveKey(passphrase: String, salt: ByteArray): SecretKey {
    val spec = PBEKeySpec(passphrase.toCharArray(), salt, PBKDF2_ITERATIONS, KEY_LENGTH_BITS)
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val keyBytes = factory.generateSecret(spec).encoded
    return SecretKeySpec(keyBytes, ALGORITHM)
  }

  fun encrypt(data: ByteArray, secretKey: SecretKey, iv: ByteArray): ByteArray {
    val cipher = Cipher.getInstance(TRANSFORMATION)
    val parameterSpec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec)
    return cipher.doFinal(data)
  }

  fun decrypt(encryptedData: ByteArray, secretKey: SecretKey, iv: ByteArray): ByteArray {
    val cipher = Cipher.getInstance(TRANSFORMATION)
    val parameterSpec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
    cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec)
    return cipher.doFinal(encryptedData)
  }

  fun calculateSha256(data: ByteArray): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(data)
    return hashBytes.joinToString("") { "%02x".format(it) }
  }

  fun generateKeyFingerprint(keyBytes: ByteArray): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(keyBytes)
    return hash.take(8).joinToString(":") { "%02X".format(it) }
  }

  fun toBase64(bytes: ByteArray): String {
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
  }

  fun fromBase64(str: String): ByteArray {
    return Base64.decode(str, Base64.NO_WRAP)
  }

  fun toHex(bytes: ByteArray): String {
    return bytes.joinToString("") { "%02x".format(it) }
  }
}
