plugins {
    id("java")
}

group = "org.nwolfhub"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("com.github.pengrad:java-telegram-bot-api:7.1.1")
    implementation("us.codecraft:xsoup:0.3.7")
    implementation("org.nwolfhub:utils:1.2.7")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}