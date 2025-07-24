plugins {
    kotlin("jvm") version "2.2.0" apply false
}

version = "1.0.0"
group = "com.anatnaso.nasocoin"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    version = rootProject.version

    dependencies {
        add("implementation", kotlin("stdlib"))
        add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
        add("implementation", "org.slf4j:slf4j-simple:2.0.17")
        add("implementation", "commons-codec:commons-codec:1.18.0")
        add("implementation", "com.google.code.gson:gson:2.13.1")
    }
}

tasks.register("buildAll") {
    dependsOn(
        subprojects.mapNotNull { subproject ->
            subproject.tasks.findByName("shadowJar")
        }
    )
}