package com.muthuram.faceliveness.helper

import android.util.Base64
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.nio.charset.StandardCharsets
import java.security.DigestException
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object AesEncryption {
    /*fun encrypt(key: String, password: String): String? {
        return try {
            val sr = SecureRandom()
            val salt = ByteArray(8)
            sr.nextBytes(salt)
            val keyAndIV: Array<ByteArray?>? = generateKeyAndIV(
                32, 16, 1, salt, key.toByteArray(StandardCharsets.UTF_8),
                MessageDigest.getInstance("MD5")
            )
            val cipher: Cipher = Cipher.getInstance(
                "AES/CBC/PKCS7Padding",
                BouncyCastleProvider()
            )
            cipher.init(
                Cipher.ENCRYPT_MODE,
                SecretKeySpec(keyAndIV?.get(0), "AES"),
                IvParameterSpec(keyAndIV?.get(1))
            )
            val encryptedData = cipher.doFinal(password.toByteArray(StandardCharsets.UTF_8))
            val prefixAndSaltAndEncryptedData = ByteArray(16 + encryptedData.size)
            // Copy prefix (0-th to 7-th bytes)
            System.arraycopy(
                "Salted__".toByteArray(StandardCharsets.UTF_8),
                0,
                prefixAndSaltAndEncryptedData,
                0,
                8
            )
            // Copy salt (8-th to 15-th bytes)
            System.arraycopy(salt, 0, prefixAndSaltAndEncryptedData, 8, 8)
            // Copy encrypted data (16-th byte and onwards)
            System.arraycopy(
                encryptedData,
                0,
                prefixAndSaltAndEncryptedData,
                16,
                encryptedData.size
            )
            Base64.encodeToString(prefixAndSaltAndEncryptedData, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }*/

    fun encrypt(key: String, password: String): String? {
        return try {
            val keySpec = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), "AES")
            val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", BouncyCastleProvider())
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            val encryptedData = cipher.doFinal(password.toByteArray(StandardCharsets.UTF_8))
            Base64.encodeToString(encryptedData, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    @Suppress("SameParameterValue")
    private fun generateKeyAndIV(
        keyLength: Int,
        ivLength: Int,
        iterations: Int,
        salt: ByteArray?,
        password: ByteArray?,
        md: MessageDigest
    ): Array<ByteArray?>? {
        val digestLength = md.digestLength
        val requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength
        val generatedData = ByteArray(requiredLength)
        var generatedLength = 0
        return try {
            md.reset()

            // Repeat process until sufficient data has been generated
            while (generatedLength < keyLength + ivLength) {

                // Digest data (last digest if available, password data, salt if available)
                if (generatedLength > 0) md.update(
                    generatedData,
                    generatedLength - digestLength,
                    digestLength
                )
                md.update(password!!)
                if (salt != null) md.update(salt, 0, 8)
                md.digest(generatedData, generatedLength, digestLength)

                // additional rounds
                for (i in 1 until iterations) {
                    md.update(generatedData, generatedLength, digestLength)
                    md.digest(generatedData, generatedLength, digestLength)
                }
                generatedLength += digestLength
            }

            // Copy key and IV into separate byte arrays
            val result = arrayOfNulls<ByteArray>(2)
            result[0] = generatedData.copyOfRange(0, keyLength)
            if (ivLength > 0) result[1] =
                generatedData.copyOfRange(keyLength, keyLength + ivLength)
            result
        } catch (e: DigestException) {
            throw RuntimeException(e)
        } finally {
            // Clean out temporary data
            Arrays.fill(generatedData, 0.toByte())
        }
    }

    /*fun decrypt(key: String, encryptedData: String): String? {
        return try {
            val decodedData = Base64.decode(encryptedData, Base64.NO_WRAP)
            val salt = ByteArray(8)

            // Extract the salt from the encoded data
            System.arraycopy(decodedData, 8, salt, 0, 8)

            val keyAndIV: Array<ByteArray?>? = generateKeyAndIV(
                32, 16, 1, salt, key.toByteArray(StandardCharsets.UTF_8),
                MessageDigest.getInstance("MD5")
            )

            val cipher: Cipher = Cipher.getInstance(
                "AES/CBC/PKCS7Padding",
                BouncyCastleProvider()
            )
            cipher.init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(keyAndIV?.get(0), "AES"),
                IvParameterSpec(keyAndIV?.get(1))
            )

            // Decrypt the data excluding the salt and prefix
            val decryptedData = cipher.doFinal(decodedData, 16, decodedData.size - 16)

            String(decryptedData, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }*/

    fun decrypt(key: String, encryptedData: String): String? {
        return try {
            val decodedData = Base64.decode(encryptedData, Base64.NO_WRAP)
            val keySpec = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), "AES")
            val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", BouncyCastleProvider())
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            val decryptedData = cipher.doFinal(decodedData)
            String(decryptedData, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}