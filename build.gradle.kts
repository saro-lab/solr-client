import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * SARO Solr Client
 *
 * + publish
 * 1. gradle publish
 * 2. https://oss.sonatype.org/
 * 3. Staging Repositories
 * 4. Close -> Release
 *
 * + publish setting
 * 1. create gpg
 * 2. set gradle.properties
 *    - ex windows path) C:/Users/<USER_NAME>/.gradle/gradle.properties
 *    sonatype.username=<username>
 *    sonatype.password=<password>
 *    signing.keyId=<last 8/16 chars in key>
 *    signing.password=<secret>
 *    signing.secretKeyRingFile=<path of secring.gpg>
 * 3. gradlew publish
 *
 * @See
 * https://github.com/saro-lab/rss-stream-reader
 * https://docs.gradle.org/current/userguide/publishing_maven.html
 * https://docs.gradle.org/current/userguide/signing_plugin.html#signing_plugin
 */

plugins {
    kotlin("jvm") version "1.8.10"
    signing
    `maven-publish`
}

group = "me.saro"
version = "1.0"

val scGroupId = group as String
val scArtifactId = "solr-client"
val scVersion = version as String

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

java {
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")

    // test
    val junitVer = "5.9.0"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVer")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVer")
}

publishing {
    publications {
        create<MavenPublication>("maven") {

            groupId = scGroupId
            artifactId = scArtifactId
            version = scVersion

            from(components["java"])

            repositories {
                maven {
                    credentials {
                        username = project.property("sonatype.username").toString()
                        password = project.property("sonatype.password").toString()
                    }
                    val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                    val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
                    url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
                }
            }

            pom {
                name.set("Solr Client")
                description.set("Solr Client by SARO")
                url.set("https://saro.me")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        name.set("PARK Yong Seo")
                        email.set("j@saro.me")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/saro-lab/solr-client.git")
                    developerConnection.set("scm:git:git@github.com:saro-lab/solr-client.git")
                    url.set("https://github.com/saro-lab/solr-client")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}

tasks.withType<Javadoc>().configureEach {
    options {
        this as StandardJavadocDocletOptions
        addBooleanOption("Xdoclint:none", true)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}