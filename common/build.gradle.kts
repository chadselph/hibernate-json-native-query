plugins {
    id("java")
    `java-library`
}

group = "me.chadrs.hibernate-test-json"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("org.postgresql:postgresql:42.7.0")
    implementation("org.hibernate:hibernate-core:6.1.7.Final")
    api("org.testcontainers:postgresql:1.19.7")
    api(platform("org.junit:junit-bom:5.10.0"))
    api("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}