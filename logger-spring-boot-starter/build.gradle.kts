plugins {
    `java-library`
    `maven-publish`
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
    kotlin("jvm")
}

dependencies {
    api(project(":logger-core"))
    implementation(libs.bundles.spring)
    implementation(libs.jakarta.servlet.api)

    testImplementation(libs.spring.test)
}

java {
    withJavadocJar()
    withSourcesJar()
}

task<Jar>("springStarterSourcesJar") {
    group = "build"
    archiveClassifier = "sourcesJar"
    from(sourceSets.main.get().allJava)
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/rvigo/logger")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications {
        register<MavenPublication>("springStarter") {
            artifactId = "logger-spring-boot-starter"
            from(components["java"])
            artifact(tasks["springStarterSourcesJar"])
        }
    }
}

tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}
