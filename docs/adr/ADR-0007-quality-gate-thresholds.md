# ADR-0007: Quality gate thresholds and enforcement

- Status: Accepted
- Date: 2026-06-19

## Context

As a quality-focused tool, habit-hooks must maintain a stronger internal bar
than a minimal compile-and-test signal.

## Decision

Enforce these baseline quality gates in the standard build lifecycle:

- JaCoCo line coverage minimum of 70%.
- Spring Java Format (`spring-javaformat-maven-plugin`) formatting check at `validate` phase.
- Checkstyle, PMD/CPD, and SpotBugs as mandatory checks.
- Integration tests via Maven Failsafe for end-to-end CLI behavior.
- Optional PIT mutation testing profile for deeper test-suite effectiveness.

## Consequences

- Raises confidence in refactors and rule-coaching changes.
- Increases initial contributor friction when tests/formatting lag.
- Keeps verification cost manageable by leaving mutation testing opt-in.
