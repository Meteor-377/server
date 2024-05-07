plugins {
    kotlin("jvm")
}

group = "meteor"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":util"))
    implementation("io.netty:netty-all:4.1.109.Final")
    implementation("com.google.guava:guava:33.2.0-jre")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}