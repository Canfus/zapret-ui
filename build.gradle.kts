import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  kotlin("multiplatform") version "2.0.0"
  id("org.jetbrains.compose") version "1.7.0"
  id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
  kotlin("plugin.compose") version "2.0.0"
}

repositories {
  mavenCentral()
  google()
  maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
  jvm("desktop") {
    withJava()
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
      }
    }

    val desktopMain by getting {
      dependencies {
        implementation(compose.desktop.currentOs)
        implementation(compose.materialIconsExtended)

        implementation("io.ktor:ktor-client-cio:2.3.12")
        implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
        implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
      }
    }
  }
}

compose.desktop {
  application {
    mainClass = "ru.zapret.MainKt"

    nativeDistributions {
      targetFormats(TargetFormat.Exe)
      packageName = "ZapretUI"
      packageVersion = "1.0.0"

      windows {
        menuGroup = "Network Utilities"
      }
    }
  }
}