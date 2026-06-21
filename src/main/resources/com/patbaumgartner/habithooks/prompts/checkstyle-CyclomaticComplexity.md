High cyclomatic complexity means the method has many independent execution paths.
Each extra branch adds another scenario to test and another path for a reader to trace.
In this project, complexity above 8 is treated as a maintainability risk.

**How to fix it:**

1. **Guard clauses** — invert conditions and return early to eliminate nesting.
2. **Extract methods** — pull complex conditions into well-named boolean helpers.
3. **Polymorphism** — replace `if/else` or `switch` chains on type with virtual dispatch.
4. **Strategy pattern** — move conditionally-selected algorithms into their own classes.

```java
// Before — complexity 5
String classify(int score) {
    if (score >= 90) {
        return "A";
    } else if (score >= 80) {
        return "B";
    } else if (score >= 70) {
        return "C";
    } else if (score >= 60) {
        return "D";
    } else {
        return "F";
    }
}

// After — use a lookup or extract to a class
```

A complexity score of 1 means one linear path. Every `if`, `for`, `while`, `catch`,
`&&`, `||`, and `?:` adds 1. Target ≤ 8. Aim for ≤ 4 in most cases.
