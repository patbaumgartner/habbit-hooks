# ADR-0003: Rebase-only repository policy

- Status: Accepted
- Date: 2026-06-19

## Context

The repository enforces strict quality gates and automated review signals.
Linear history improves traceability and bisectability for release/security
investigations.

## Decision

Enforce rebase-only merges. Disable merge commits and squash merges. Delete
branches on merge.

## Consequences

- Linear and easier-to-audit history
- Cleaner provenance from commit to release artifact
- Contributors must rebase regularly to resolve conflicts
