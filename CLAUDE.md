# playwright-scenarios-kotlin-template

Kotlin/Gradle starter pre-wired for the [`playwright-scenarios`](https://github.com/mattbobambrose/playwright-scenarios) Claude Code plugin: Playwright-for-Java + Kotest test dependencies, the Gradle tasks the plugin calls, and a scenario/test folder layout with one reviewed example per folder. Spin up your own copy with GitHub's **Use this template**.

## Build & test

The `Makefile` wraps Gradle — `make help` lists every target. Common ones:

- `make build` — compile and run tests (`./gradlew build`)
- `make tests` — run the test suite (`./gradlew test`)
- `make install` — one-time setup: Playwright browser binaries + the global `playwright-cli` npm package
- `make record url=<start-url> out=<path>` — launch Playwright codegen

Tests are **Kotest StringSpec** on the JUnit Platform. JDK 17 (toolchain set from the version catalog).

## Dependency versions

All versions live in the `gradle/libs.versions.toml` version catalog — change them there, never inline in `build.gradle.kts`. `make versions` reports available updates.

## Layout

- `src/test/scenarios/{crawl,record,convert}/` — scenario markdown, one folder per authoring command (`/crawl-site`, `/record-scenario`, `/doc-to-scenarios`); one reviewed example ships in each.
- `src/test/kotlin/com/bookshelf/scenarios/{crawl,record,convert}/` — generated tests mirror the scenario folders; `/scenario-to-tests` writes `<scenario-name>/<ClassName>.kt` here.
- `src/test/docs/` — suggested home for input documents fed to `/doc-to-scenarios`; ships two examples, `checkout-user-story.md` and `checkout-test-spec.md`.
- `src/main/kotlin/Main.kt` — placeholder for your application code.

## Placeholder tests

Each test folder ships a no-op `*Test.kt` (`CrawlTest`, `RecordTest`, `ConvertTest`) so `./gradlew test` passes on a fresh clone. Delete them once you have generated tests. Do **not** rename them to `Base*Test`, `*TestBase`, `Abstract*Test`, or `*PageTest` — those patterns match the plugin's base-test-class discovery globs and would suppress the `/create-base-test` offer.

## The plugin

This repo is the test target, not the plugin itself. Install the plugin separately (`/plugin marketplace add mattbobambrose/playwright-scenarios`, then `/plugin install playwright-scenarios@playwright-scenarios`); the first command run bootstraps `.claude/playwright-scenarios.local.md`. For a full walkthrough see the plugin's [tutorial](https://mattbobambrose.github.io/playwright-scenarios/tutorial/).
