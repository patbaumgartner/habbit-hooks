JSpecify is on the classpath, but main sources do not appear to use the annotations yet. The dependency exists, but the nullness habit has not started.

**How to fix it:**

1. Choose a small package or public API boundary to annotate first.
2. Add `@NullMarked` where non-null should be the default.
3. Use `@Nullable` only where absence is part of the contract.

Do not annotate mechanically. Let each annotation clarify an interface the next agent will depend on.
