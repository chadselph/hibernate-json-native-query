plugins {
    id("java")
}

group = "me.chadrs.hibernate-test-json"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation("org.hibernate:hibernate-core:6.1.7.Final")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    this.testLogging {
        this.showStandardStreams = true
    }

}