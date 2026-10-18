plugins {
    java
    id("org.jetbrains.intellij.platform") version "2.18.1"
}

group = "de.netnexus"
version = "3.0.14"

val mavenMirrorUrl = providers.environmentVariable("MAVEN_MIRROR_URL").orNull
val localIdePath = providers.environmentVariable("LOCAL_IDE_PATH").orNull
val skipIntelliJAuxiliaryTasks = providers.environmentVariable("SKIP_INTELLIJ_AUXILIARY_TASKS")
    .map(String::toBoolean)
    .getOrElse(false)

repositories {
    if (mavenMirrorUrl != null) {
        maven(mavenMirrorUrl)
    }
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

sourceSets {
    main {
        java.setSrcDirs(listOf("src"))
        resources.srcDir(".")
        resources.include("META-INF/**")
    }
}

dependencies {
    implementation("commons-lang:commons-lang:2.6")

    intellijPlatform {
        if (localIdePath != null) {
            local(localIdePath)
        } else {
            intellijIdea("2026.2.2.1")
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(11)
}

intellijPlatform {
    buildSearchableOptions = !skipIntelliJAuxiliaryTasks
    instrumentCode = !skipIntelliJAuxiliaryTasks

    pluginConfiguration {
        ideaVersion {
            sinceBuild = "193.5233.102"
            untilBuild = provider { null }
        }
    }
}
