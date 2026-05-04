# playwright-scenarios-kotlin-template

A starter Kotlin/Gradle project pre-wired for the [`playwright-scenarios`](https://github.com/mattbobambrose/playwright-scenarios) Claude Code plugin. Clone it, install the plugin, and start authoring browser-driven test scenarios as markdown — no host-project setup required.

## What's pre-wired

- **Playwright for Java** + **Kotest** test dependencies in `build.gradle.kts`.
- Two Gradle tasks the plugin invokes:
    - `installPlaywrightBrowsers` — one-time browser binary download.
    - `recordScenario` — launches Playwright codegen, used by `/record-scenario`.
- The partition layout under `src/test/kotlin/com/mattbobambrose/scenarios/`, each holding a no-op `*Test.kt` smoke placeholder so `./gradlew test` passes on a fresh clone:
    - `record/` — output of `/record-scenario`
    - `crawl/` — output of `/crawl-site`
    - `convert/` — output of `/doc-to-scenarios`
- JUnit 5 platform wired up so Kotest StringSpec runs.

## Getting started

1. **Use this template.** Click the green **Use this template → Create a new repository** button at the top of the GitHub page to spin up your own copy. Then clone the new repo locally. (Plain `git clone` of *this* repo also works if you don't want a fresh repo of your own.)
2. **Download the Playwright browsers** (one-time, ~200 MB):
   ```
   ./gradlew installPlaywrightBrowsers
   ```
3. **Open Claude Code** in the project root:
   ```
   claude
   ```
4. **Install the plugin:**
   ```
   /plugin marketplace add mattbobambrose/playwright-scenarios
   /plugin install playwright-scenarios@playwright-scenarios
   ```
5. **Follow the [tutorial](https://mattbobambrose.github.io/playwright-scenarios/tutorial/)** — a 5-step walkthrough that exercises crawl, record, and doc-driven authoring, ending with an executable test suite.

The tutorial's Step 7 has you run `/scaffold-base-test` as your first plugin command. That triggers the config bootstrap (four required fields — accept the defaults; they match this layout) followed by three scaffold customizations (reset endpoint, lifecycle scope, browser — defaults are fine for the tutorial). When it finishes, `BasePageTest.kt` lands at `src/test/kotlin/com/mattbobambrose/BasePageTest.kt` and `base_test_class` is persisted in the config.

## Requirements

- JDK 17 or later (the toolchain is set in `build.gradle.kts`).
- Gradle wrapper is included; you don't need a system Gradle install.
- `playwright-cli` for the live-site exploration commands (`/review-scenario`, `/scenario-to-tests`, `/crawl-site`):
  ```
  npm install -g @playwright/cli@latest
  ```

## Project structure

```
.
├── build.gradle.kts                    # Playwright + Kotest deps and the two Gradle tasks
├── settings.gradle.kts
├── gradle.properties
├── gradlew / gradlew.bat
└── src/
    ├── main/kotlin/Main.kt             # placeholder for your application code
    └── test/kotlin/com/mattbobambrose/scenarios/
        ├── record/                     # generated tests from /record-scenario
        ├── crawl/                      # generated tests from /crawl-site
        └── convert/                    # generated tests from /doc-to-scenarios
```

Scenario markdown lives at `src/test/scenarios/{record,crawl,convert}/` — the partition subdirs are created by `loading-config` on first run. The placeholder `*Test.kt` files in each test partition are smoke tests so `./gradlew test` passes immediately; you can leave them alongside generated tests or delete them once you have your own.

`src/test/docs/` is a suggested home for the input documents you feed to `/doc-to-scenarios`. The plugin doesn't enforce this location — pass any path you like — but having a stable directory keeps your repo tidy.
