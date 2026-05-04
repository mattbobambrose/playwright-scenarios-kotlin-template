# playwright-scenarios-kotlin-template

A starter Kotlin/Gradle project pre-wired for the [`playwright-scenarios`](https://github.com/mattbobambrose/playwright-scenarios) Claude Code plugin. Clone it, install the plugin, and start authoring browser-driven test scenarios as markdown — no host-project setup required.

## What's pre-wired

- **Playwright for Java** + **Kotest** test dependencies in `build.gradle.kts`.
- Two Gradle tasks the plugin invokes:
    - `installPlaywrightBrowsers` — one-time browser binary download.
    - `recordScenario` — launches Playwright codegen, used by `/record-scenario`.
- The partition layout under `src/test/kotlin/com/mattbobambrose/scenarios/` (scaffolded by the plugin on first run):
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

The first plugin command you run will prompt for the four config fields (`scenario_dir`, `test_dir`, `test_language`, `test_framework`). Accept the defaults — they match this layout.

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

Scenario markdown lives at `src/test/scenarios/{record,crawl,convert}/` (created by the plugin on first run). A `BasePageTest` class is auto-offered by the plugin during the first-run bootstrap — say Yes when prompted.
