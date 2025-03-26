plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("maven-publish")
}

group = "com.hachther.mesomb"
version = "2.0.0"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.json:json:20250107")

    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

gradlePlugin {
    plugins {
        create("mesomb") {
            id = "com.hachther.mesomb"
            implementationClass = "com.hachther.mesomb.MeSomb"
        }
    }
}

publishing {
    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}