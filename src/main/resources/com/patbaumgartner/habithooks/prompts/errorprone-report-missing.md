The Error Prone analyzer did not capture compiler output. Without that output, habit-hooks cannot tell whether the compile failure was an Error Prone finding or a setup problem.

**How to fix it:**

1. Re-run the configured compile goal directly.
2. Confirm habit-hooks can write to the configured report path.
3. Fix the build setup, then run habit-hooks again.

The agent needs compiler evidence, not just a red build.
