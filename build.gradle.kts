import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  kotlin("multiplatform") version "2.0.0"
  id("org.jetbrains.compose") version "1.6.11"
  id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
  kotlin("plugin.serialization") version "2.0.0"
}

group = "com.github.canfus.zapret"
version = "1.0.0"

repositories {
  mavenCentral()
  google()
  maven("https://maven.pkg.jetbrains.space/public/p/compose/interactive")
}

kotlin {
  jvm("desktop")

  sourceSets {
    val desktopMain by getting {
      dependencies {
        implementation(compose.desktop.currentOs)
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.ui)
        implementation(compose.components.resources)
        implementation(compose.components.uiToolingPreview)

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.1")

        implementation("io.insert-koin:koin-core:3.5.6")
        implementation("io.insert-koin:koin-compose:1.1.5")

        implementation("io.ktor:ktor-client-core:2.3.11")
        implementation("io.ktor:ktor-client-cio:2.3.11")
        implementation("io.ktor:ktor-client-content-negotiation:2.3.11")
        implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.11")

        implementation("io.github.oshai:kotlin-logging-jvm:6.0.3")
      }
    }
  }
}

compose.desktop {
  application {
    mainClass = "MainKt"

    nativeDistributions {
      targetFormats(TargetFormat.Exe, TargetFormat.Msi)
      packageName = "ZapretGUI"
      packageVersion = "1.0.0"
      description = "GUI for zapret-discord-youtube"
      vendor = "Canfus"

      windows {
        shortcut = true
        menu = true
        upgradeUuid = "uuid будет позже"
        // iconFile.set(project.file("icon.ico"))
      }
    }
  }
}