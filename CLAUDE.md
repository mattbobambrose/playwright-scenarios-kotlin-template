# playwright-scenarios-kotlin-template

Kotlin/Gradle starter pre-wired for the [`playwright-scenarios`](https://github.com/mattbobambrose/playwright-scenarios) Claude Code plugin: Playwright-for-Java + Kotest test dependencies, the Gradle tasks the plugin calls, and two example input documents for `/doc-to-scenarios`. Spin up your own copy with GitHub's **Use this template**.

## Build & test

The `Makefile` wraps Gradle — `make help` lists every target. Common ones:

- `make build` — compile and run tests (`./gradlew build`)
- `make tests` — run the test suite (`./gradlew test`)
- `make install` — one-time setup: Playwright browser binaries + the global `playwright-cli` npm package
- `make record url=<start-url> out=<path>` — launch Playwright codegen

Tests are **Kotest StringSpec** on the JUnit Platform. JDK 17 (toolchain set from the version catalog). The template ships no tests of its own — the suite starts empty and fills as `/scenario-to-tests` generates them, so `make tests` passes (with zero tests) on a fresh clone.

## Dependency versions

All versions live in the `gradle/libs.versions.toml` version catalog — change them there, never inline in `build.gradle.kts`. `make versions` reports available updates.

## Layout

- `src/test/scenarios/{crawl,record,convert}/` — scenario markdown, one folder per authoring command (`/crawl-site`, `/record-scenario`, `/doc-to-scenarios`). Created by the plugin's `loading-config` skill on first run — not committed.
- `src/test/kotlin/com/bookshelf/scenarios/{crawl,record,convert}/` — generated tests mirror the scenario folders; `/scenario-to-tests` writes `<scenario-name>/<ClassName>.kt` here. Also created by `loading-config` on first run.
- `src/test/docs/` — suggested home for input documents fed to `/doc-to-scenarios`; ships two examples, `checkout-user-story.md` and `checkout-test-spec.md`.
- `src/main/kotlin/Main.kt` — placeholder for your application code.

## The plugin

This repo is the test target, not the plugin itself. Install the plugin separately (`/plugin marketplace add mattbobambrose/playwright-scenarios`, then `/plugin install playwright-scenarios@playwright-scenarios`); the first command run bootstraps `.claude/playwright-scenarios.local.md`. For a full walkthrough see the plugin's [tutorial](https://mattbobambrose.github.io/playwright-scenarios/tutorial/).
