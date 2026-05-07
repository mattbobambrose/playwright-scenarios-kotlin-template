package scenarios.crawl

import io.kotest.core.spec.style.StringSpec

// Smoke-test placeholder so `./gradlew test` passes on a fresh clone.
// Real generated tests will be written by `/scenario-to-tests` to
// <scenario-name>/<ClassName>.kt subdirectories alongside this file,
// each extending BasePageTest (created by `/scaffold-base-test`).
// Safe to delete once you have generated tests of your own.
//
// Don't rename this to BaseCrawlTest / CrawlTestBase / AbstractCrawlTest /
// CrawlPageTest — those patterns match `loading-config`'s base-test-class
// discovery globs and would suppress the BasePageTest scaffold offer.
class CrawlTest : StringSpec({
  "placeholder for crawl scenario tests" {
  }
})
