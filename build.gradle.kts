plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "net.azisaba"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
}

dependencies {
    implementation("jp.ne.paypay:paypayopa:1.0.8")
    compileOnly("com.destroystokyo.paper:paper-api:1.15.2-R0.1-SNAPSHOT")
}

tasks {
    test {
        useJUnitPlatform()
    }

    shadowJar {
        relocate("jp.ne.paypay", "net.azisaba.paypaytestplugin.lib.jp.ne.paypay")
        relocate("okio", "net.azisaba.paypaytestplugin.lib.okio")
        relocate("io.swagger", "net.azisaba.paypaytestplugin.lib.io.swagger")
        relocate("com.auth0", "net.azisaba.paypaytestplugin.lib.com.auth0")
        relocate("com.fasterxml", "net.azisaba.paypaytestplugin.lib.com.fasterxml")
        relocate("com.google", "net.azisaba.paypaytestplugin.lib.com.google")
        relocate("com.squareup", "net.azisaba.paypaytestplugin.lib.com.squareup")
        relocate("com.sun", "net.azisaba.paypaytestplugin.lib.com.sun")
        relocate("jakarta", "net.azisaba.paypaytestplugin.lib.jakarta")
        relocate("kotlin", "net.azisaba.paypaytestplugin.lib.kotlin")
        relocate("org.apache", "net.azisaba.paypaytestplugin.lib.org.apache")
        relocate("org.hibernate", "net.azisaba.paypaytestplugin.lib.org.hibernate")
        relocate("org.intellij", "net.azisaba.paypaytestplugin.lib.org.intellij")
        relocate("org.jetbrains", "net.azisaba.paypaytestplugin.lib.org.jetbrains")
        relocate("org.jboss", "net.azisaba.paypaytestplugin.lib.org.jboss")
        mergeServiceFiles()
    }
}

kotlin {
    jvmToolchain(8)
}
