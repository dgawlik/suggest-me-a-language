import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.22"
    id("io.ktor.plugin") version "2.2.1"
    application
    kotlin("plugin.serialization").version("1.7.22")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.3")
    implementation(kotlin("reflect"))
    implementation(platform("org.http4k:http4k-bom:4.34.4.0"))
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-format-jackson")
    implementation("ch.qos.logback:logback-classic:1.4.5")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainModule.set("org.dgawlik")
    mainClass.set("org.dgawlik.MainKt")
}