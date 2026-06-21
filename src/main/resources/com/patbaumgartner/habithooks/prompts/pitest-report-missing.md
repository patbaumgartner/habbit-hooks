PIT did not produce the configured mutation report. Without that report, habit-hooks cannot identify surviving or uncovered mutations.

**How to fix it:**

1. Run PIT directly and inspect the first failure.
2. Confirm the report path matches the PIT configuration.
3. Fix the test or plugin setup, then regenerate the mutation report.

Do not tune mutation thresholds until the report itself is reliable.
