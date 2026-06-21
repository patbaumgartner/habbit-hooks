# pmd UseCollectionIsEmpty

Checking `size() == 0` to test emptiness is less direct than `isEmpty()` and can be less efficient for some collection implementations.
`isEmpty()` communicates intent directly and avoids depending on how a collection computes its size.

**How to fix it:**

```java
// Before
if (list.size() == 0) { ... }
if (map.size() > 0) { ... }

// After
if (list.isEmpty()) { ... }
if (!map.isEmpty()) { ... }
```

`isEmpty()` is the idiomatic Java way and is understood instantly by any reader.
