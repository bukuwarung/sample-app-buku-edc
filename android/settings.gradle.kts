import java.net.URI

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()

        maven {
            name = "Central Portal Snapshots"
            url = URI(
                "https://central.sonatype.com/repository/maven-snapshots/"
            )

            // Only search this repository for the specific dependency
            content {
                includeModule("com.bukuwarung.edc", "edc-sdk-fat")
            }
        }
        mavenLocal()
        mavenCentral()
    }
}

rootProject.name = "Sample Buku EDC"
include(":app")
include(":domain")
include(":ui")
include(":data")
