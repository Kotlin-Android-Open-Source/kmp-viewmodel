@file:Suppress("UnstableApiUsage", "UNUSED_VARIABLE")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.cocoapods)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.parcelize)
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
    vendor.set(JvmVendorSpec.AZUL)
  }

  androidTarget {
    compilations.all {
      kotlinOptions {
        jvmTarget = JavaVersion.toVersion(libs.versions.java.get()).toString()
      }
    }
  }
  iosX64()
  iosArm64()
  iosSimulatorArm64()

  cocoapods {
    summary = "Some description for the Shared Module"
    homepage = "Link to the Shared Module homepage"
    version = "1.0"
    ios.deploymentTarget = "14.1"
    podfile = project.file("../iosApp/Podfile")
    framework {
      baseName = "shared"

      export(projects.viewmodel)
      export(projects.viewmodelSavedstate)

      export(libs.napier)
      export(libs.coroutines.core)
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(projects.viewmodel)
        api(projects.viewmodelSavedstate)

        api(libs.napier)
        api(libs.coroutines.core)
        api(libs.koin.core)
        api(libs.kotlinx.collections.immutable)

        implementation(libs.kotlinx.serialization.json)
        implementation(libs.flowExt)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
    val androidMain by getting {
      dependencies {
        api(libs.koin.android)
      }
    }
    val androidUnitTest by getting
    val iosX64Main by getting
    val iosArm64Main by getting
    val iosSimulatorArm64Main by getting
    val iosMain by creating {
      dependsOn(commonMain)
      iosX64Main.dependsOn(this)
      iosArm64Main.dependsOn(this)
      iosSimulatorArm64Main.dependsOn(this)

      dependencies {
      }
    }
    val iosX64Test by getting
    val iosArm64Test by getting
    val iosSimulatorArm64Test by getting
    val iosTest by creating {
      dependsOn(commonTest)
      iosX64Test.dependsOn(this)
      iosArm64Test.dependsOn(this)
      iosSimulatorArm64Test.dependsOn(this)
    }
  }
}

android {
  namespace = "com.hoc081098.kmpviewmodelsample"
  compileSdk = 33
  defaultConfig {
    minSdk = 26
  }
  buildFeatures {
    buildConfig = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
  }
  // still needed for Android projects despite toolchain
  compileOptions {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
  }

  dependencies {
    implementation(platform(libs.androidx.compose.bom))
    compileOnly(libs.androidx.compose.runtime)
  }
}

workaroundForIssueKT51970()

// Workaround for https://youtrack.jetbrains.com/issue/KT-51970
fun Project.workaroundForIssueKT51970() {
  afterEvaluate {
    afterEvaluate {
      tasks.configureEach {
        if (
          name.startsWith("compile") &&
          name.endsWith("KotlinMetadata")
        ) {
          enabled = false
        }
      }
    }
  }
}
