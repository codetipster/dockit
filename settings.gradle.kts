plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "dockit"

include("dockit-core")
include("dockit-nlp")
include("dockit-viz")
include("dockit-plugin")
