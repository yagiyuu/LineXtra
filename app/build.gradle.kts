plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("release.jks")
            storePassword = System.getenv("L_STORE_PASSWORD")
            keyAlias = System.getenv("L_KEY_ALIAS")
            keyPassword = System.getenv("L_KEY_PASSWORD")
        }
    }
    namespace = "io.github.yagiyuu.linextra"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.github.yagiyuu.linextra"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.1"
        setProperty("archivesBaseName", "${rootProject.name}-${versionName}-${versionCode}")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packaging {
        resources.excludes.addAll(
            listOf(
                "kotlin-tooling-metadata.json",
                "kotlin/**"
            )
        )
    }
}

dependencies {
    compileOnly(files("src/jniLibs/api-82.jar"))
    compileOnly(files("src/jniLibs/api-82-sources.jar"))
}