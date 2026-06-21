Error Prone compiler output was captured, but habit-hooks could not read or normalize it. The build failure is visible, but the harness cannot turn the evidence into rule-level feedback yet.

**How to fix it:**

1. Check the configured report file permissions and encoding.
2. Re-run the configured compile goal and inspect the captured output.
3. Update the parser and add a fixture test if Error Prone output changed.

Compiler diagnostics should land as structured feedback, not just noise in a log.
