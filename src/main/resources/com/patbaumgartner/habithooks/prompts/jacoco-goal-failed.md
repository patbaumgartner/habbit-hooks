The configured JaCoCo Maven goal exited non-zero and habit-hooks could not read parseable coverage evidence. That means the test-and-coverage loop is broken, not merely below target.

**How to fix it:**

1. Run the configured JaCoCo Maven goal directly.
2. Fix failing tests, JaCoCo agent setup, or report generation before adding coverage work.
3. Re-run habit-hooks and then decide whether uncovered behavior needs tests.

Coverage is useful only when the measurement pipeline is trustworthy.
