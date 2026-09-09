# Keep Android System components & VpnService
-keep public class * extends android.app.Service
-keep public class * extends android.app.Activity
-keep public class * extends android.content.BroadcastReceiver
-keep public class com.aifirewall.app.engine.service.FirewallVpnService { *; }

# Room database rules
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**
-keepclassmembers class * {
    @androidx.room.* <fields>;
    @androidx.room.* <methods>;
}

# Keep Entities and Data Models
-keep class com.aifirewall.app.data.local.db.entity.** { *; }
-keep class com.aifirewall.app.domain.model.** { *; }

# Gson rules
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn com.google.gson.**
-keep class com.google.gson.** { *; }

# Coroutines rules
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Jetpack Compose rules
-keep class androidx.compose.material3.** { *; }

# Tink & javax.annotation rules
-dontwarn javax.annotation.**
-dontwarn com.google.crypto.tink.**
-dontwarn org.checkerframework.**
-keep class com.google.crypto.tink.** { *; }
