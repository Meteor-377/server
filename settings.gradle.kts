plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "server-377"
include("cache")
include("util")
include("server")
include("net")
