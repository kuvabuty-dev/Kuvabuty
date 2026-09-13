package com.example

import com.example.crypto.CryptoEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testAesGcmEncryptionDecryption() {
    val passphrase = "test-master-passphrase-2026"
    val salt = CryptoEngine.generateSalt()
    val key = CryptoEngine.deriveKey(passphrase, salt)
    val iv = CryptoEngine.generateIv()

    val plaintext = "VaultSync Zero-Knowledge Encrypted Payload Data"
    val plainBytes = plaintext.toByteArray(Charsets.UTF_8)

    val cipherBytes = CryptoEngine.encrypt(plainBytes, key, iv)
    assertNotEquals(plaintext, String(cipherBytes, Charsets.UTF_8))

    val decryptedBytes = CryptoEngine.decrypt(cipherBytes, key, iv)
    val decryptedText = String(decryptedBytes, Charsets.UTF_8)
    assertEquals(plaintext, decryptedText)
  }

  @Test
  fun testSha256Checksum() {
    val data = "ZeroKnowledgeHash".toByteArray(Charsets.UTF_8)
    val checksum = CryptoEngine.calculateSha256(data)
    assertTrue(checksum.isNotEmpty())
    assertEquals(64, checksum.length) // SHA-256 is 64 hex characters
  }
}
