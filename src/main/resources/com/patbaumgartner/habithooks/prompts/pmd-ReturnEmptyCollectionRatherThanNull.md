# pmd ReturnEmptyCollectionRatherThanNull

Returning `null` for a missing collection forces every caller to null-check before iterating.
An empty collection is the correct representation of "nothing here" — it composes correctly with streams, for-each, and size checks with no special casing.

**How to fix it:**

```java
// Before — every caller must guard against null
List<Order> getOrders() {
    if (noData) return null;
    ...
}

// After — callers iterate safely with zero cognitive overhead
List<Order> getOrders() {
    if (noData) return List.of();
    ...
}
```

Use `List.of()`, `Set.of()`, `Map.of()`, or `Collections.empty*()` depending on the return type.
For `Optional` single values, return `Optional.empty()` instead.
