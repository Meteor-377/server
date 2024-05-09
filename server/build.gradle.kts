plugins {
    kotlin("jvm")
}

group = "meteor"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":cache"))
    implementation(project(":net"))
    implementation(project(":util"))
    compileOnly("org.jetbrains.kotlin:kotlin-scripting-common:2.0.0-RC2")
    compileOnly("org.jetbrains.kotlin:kotlin-script-runtime:2.0.0-RC2")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("io.github.classgraph:classgraph:4.8.172")
    implementation("com.lambdaworks:scrypt:1.4.0")
    implementation("io.netty:netty-all:4.1.109.Final")
    implementation("com.google.guava:guava:33.2.0-jre")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE // or DuplicatesStrategy.WARN, DuplicatesStrategy.EXCLUDE
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}