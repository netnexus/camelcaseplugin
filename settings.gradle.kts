pluginManagement {
    val gradlePluginMirrorUrl = System.getenv("GRADLE_PLUGIN_MIRROR_URL")

    repositories {
        if (gradlePluginMirrorUrl != null) {
            maven(gradlePluginMirrorUrl)
        }
        gradlePluginPortal()
    }
}

rootProject.name = "camelcaseplugin"
