# Convenience targets for common operations.
# Run `make help` to see all available targets.

.DEFAULT_GOAL := help

.PHONY: help install install-browsers install-cli test build clean

help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-20s\033[0m %s\n", $$1, $$2}'

install: install-browsers install-cli ## Install all prerequisites (Playwright browsers + playwright-cli)

install-browsers: ## Download Playwright browser binaries (one-time, ~200 MB)
	./gradlew installPlaywrightBrowsers

install-cli: ## Install the playwright-cli npm package globally
	npm install -g @playwright/cli@latest

test: ## Run the test suite
	./gradlew test

build: ## Compile sources and run tests (full build)
	./gradlew build

clean: ## Remove build artifacts
	./gradlew clean
