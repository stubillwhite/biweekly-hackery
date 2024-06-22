# Constants
# ==============================================================================

COLOR_BLUE=\033[0;34m
COLOR_NONE=\033[0m

SHELL=/usr/local/bin/zsh
.SHELLFLAGS=-i

PYTHON=python3
PIP=pip

PROJECT_NAME=newsfeed

export POETRY_VIRTUALENVS_IN_PROJECT=true

# Targets
# ==============================================================================

# Help
# ======================================

help:
	@grep -E '^[0-9a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) \
		| sort \
		| awk 'BEGIN {FS = ":.*?## "}; {printf "$(COLOR_BLUE)%s|$(COLOR_NONE)%s\n", $$1, $$2}' \
		| column -t -s '|'

# Clean
# ======================================

.PHONY: stop
stop: ## Stop any running processes
	@echo 'Stopping processes'
	@poetry run dmypy stop || true

.PHONY: clean
clean: stop ## Remove all artefacts
	@echo 'Cleaning application'

.PHONY: clean-all
clean-all: clean ## Remove all artefacts and dependencies
	@echo 'Cleaning dependencies'
	@rm -rf dist/ htmlcov/
	@rm -f .make-sentinel.deps
	@poetry env remove --all

# Dependencies
# ======================================

.make-sentinel.deps: pyproject.toml
	@echo 'Fetching dependencies'
	@poetry install
	@touch .make-sentinel.deps

deps: .make-sentinel.deps ## Install dependencies

# Running
# ======================================

run: deps ## Run the app
	@echo 'Running application'
	@poetry run run-app
	@echo

test: deps ## Run the tests
	@echo 'Running tests'
	@poetry run pytest --cov
	@poetry run coverage html
	@echo

check: deps ## Check the code
	@echo 'Running linter'
	@poetry run ruff check
	@echo
	@echo 'Running static type checks'
	@poetry run dmypy run -- src tests
	@echo

fix: format deps ## Fix formatting and minor style issues
	@echo 'Fixing problems'
	@poetry run ruff check --fix
	@echo

format: deps ## Format the code
	@echo 'Formatting code'
	@poetry run black src tests
	@echo

build: format test check ## Build the code
	@echo 'Building artefacts'
	@poetry build
