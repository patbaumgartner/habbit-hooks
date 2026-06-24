Maven failed before the OpenRewrite goal started, so any previous patch output is stale.

**How to fix it:**

1. Resolve the first lifecycle blocker (formatting, compile, test, or dependency issue).
2. Re-run habit-hooks so OpenRewrite executes in the current build state.
3. Review only newly generated patch findings.

Fix upstream lifecycle blockers first; do not trust stale refactoring artifacts.
