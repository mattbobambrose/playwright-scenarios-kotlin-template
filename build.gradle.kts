import java.io.File
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.ben.manes.versions)
}

dependencies {
  testImplementation(libs.playwright)
  testImplementation(libs.kotest.runner.junit5)
}

kotlin {
  jvmToolchain(libs.versions.jvm.get().toInt())
}

tasks.test {
  useJUnitPlatform()

  testLogging {
    events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED, TestLogEvent.STANDARD_ERROR)
    exceptionFormat = TestExceptionFormat.FULL
    showStandardStreams = true
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

  // Read -Purl / -Pout (or make's url=/out=) at configuration time so the task
  // action stays configuration-cache compatible — no Project access at execution time.
  val urlProp = providers.gradleProperty("url")
  val outProp = providers.gradleProperty("out")

  doFirst {
    val url = urlProp.orNull?.takeIf { it.isNotBlank() }
      ?: error("Provide url=<start-url> (make record) or -Purl=<start-url> (gradle)")
    val out = outProp.orNull?.takeIf { it.isNotBlank() }
      ?: error("Provide out=<output-path> (make record) or -Pout=<output-path> (gradle)")
    args = listOf("codegen", "--target", "java", "-o", out, url)
    File(out).absoluteFile.parentFile?.mkdirs()
  }
}
