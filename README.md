# playwright-scenarios-kotlin-template

A Kotlin repo that demonstrates the use of the [`playwright-scenarios`](https://github.com/mattbobambrose/playwright-scenarios) Claude Code plugin.

## What's included

- **Playwright for Java** + **Kotest** test dependencies in `build.gradle.kts`.
- Two Gradle tasks the plugin invokes:
    - `installPlaywrightBrowsers` — one-time browser binary download.
    - `recordScenario` — launches Playwright codegen, used by `/record-scenario`.
- Two example input documents under `src/test/docs/` — sample `/doc-to-scenarios` inputs written to the plugin's `TEST_DOC_GUIDE.md` conventions:
    - `checkout-user-story.md` — the checkout flow framed as a user story with acceptance criteria
    - `checkout-test-spec.md` — the checkout page covered exhaustively, element by element
- JUnit 5 platform wired up so Kotest StringSpec runs.

## Getting started

1. **Use this template.** Click the green **Use this template → Create a new repository** button at the top of the GitHub page to spin up your own copy. Then clone the new repo locally. (Plain `git clone` of *this* repo also works if you don't want a fresh repo of your own.)
2. **Follow the [tutorial](https://mattbobambrose.github.io/playwright-scenarios/tutorial/)** — a walkthrough that exercises crawl, record, and doc-driven authoring, ending with an executable test suite.

## Requirements

- JDK 17 or later (the toolchain is set in `build.gradle.kts`).
- `playwright-cli` for the live-site exploration commands (`/crawl-site`, `/review-scenario`, `/scenario-to-tests`)

## Project structure

```
.
├── build.gradle.kts                    # Playwright + Kotest deps and the two Gradle tasks
├── settings.gradle.kts
├── gradle.properties
├── gradlew / gradlew.bat
└── src/
    ├── main/kotlin/Main.kt             # placeholder for your application code
    └── test/
        └── docs/                       # example input documents for /doc-to-scenarios
            ├── checkout-user-story.md
            └── checkout-test-spec.md
```

The plugin keys scenarios and tests by authoring command — `crawl/` (`/crawl-site`), `record/` (`/record-scenario`), and `convert/` (`/doc-to-scenarios`). Scenario markdown lands under `src/test/scenarios/<command>/` and generated tests under `src/test/kotlin/com/bookshelf/scenarios/<command>/`. Those folders aren't committed — the plugin's `loading-config` skill creates them on the first command run — so the template starts with an empty test suite that fills as you author scenarios and generate tests.

`src/test/docs/` is a suggested home for the input documents you feed to `/doc-to-scenarios`; it ships with two examples, `checkout-user-story.md` and `checkout-test-spec.md`. The plugin doesn't enforce this location — pass any path you like — but having a stable directory keeps your repo tidy.
