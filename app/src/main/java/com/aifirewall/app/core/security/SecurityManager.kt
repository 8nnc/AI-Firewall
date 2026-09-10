package com.aifirewall.app.core.security

import android.content.Context
import android.content.SharedPreferences
import android.os.SystemClock
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class SecurityManager private constructor(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "security_secrets",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _isSecurityLockEnabled = MutableStateFlow(encryptedPrefs.getBoolean(KEY_LOCK_ENABLED, false))
    val isSecurityLockEnabled: StateFlow<Boolean> = _isSecurityLockEnabled.asStateFlow()

    private val _isBiometricEnabled = MutableStateFlow(encryptedPrefs.getBoolean(KEY_BIOMETRIC_ENABLED, false))
    val isBiometricEnabled: StateFlow<Boolean> = _isBiometricEnabled.asStateFlow()

    private val _timeoutMinutes = MutableStateFlow(encryptedPrefs.getInt(KEY_TIMEOUT_MINUTES, 0))
    val timeoutMinutes: StateFlow<Int> = _timeoutMinutes.asStateFlow()

    private var lastUnlockTime: Long = 0L

    companion object {
        @Volatile
        private var INSTANCE: SecurityManager? = null

        fun getInstance(context: Context): SecurityManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SecurityManager(context.applicationContext).also { INSTANCE = it }
            }
        }

        private const val KEY_LOCK_ENABLED = "security_lock_enabled"
        private const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"
        private const val KEY_TIMEOUT_MINUTES = "timeout_minutes"
        private const val KEY_PIN_HASH = "pin_hash"
        private const val KEY_PIN_SALT = "pin_salt"
        
        private const val ITERATION_COUNT = 10000
        private const val KEY_LENGTH = 256
    }

    fun isLocked(): Boolean {
        if (!_isSecurityLockEnabled.value) return false
        
        val timeoutMs = _timeoutMinutes.value * 60 * 1000L
        if (timeoutMs == 0L) return true // Immediate
        
        val elapsed = SystemClock.elapsedRealtime() - lastUnlockTime
        return elapsed > timeoutMs
    }

    fun unlockSession() {
        lastUnlockTime = SystemClock.elapsedRealtime()
    }

    fun setSecurityLockEnabled(enabled: Boolean) {
        encryptedPrefs.edit().putBoolean(KEY_LOCK_ENABLED, enabled).apply()
        _isSecurityLockEnabled.value = enabled
        if (!enabled) {
            // Clear PIN when disabling lock
            encryptedPrefs.edit().remove(KEY_PIN_HASH).remove(KEY_PIN_SALT).apply()
            setBiometricEnabled(false)
        }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        encryptedPrefs.edit().putBoolean(KEY_BIOMETRIC_ENABLED, enabled).apply()
        _isBiometricEnabled.value = enabled
    }

    fun setTimeoutMinutes(minutes: Int) {
        encryptedPrefs.edit().putInt(KEY_TIMEOUT_MINUTES, minutes).apply()
        _timeoutMinutes.value = minutes
    }

    fun setPin(pin: String) {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        
        val hash = hashPin(pin, salt)
        
        encryptedPrefs.edit()
            .putString(KEY_PIN_HASH, Base64.encodeToString(hash, Base64.NO_WRAP))
            .putString(KEY_PIN_SALT, Base64.encodeToString(salt, Base64.NO_WRAP))
            .apply()
    }

    fun verifyPin(pin: String): Boolean {
        val hashStr = encryptedPrefs.getString(KEY_PIN_HASH, null) ?: return false
        val saltStr = encryptedPrefs.getString(KEY_PIN_SALT, null) ?: return false
        
        val storedHash = Base64.decode(hashStr, Base64.NO_WRAP)
        val salt = Base64.decode(saltStr, Base64.NO_WRAP)
        
        val attemptHash = hashPin(pin, salt)
        return storedHash.contentEquals(attemptHash)
    }

    fun hasPinConfigured(): Boolean {
        return encryptedPrefs.contains(KEY_PIN_HASH) && encryptedPrefs.contains(KEY_PIN_SALT)
    }

    private fun hashPin(pin: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(pin.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        return factory.generateSecret(spec).encoded
    }
}
