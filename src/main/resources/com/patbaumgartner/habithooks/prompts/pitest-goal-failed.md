The configured PIT Maven goal exited non-zero and habit-hooks could not extract mutation results. Because PIT runs the test suite in a specialized way, failures often expose brittle tests or build assumptions.

**How to fix it:**

1. Run the configured PIT Maven goal directly.
2. Fix test execution, classpath, or plugin configuration errors first.
3. Only then interpret surviving mutations as test-design feedback.

A broken mutation run is still valuable: it tells you the deeper test harness needs attention.
