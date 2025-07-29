plugins {
    application
    id("com.gradleup.shadow") version "9.0.0-rc1"
}

group = "${rootProject.group}.client"
var mainClassPath = "${group}.MainKt"

dependencies {
    implementation(project(":shared"))
    implementation("ch.qos.logback:logback-classic:1.5.18")

    implementation("io.ktor:ktor-client-core-jvm:3.2.2")
    implementation("io.ktor:ktor-client-logging-jvm:3.2.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:3.2.2")
    implementation("io.ktor:ktor-client-cio-jvm:3.2.2")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:3.2.2")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.9.0")
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