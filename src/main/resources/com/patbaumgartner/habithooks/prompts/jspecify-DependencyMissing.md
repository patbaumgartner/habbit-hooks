The JSpecify analyzer is enabled, but the project does not declare the JSpecify annotations dependency. Nullness habits need a shared annotation vocabulary before they can become consistent.

**How to fix it:**

1. Add `org.jspecify:jspecify` as a provided or compile-time dependency.
2. Start with package-level or module-level nullness defaults where appropriate.
3. Add focused annotations at API boundaries before annotating every local detail.

Nullness work is most useful when it documents contracts that callers rely on.
