A surviving mutation means tests executed, but they did not prove the behavior strongly enough to catch a meaningful code change.

**How to fix it:**

1. Read the mutation description and identify the behavior it changed.
2. Add or strengthen a test that would fail for the mutated behavior.
3. Avoid asserting implementation details unless that is the actual public contract.

Mutation testing is a craft signal: it asks whether tests protect meaning, not just lines.
