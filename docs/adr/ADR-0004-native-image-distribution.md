# ADR-0004: GraalVM native image distribution profile

- Status: Accepted
- Date: 2026-06-19

## Context

habit-hooks is a CLI tool intended to run frequently during local and CI loops.
Startup latency has a direct impact on developer feedback cycle time.

## Decision

Keep Java fat JAR as the default artifact and provide an opt-in GraalVM native
image profile (`-Pnative`) for low-latency execution environments.

## Consequences

- Preserves broad compatibility through the JVM artifact.
- Enables fast startup for users willing to install GraalVM native-image.
- Adds native build and benchmark jobs to CI to catch regressions early.
