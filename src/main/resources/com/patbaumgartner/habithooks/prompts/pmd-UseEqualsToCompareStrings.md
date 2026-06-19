# pmd UseEqualsToCompareStrings

Using `==` to compare strings tests object identity, not content. Two strings with identical characters can be different objects and will compare unequal with `==`.

**How to fix it:**

```java
// Before — works by accident when strings are interned, breaks otherwise
if (status == "ACTIVE") { ... }

// After — compares content, always correct
if ("ACTIVE".equals(status)) { ... }
```

`==` is correct only when you explicitly want to compare object identity (rare).
For string content, always use `.equals()` — put the literal first to guard against `null`.
