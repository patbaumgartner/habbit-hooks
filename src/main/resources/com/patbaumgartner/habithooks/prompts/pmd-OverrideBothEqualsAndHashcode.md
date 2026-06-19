# pmd OverrideBothEqualsAndHashcode

The `equals` and `hashCode` contract requires that objects which are equal must produce the same hash code.
Overriding one without the other breaks this contract: objects that compare equal will be stored separately in hash-based collections (`HashMap`, `HashSet`), and lookups will silently fail.

**How to fix it:**

Always override both, or neither.

```java
// Broken — equal objects may have different hash codes
@Override
public boolean equals(Object o) { ... }
// hashCode missing → HashMap, HashSet will behave incorrectly

// Fixed — contract satisfied
@Override
public boolean equals(Object o) { ... }

@Override
public int hashCode() {
    return Objects.hash(id, name);
}
```

For value objects, prefer **records** — Java generates a correct `equals` and `hashCode` from all components automatically.
