The configured compile goal exited non-zero and habit-hooks could not extract a specific Error Prone rule from compiler output. This usually means compilation failed normally, Error Prone is not wired correctly, or the output format changed.

**How to fix it:**

1. Run the configured compile goal directly.
2. Fix normal compiler errors before interpreting Error Prone findings.
3. If Error Prone should run, verify the Maven compiler plugin invokes it as a javac plugin.

Compiler feedback is the tightest quality loop. Keep it boring, fast, and visible.
