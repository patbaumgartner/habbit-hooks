Coverage gaps mean some behavior is present without tests that execute it. The missing lines are not automatically wrong, but changes there can slip through without a failing test.

**How to fix it:**

1. Read the uncovered branch and name the behavior it protects.
2. Add the smallest test that would fail if that behavior regressed.
3. Prefer testing through public behavior instead of asserting private implementation details.

Do not chase coverage by executing lines without assertions. The useful habit is to turn unprotected behavior into executable intent.
