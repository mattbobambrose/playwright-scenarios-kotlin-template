# Convenience targets for common operations.
# Run `make help` to see all available targets.

.DEFAULT_GOAL := help

.PHONY: help install install-browsers install-cli test build clean record _require-record-args versions upgrade-wrapper _require-gradle-version

GRADLE_VERSION := $(shell sed -n 's/^gradle = "\(.*\)"/\1/p' gradle/libs.versions.toml)

help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-20s\033[0m %s\n", $$1, $$2}'

install: install-browsers install-cli ## Install all prerequisites (Playwright browsers + playwright-cli)

install-browsers: ## Download Playwright browser binaries (one-time, ~200 MB)
	./gradlew installPlaywrightBrowsers

install-cli: ## Install the playwright-cli npm package globally
	npm install -g @playwright/cli@latest

record: _require-record-args ## Record a browser flow with Playwright codegen (url=<start-url> out=<path>)
	./gradlew recordScenario -Purl=$(url) -Pout=$(out)

clean: ## Remove build artifacts
	./gradlew clean

build: ## Compile sources and run tests (full build)
	./gradlew build

test: ## Run the test suite
	./gradlew test

versions: ## Check for dependency updates
	./gradlew dependencyUpdates

# Gradle's documented upgrade procedure: the first run rewrites
# gradle-wrapper.properties using the *old* wrapper jar; the second run
# regenerates the wrapper itself with the new version.
upgrade-wrapper: _require-gradle-version ## Re-run the Gradle wrapper task at the pinned version
	./gradlew wrapper --gradle-version=$(GRADLE_VERSION) --distribution-type=bin
	./gradlew wrapper --gradle-version=$(GRADLE_VERSION) --distribution-type=bin

_require-record-args:
	@[ -n "$(url)" ] && [ -n "$(out)" ] || { echo "ERROR: Provide url=<start-url> and out=<output-path>, e.g. make record url=https://example.com out=src/test/java/Recorded.java" >&2; exit 1; }

_require-gradle-version:
	@[ -n "$(GRADLE_VERSION)" ] || { echo "ERROR: Could not determine gradle version from gradle/libs.versions.toml" >&2; exit 1; }
	