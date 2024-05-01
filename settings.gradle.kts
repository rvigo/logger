plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "logger"
include("logger-core")
include("logger-spring-boot-starter")
