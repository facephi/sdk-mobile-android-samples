pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    var user = System.getenv("USERNAME_ARTIFACTORY")
    var token = System.getenv("TOKEN_ARTIFACTORY")
    if (user.isNullOrEmpty() || token.isNullOrEmpty()) {
        val properties = File(rootDir, "local.properties").inputStream().use {
            java.util.Properties().apply { load(it) }
        }
        user = properties.getValue("artifactory.user") as String
        token = properties.getValue("artifactory.token") as String
    }

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://facephicorp.jfrog.io/artifactory/maven-pro-fphi")
            credentials {
                username = user
                password = token
            }
        }
    }
}

rootProject.name = "DemoPhingers"
include(":app")
 