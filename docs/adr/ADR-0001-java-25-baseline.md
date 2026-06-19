# ADR-0001: Java 25 baseline

- Status: Accepted
- Date: 2026-06-19

## Context

habit-hooks is a tooling-focused Java CLI with static-analysis integrations and
CI/CD supply-chain controls. The project benefits from modern language/runtime
features and from testing against the latest stable JDK.

## Decision

Set Java 25 as the minimum and only supported runtime/build target for this
project.

## Consequences

- Simpler compatibility/testing matrix and fewer branch conditions in code
- Access to modern Java performance and language improvements
- Users on older LTS JDKs must upgrade to run/build habit-hooks
