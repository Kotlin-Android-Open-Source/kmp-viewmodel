import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.jetbrains.compose)
  alias(libs.plugins.android.app)
  alias(libs.plugins.kotlin.android)
}

group = "com.hoc081098"
version = "1.0-SNAPSHOT"

repositories {
  jcenter()
}

dependencies {
  implementation(projects.common)
  implementation(libs.androidx.activity.compose)
}

android {
  compileSdk = libs.versions.android.compile.get().toInt()
  namespace = "com.hoc081098.android"

  defaultConfig {
    applicationId = "com.hoc081098.android"
    minSdk = libs.versions.android.min.get().toInt()
    targetSdk = libs.versions.android.target.get().toInt()
    versionCode = 1
    versionName = "1.0-SNAPSHOT"
  }

  // still needed for Android projects despite toolchain
  compileOptions {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.target.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.target.get())
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true
    }
  }

  kotlinOptions {
    jvmTarget = JvmTarget.fromTarget(libs.versions.java.target.get()).target
  }
}
