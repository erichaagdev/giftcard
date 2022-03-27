plugins {
  kotlin("jvm") version "1.6.10"
  application
}

group = "dev.erichaag"
version = "0.0.1-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation("com.eatthepath:java-otp:0.3.1")
  implementation("com.google.guava:guava:31.1-jre")
  implementation("com.microsoft.playwright:playwright:1.17.1")
  implementation(kotlin("stdlib-jdk8"))
}

application {
  mainClass.set("dev.erichaag.giftcard.MainKt")
}
