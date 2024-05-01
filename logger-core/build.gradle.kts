plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
}

dependencies {
    implementation(libs.slf4j.api)
    api(libs.bundles.jackson)
    api(libs.bundles.logback)

    testImplementation(libs.jupiter.engine)
    testImplementation(libs.mockk)
}

java {
    withJavadocJar()
    withSourcesJar()
}

task<Jar>("coreSourcesJar") {
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
        register<MavenPublication>("core") {
            artifactId = "logger-core"
            from(components["java"])
            artifact(tasks["coreSourcesJar"])
        }
    }
}
