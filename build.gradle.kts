plugins {
    id("java")
    id("maven-publish")
    id("io.freefair.aspectj") version "8.11"
}

group = "net.renphis.libs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.0")
    implementation("org.apache.logging.log4j:log4j-api:2.24.3")
    implementation("org.apache.logging.log4j:log4j-core:2.24.3")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.aspectj:aspectjrt:1.9.22")
    implementation("org.aspectj:aspectjweaver:1.9.22")

    testImplementation("org.mockito:mockito-core:5.14.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

tasks.test {
    useJUnitPlatform()
}

/**
 * Publish to GitHub Packages
 */
publishing {
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
            groupId = "net.renphis.libs"
            artifactId = project.name
            version = project.version.toString()
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/renphis/config")
            credentials {
                username = System.getenv("USER")
                password = System.getenv("TOKEN")
            }
        }
    }
}