plugins {
    kotlin("jvm") version "1.9.23"
}

group = "com.emmanuel.pastor.simplesmartapps"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val mordantVersion = "2.7.2"
dependencies {
    implementation("com.github.ajalt.mordant:mordant:$mordantVersion")
    implementation("com.github.ajalt.mordant:mordant-coroutines:$mordantVersion")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "frontends.cli.MainKt"
        )
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}