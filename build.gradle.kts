import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.ben.manes.versions)
}

val jvmTargetVersion = libs.versions.jvm.get()

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
  testImplementation(libs.playwright)
  testImplementation(libs.kotest.runner.junit5)
}

kotlin {
  jvmToolchain(jvmTargetVersion.toInt())
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED, TestLogEvent.STANDARD_ERROR)
    exceptionFormat = TestExceptionFormat.FULL
    showStandardStreams = false
  }

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
