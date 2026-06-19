# pmd LooseCoupling

Declaring fields and return types as concrete collection implementations (`ArrayList`, `HashMap`, `HashSet`) instead of interfaces (`List`, `Map`, `Set`) ties the API contract to an implementation detail.
Callers cannot substitute a different implementation, and the code is harder to test in isolation.

**How to fix it:**

```java
// Before — locked to ArrayList
private ArrayList<String> names = new ArrayList<>();
public ArrayList<String> getNames() { return names; }

// After — open to any List implementation
private List<String> names = new ArrayList<>();
public List<String> getNames() { return names; }
```

Use the narrowest interface that satisfies the contract.
`List`, `Map`, `Set`, `Collection`, `Iterable` are almost always the right choice.
Reserve the concrete type for the constructor call on the right-hand side.
