# pmd LiteralsFirstInComparisons

Comparing a string literal against a variable risks a `NullPointerException` when the variable is `null`.
Reversing the order — literal first — makes the comparison null-safe without a null check.

**How to fix it:**

```java
// Before — throws NPE when name is null
if (name.equals("admin")) { ... }

// After — null-safe
if ("admin".equals(name)) { ... }
```

This is a defensive reflex, not a style preference.
`Objects.equals(name, "admin")` is equally acceptable when both sides can be null.
