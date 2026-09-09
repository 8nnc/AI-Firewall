package com.aifirewall.app.core.security;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\t\u0018\u0000 \'2\u00020\u0001:\u0001\'B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0017\u001a\u00020\u0007J\u0018\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0019H\u0002J\u0006\u0010\u001d\u001a\u00020\u0007J\u000e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0007J\u000e\u0010!\u001a\u00020\u001f2\u0006\u0010\u001a\u001a\u00020\u001bJ\u000e\u0010\"\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0007J\u000e\u0010#\u001a\u00020\u001f2\u0006\u0010$\u001a\u00020\nJ\u0006\u0010%\u001a\u00020\u001fJ\u000e\u0010&\u001a\u00020\u00072\u0006\u0010\u001a\u001a\u00020\u001bR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000fR\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00070\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\n0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u000f\u00a8\u0006("}, d2 = {"Lcom/aifirewall/app/core/security/SecurityManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "_isBiometricEnabled", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_isSecurityLockEnabled", "_timeoutMinutes", "", "encryptedPrefs", "Landroid/content/SharedPreferences;", "isBiometricEnabled", "Lkotlinx/coroutines/flow/StateFlow;", "()Lkotlinx/coroutines/flow/StateFlow;", "isSecurityLockEnabled", "lastUnlockTime", "", "masterKey", "Landroidx/security/crypto/MasterKey;", "timeoutMinutes", "getTimeoutMinutes", "hasPinConfigured", "hashPin", "", "pin", "", "salt", "isLocked", "setBiometricEnabled", "", "enabled", "setPin", "setSecurityLockEnabled", "setTimeoutMinutes", "minutes", "unlockSession", "verifyPin", "Companion", "app_debug"})
public final class SecurityManager {
    @org.jetbrains.annotations.NotNull()
    private final androidx.security.crypto.MasterKey masterKey = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences encryptedPrefs = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isSecurityLockEnabled = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isSecurityLockEnabled = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isBiometricEnabled = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isBiometricEnabled = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _timeoutMinutes = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> timeoutMinutes = null;
    private long lastUnlockTime = 0L;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.aifirewall.app.core.security.SecurityManager INSTANCE;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LOCK_ENABLED = "security_lock_enabled";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_BIOMETRIC_ENABLED = "biometric_enabled";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_TIMEOUT_MINUTES = "timeout_minutes";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_PIN_HASH = "pin_hash";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_PIN_SALT = "pin_salt";
    private static final int ITERATION_COUNT = 10000;
    private static final int KEY_LENGTH = 256;
    @org.jetbrains.annotations.NotNull()
    public static final com.aifirewall.app.core.security.SecurityManager.Companion Companion = null;
    
    private SecurityManager(android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isSecurityLockEnabled() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isBiometricEnabled() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getTimeoutMinutes() {
        return null;
    }
    
    public final boolean isLocked() {
        return false;
    }
    
    public final void unlockSession() {
    }
    
    public final void setSecurityLockEnabled(boolean enabled) {
    }
    
    public final void setBiometricEnabled(boolean enabled) {
    }
    
    public final void setTimeoutMinutes(int minutes) {
    }
    
    public final void setPin(@org.jetbrains.annotations.NotNull()
    java.lang.String pin) {
    }
    
    public final boolean verifyPin(@org.jetbrains.annotations.NotNull()
    java.lang.String pin) {
        return false;
    }
    
    public final boolean hasPinConfigured() {
        return false;
    }
    
    private final byte[] hashPin(java.lang.String pin, byte[] salt) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0010R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/aifirewall/app/core/security/SecurityManager$Companion;", "", "()V", "INSTANCE", "Lcom/aifirewall/app/core/security/SecurityManager;", "ITERATION_COUNT", "", "KEY_BIOMETRIC_ENABLED", "", "KEY_LENGTH", "KEY_LOCK_ENABLED", "KEY_PIN_HASH", "KEY_PIN_SALT", "KEY_TIMEOUT_MINUTES", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.aifirewall.app.core.security.SecurityManager getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}