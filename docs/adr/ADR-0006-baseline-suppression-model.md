# ADR-0006: Baseline suppression model

- Status: Accepted
- Date: 2026-06-19

## Context

Legacy repositories may have pre-existing quality violations that cannot be
fixed in a single pass. Teams still need to prevent new debt and avoid broad,
static suppressions.

## Decision

Use a committed JSON baseline (`.habit-hooks-baseline.json`) keyed by file path,
rule IDs, and file last-commit hash. A baseline entry only suppresses findings
when the file commit hash matches and the file is not dirty.

## Consequences

- Supports incremental adoption on brownfield codebases.
- Prevents stale suppressions from masking modified files.
- Keeps suppressions explicit and reviewable in version control.
