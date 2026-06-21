The configured SpotBugs Maven goal exited non-zero and habit-hooks could not extract a specific finding. Treat this as a tooling signal first, then a code signal.

**How to fix it:**

1. Run the SpotBugs Maven goal directly and read the first concrete error.
2. If the failure is configuration or classpath related, fix the build setup before changing production code.
3. If SpotBugs reports a bug pattern, address the underlying correctness risk rather than suppressing the warning.

A static-analysis tool that cannot run is part of the quality surface. Restore the signal before continuing feature work.
