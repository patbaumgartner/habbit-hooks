# pmd EmptyCatchBlock

An empty catch block silently swallows exceptions. The failure disappears; nothing is logged, nothing is rethrown, nothing is recovered. This is one of the hardest bugs to diagnose in production because the only evidence is the absence of output.

**How to fix it:**

1. If the exception is genuinely expected and safe to ignore, log it at DEBUG level and add a comment explaining why.
2. If recovery is possible, perform it.
3. If the exception signals a programming error, rethrow as an unchecked exception.

```java
// Before — silent failure
try {
    resource.close();
} catch (IOException e) {
}

// After — at minimum, log it
try {
    resource.close();
} catch (IOException e) {
    LOGGER.debug("Failed to close resource: {}", e.getMessage());
}
```

Use try-with-resources for `Closeable` types to remove the need for explicit close calls entirely.
