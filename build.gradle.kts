plugins {
    kotlin("jvm") version "2.0.21"
    id("application")
}

group = "com.lucaspowered"
version = ""

application {
    mainClass = "com.lucaspowered.bfk.MainKt"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("info.picocli:picocli:4.7.6")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

tasks.jar {
    manifest.attributes["Main-Class"] = "com.lucaspowered.bfk.MainKt"
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
