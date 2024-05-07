plugins {
    kotlin("jvm")
}

group = "meteor"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.bouncycastle:bcprov-jdk18on:1.78.1")
    implementation("com.google.guava:guava:33.2.0-jre")
    implementation("io.netty:netty-all:4.1.109.Final")
    implementation("org.apache.commons:commons-compress:1.26.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}