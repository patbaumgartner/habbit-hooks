# pmd AvoidReassigningParameters

Reassigning a method parameter reuses a name for two different things — the original argument and a derived value — making the reader trace back through the method to understand the current state of the variable.

**How to fix it:**

Introduce a new local variable with a descriptive name for the derived value.

```java
// Before — 'value' means two different things
void process(String value) {
    value = value.trim();
    value = value.toUpperCase();
    store(value);
}

// After — intent is explicit
void process(String rawValue) {
    String normalized = rawValue.trim().toUpperCase();
    store(normalized);
}
```

The parameter name documents what the caller passed in.
A local variable name documents what you computed from it.
Keeping them separate eliminates a common source of confusion during code review.
