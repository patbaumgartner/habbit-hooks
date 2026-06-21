The configured Spring Java Format goal exited non-zero and habit-hooks could not extract formatter feedback from the captured output. Treat this as a formatting feedback-loop failure.

**How to fix it:**

1. Run the configured Spring Java Format goal directly.
2. Apply the formatter if the failure is ordinary style drift.
3. Fix plugin or Java version issues if the formatter itself cannot run.

A predictable formatter lets agents spend attention on design instead of whitespace.
