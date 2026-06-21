PIT produced a mutation report, but habit-hooks could not parse it. The mutation signal is there, but the agent cannot yet read it.

**How to fix it:**

1. Confirm the configured file is PIT XML, not HTML or console output.
2. Regenerate the report if it is partial.
3. Update the parser and add a fixture test if the PIT format changed.

Preserve the signal in a machine-readable form so the next coding loop can act on it.
