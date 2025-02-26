package com.gxd.demo.lib.dal

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class KeystoreManager(private val context: Context) {
    companion object {
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        private const val KEY_ALIAS = "my_secret_key_alias"
        private const val AES_MODE = "AES/GCM/NoPadding"

        /**
         * GCM 推荐 12 字节 IV
         */
        private const val IV_SIZE_BYTES = 12
    }

    /**
     * 初始化 KeyStore
     */
    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(KEYSTORE_PROVIDER).apply { load(null) }
    }

    /**
     * 生成或获取 AES 密钥
     */
    private fun getOrCreateSecretKey(): SecretKey {
        if (!keyStore.containsAlias(KEY_ALIAS)) generateSecretKey()
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    /**
     * 生成 AES 密钥（兼容 API 23+）
     */
    private fun generateSecretKey() {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER)

        val builder = KeyGenParameterSpec.Builder(
            KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).apply {
            setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            setKeySize(256)
            setRandomizedEncryptionRequired(true) // 强制随机 IV
            // 如果设备支持 StrongBox
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) setIsStrongBoxBacked(true)
        }

        keyGenerator.init(builder.build())
        keyGenerator.generateKey()
    }

    /**
     * 加密数据
     */
    fun encryptData(plaintext: String): String {
        val secretKey = getOrCreateSecretKey()
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        // 获取 IV 并拼接密文
        val iv = cipher.iv
        val ciphertext = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))

        val outputStream = ByteArrayOutputStream()
        outputStream.write(iv)
        outputStream.write(ciphertext)

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    /**
     * 解密数据
     */
    fun decryptData(encryptedData: String): String {
        val secretKey = getOrCreateSecretKey()
        val cipher = Cipher.getInstance(AES_MODE)
        val encryptedBytes = Base64.decode(encryptedData, Base64.DEFAULT)

        val inputStream = ByteArrayInputStream(encryptedBytes)
        val iv = ByteArray(IV_SIZE_BYTES)
        inputStream.read(iv)
        val ciphertext = inputStream.readBytes()

        val spec = GCMParameterSpec(128, iv) // GCM 标签长度 128 位
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        val plaintext = cipher.doFinal(ciphertext)
        return String(plaintext, Charsets.UTF_8)
    }

    /**
     * 删除密钥（调试用）
     */
    fun deleteKey() {
        if (keyStore.containsAlias(KEY_ALIAS)) keyStore.deleteEntry(KEY_ALIAS)
    }
}