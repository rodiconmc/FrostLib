

plugins {
    `java-library`
}

repositories {
    jcenter()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
    implementation("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    api("org.jetbrains:annotations:17.0.0")
}

