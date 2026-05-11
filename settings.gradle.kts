rootProject.name = "auth-service"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://repo.maven.apache.org/maven2") }
    }
}

include("contracts")
include("platform")


