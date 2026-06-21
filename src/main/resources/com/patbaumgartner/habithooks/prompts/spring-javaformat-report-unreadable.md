Spring Java Format output was captured, but habit-hooks could not read it. The formatter signal reached disk but did not become useful feedback.

**How to fix it:**

1. Check the configured report file permissions and encoding.
2. Delete stale captured output and rerun the formatter goal.
3. Update the text parser and add a fixture test if the output shape changed.

The harness is strongest when even formatter failures become clear instructions.
