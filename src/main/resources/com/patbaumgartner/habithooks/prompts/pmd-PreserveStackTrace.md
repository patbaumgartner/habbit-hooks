# pmd PreserveStackTrace

Catching an exception and throwing a new one without wrapping the original discards the stack trace.
The root cause disappears from logs, and debugging requires guesswork instead of evidence.

**How to fix it:**

Pass the original exception as the cause when constructing the new one.

```java
// Before — root cause lost
try {
    parse(input);
} catch (ParseException e) {
    throw new ServiceException("Failed to parse input");
}

// After — root cause preserved
try {
    parse(input);
} catch (ParseException e) {
    throw new ServiceException("Failed to parse input", e);
}
```

Every exception constructor that accepts a `Throwable cause` parameter should receive the original exception.
If you are re-throwing the same exception type, simply `throw e` or use `throw new RuntimeException(e)` to wrap checked exceptions.
