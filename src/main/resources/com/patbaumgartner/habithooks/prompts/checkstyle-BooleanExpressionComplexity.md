# checkstyle BooleanExpressionComplexity

A boolean expression with many `&&`, `||`, `!`, `&`, and `|` operators is hard to read correctly and harder to test thoroughly. Each extra term adds another condition or interaction a reviewer must reason about.

**How to fix it:**

1. Extract sub-expressions into named boolean variables or predicate methods.
2. Use a guard clause or early return to remove one branch from the expression.
3. Consider the Specification pattern for complex domain rules.

```java
// Before — requires careful reading to understand
if (user != null && user.isActive() && !user.isSuspended()
        && (user.hasRole("admin") || user.hasRole("moderator"))
        && lastLoginDays < 30) { ... }

// After — self-documenting
boolean isEligible = user != null
        && user.isActive()
        && !user.isSuspended();
boolean hasPrivilege = user.hasRole("admin") || user.hasRole("moderator");
boolean isRecent = lastLoginDays < 30;

if (isEligible && hasPrivilege && isRecent) { ... }
```

If the expression appears more than once, move it into a well-named predicate method.
