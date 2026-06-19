# ADR-0008: Resilient configuration defaults

- Status: Accepted
- Date: 2026-06-20

## Context

CLI tooling is often used in mixed environments (local shell, CI, agent runners)
where config values may be missing, explicitly null, or blank. Fragile config
handling creates avoidable failures and inconsistent behavior.

## Decision

Use a resilient config model with deterministic defaults:

- Null/blank values in `.habit-hooks.yaml` normalize to stable defaults.
- Relative paths passed to `--config` are resolved from the current working
  directory.
- Missing or malformed config falls back to defaults instead of failing closed.

## Consequences

- Better out-of-the-box behavior and fewer setup-related surprises.
- More predictable CLI behavior across local and CI usage.
- Strict validation shifts from config parsing to analyzer/tool execution stages.