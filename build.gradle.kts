
import com.novoda.gradle.release.PublishExtension

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("com.novoda:bintray-release:0.9")
    }
}

plugins {
    `java-library`
}

apply(plugin = "com.novoda.bintray-release")

repositories {
    jcenter()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
    implementation("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    api("org.jetbrains:annotations:17.0.0")
}

configure<PublishExtension> {
    repoName = "RodiconRepo"
    userOrg = "rodiconmc"
    groupId = "net.frozenspace"
    artifactId = "FrostLib"
    publishVersion = "1.0.0"
    desc = "FrostLib contains several classes very useful for the development of minecraft plugin. **NOTE: Library note made by us (Author is FrozenLegend) "
    website = "https://github.com/rodiconmc/FrostLib"
}



