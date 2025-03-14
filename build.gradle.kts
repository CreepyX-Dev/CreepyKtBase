plugins {
    kotlin("jvm") version "2.1.10"
    id ("com.gradleup.shadow") version "8.3.3"
    `maven-publish`
}

group = "com.creepyx.creepyktbase"
version = properties["version.project"] as String

tasks {
    register("sourceJar", Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/CreepyX-Dev/CreeepyBase")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?: findProperty("gpr.key") as String?
            }
//            authentication {
//                header(HttpHeaderAuthentication)
//            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
//            artifact(tasks.named("sourcesJar").get())
        }
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.codemc.org/repository/maven-public/")
    }
    maven {
        url = uri("file://C:/Users/Benny/.m2/repository")
    }
    maven {
        url = uri("https://maven.enginehub.org/repo/")
    }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        name = "worldedit-repo"
        url = uri("https://mvn.intellectualsites.com/content/repositories/releases/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:${project.properties["version.paper"]}-R0.1-SNAPSHOT")
    compileOnly ("dev.jorel:commandapi-bukkit-shade-mojang-mapped:${project.properties["version.commandapi"]}")
    compileOnly ("org.jetbrains:annotations:25.0.0")
    compileOnly ("org.projectlombok:lombok:1.18.34")
    compileOnly ("com.sk89q.worldguard:worldguard-bukkit:7.0.12")
    annotationProcessor ("org.jetbrains:annotations:25.0.0")
    annotationProcessor ("org.projectlombok:lombok:1.18.34")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

configurations.configureEach {
    resolutionStrategy {
        eachDependency {
            if (requested.group == "com.google.code.gson" && requested.name == "gson") {
                // Force all dependencies to use Gson 2.11.0
                useVersion("2.11.0")
            }
            if (requested.group == "com.google.guava" && requested.name == "guava") {
                // Force all dependencies to use Guava 30.1-jre
                useVersion("32.0.1-jre")
            }
            if (requested.group == "it.unimi.dsi" && requested.name == "fastutil") {
                // Force the use of fastutil version 8.5.15
                useVersion("8.5.15")
            }
        }
    }
}