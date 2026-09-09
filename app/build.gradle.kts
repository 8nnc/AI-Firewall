import java.io.File
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.aifirewall.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aifirewall.app"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            val envKeystorePath: String? = System.getenv("RELEASE_KEYSTORE_PATH") ?: System.getenv("KEYSTORE_PATH")
            val envKeystorePassword: String? = System.getenv("RELEASE_KEYSTORE_PASSWORD") ?: System.getenv("KEYSTORE_PASSWORD")
            val envKeyAlias: String? = System.getenv("RELEASE_KEY_ALIAS") ?: System.getenv("KEY_ALIAS")
            val envKeyPassword: String? = System.getenv("RELEASE_KEY_PASSWORD") ?: System.getenv("KEY_PASSWORD")

            val keystorePropertiesFile = rootProject.file("keystore.properties")
            val props = Properties()
            if (keystorePropertiesFile.exists()) {
                keystorePropertiesFile.inputStream().use { props.load(it) }
            }

            val resolvedStorePath: String? = envKeystorePath 
                ?: props.getProperty("storeFile") 
                ?: props.getProperty("RELEASE_KEYSTORE_PATH")
            val resolvedStorePass: String? = envKeystorePassword 
                ?: props.getProperty("storePassword") 
                ?: props.getProperty("RELEASE_KEYSTORE_PASSWORD")
            val resolvedKeyAlias: String? = envKeyAlias 
                ?: props.getProperty("keyAlias") 
                ?: props.getProperty("RELEASE_KEY_ALIAS")
            val resolvedKeyPass: String? = envKeyPassword 
                ?: props.getProperty("keyPassword") 
                ?: props.getProperty("RELEASE_KEY_PASSWORD")

            if (!resolvedStorePath.isNullOrBlank() &&
                !resolvedStorePass.isNullOrBlank() &&
                !resolvedKeyAlias.isNullOrBlank() &&
                !resolvedKeyPass.isNullOrBlank()
            ) {
                val rawFile = File(resolvedStorePath)
                val targetFile = if (rawFile.isAbsolute) {
                    rawFile
                } else {
                    val fromRoot = File(rootProject.projectDir, resolvedStorePath)
                    if (fromRoot.exists()) fromRoot else File(projectDir, resolvedStorePath)
                }
                storeFile = targetFile.absoluteFile
                storePassword = resolvedStorePass
                keyAlias = resolvedKeyAlias
                keyPassword = resolvedKeyPass
                enableV1Signing = true
                enableV2Signing = true
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    
    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    
    // DataStore (Preferences)
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    
    // Security & Biometrics
    implementation("androidx.biometric:biometric:1.2.0-alpha05")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    
    // JSON Serialization
    implementation("com.google.code.gson:gson:2.11.0")

    // Room Database
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.12.0")
    androidTestImplementation("androidx.test.ext:junit:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

gradle.taskGraph.whenReady {
    val isReleaseRequested = allTasks.any {
        it.name.contains("Release", ignoreCase = true) &&
            (it.name.startsWith("assemble") || it.name.startsWith("bundle") || it.name.startsWith("package"))
    }
    if (isReleaseRequested) {
        val releaseConfig = android.signingConfigs.findByName("release")
        val sFile = releaseConfig?.storeFile
        if (sFile == null || !sFile.exists()) {
            throw GradleException(
                "Release signing credentials not configured or keystore file does not exist.\n" +
                "Please configure environment variables (RELEASE_KEYSTORE_PATH, RELEASE_KEYSTORE_PASSWORD, RELEASE_KEY_ALIAS, RELEASE_KEY_PASSWORD)\n" +
                "or configure a local keystore.properties file with storeFile, storePassword, keyAlias, keyPassword."
            )
        }
    }
}
