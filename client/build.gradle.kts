plugins {
    application
    id("com.gradleup.shadow") version "9.0.0-rc1"
}

group = "${rootProject.group}.client"
var mainClassPath = "${group}.MainKt"

dependencies {
    implementation(project(":shared"))
    implementation("ch.qos.logback:logback-classic:1.5.18")
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