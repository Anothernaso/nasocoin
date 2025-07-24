plugins {
    application
    id("com.gradleup.shadow") version "9.0.0-rc1"
}

group = "${rootProject.group}.server"
var mainClassPath = "${group}.MainKt"

dependencies {
    implementation(project(":shared"))
    implementation("io.javalin:javalin:6.7.0")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
}

application {
    mainClass = mainClassPath
}

tasks.shadowJar {
    manifest {
        attributes (
            "Main-Class" to mainClassPath
        )
    }
}