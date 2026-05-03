plugins {
  kotlin("jvm") version "2.3.20"
}

group = "com.mattbobambrose"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
  testImplementation("com.microsoft.playwright:playwright:1.47.0")
  testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
}

kotlin {
  jvmToolchain(17)
}

tasks.test {
  useJUnitPlatform()
}

tasks.register<JavaExec>("installPlaywrightBrowsers") {
  description = "Download Playwright browser binaries (one-time setup)."
  group = "verification"
  classpath = sourceSets["test"].runtimeClasspath
  mainClass.set("com.microsoft.playwright.CLI")
  args = listOf("install")
}

tasks.register<JavaExec>("recordScenario") {
  description = "Launch Playwright codegen to record a browser flow as a Java file."
  group = "verification"

  classpath = sourceSets["test"].runtimeClasspath
  mainClass.set("com.microsoft.playwright.CLI")

  doFirst {
    val url = project.findProperty("url") as String? ?: error("Provide -Purl=<start-url>")
    val out = project.findProperty("out") as String? ?: error("Provide -Pout=<output-path>")
    args = listOf("codegen", "--target", "java", "-o", out, url)
    file(out).parentFile?.mkdirs()
  }
}
