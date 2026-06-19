# pmd ArrayIsStoredDirectly

Storing an array argument directly gives callers a back-channel to mutate the object's internal state after construction. The class can no longer enforce its own invariants.

**How to fix it:**

Copy the array on the way in, and copy again on the way out.

```java
// Before — caller can mutate internals after construction
public Config(String[] keys) {
    this.keys = keys;      // caller still holds a reference
}

// After — defensive copy breaks the aliasing
public Config(String[] keys) {
    this.keys = keys.clone();
}
```

If the collection does not need to be an array, prefer an immutable `List`:

```java
public Config(String[] keys) {
    this.keys = List.copyOf(Arrays.asList(keys));
}
```

See also: `pmd:MethodReturnsInternalArray` — the same principle applies to getters.
