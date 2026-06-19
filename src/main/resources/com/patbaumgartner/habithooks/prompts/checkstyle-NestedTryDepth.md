# checkstyle NestedTryDepth

Deeply nested try blocks signal that a method is handling multiple levels of failure in one place. The structure becomes hard to reason about: it is unclear which catch handles which exception, and the happy path is buried inside layers of error handling.

**How to fix it:**

1. Extract inner try blocks into named helper methods.
2. Rethrow exceptions at the boundary and handle them in one place.
3. Use try-with-resources to eliminate manual close calls.

```java
// Before — two levels of try/catch tangled together
try {
    connect();
    try {
        execute(query);
    } catch (QueryException e) {
        rollback();
    }
} catch (ConnectionException e) {
    logFailure(e);
}

// After — responsibilities separated
try {
    connect();
    executeWithRollback(query);
} catch (ConnectionException e) {
    logFailure(e);
}

private void executeWithRollback(String query) {
    try {
        execute(query);
    } catch (QueryException e) {
        rollback();
    }
}
```

Each nesting level should have a single, clear purpose.
