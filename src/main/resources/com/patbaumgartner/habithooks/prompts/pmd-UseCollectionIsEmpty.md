# pmd UseCollectionIsEmpty

Checking `size() == 0` to test emptiness is less readable and slightly less efficient than `isEmpty()`.
`isEmpty()` communicates intent directly and is implemented in O(1) for all standard collections.

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
