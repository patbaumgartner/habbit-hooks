# pmd NcssCount

NCSS counts non-comment source statements. A class or method with too much NCSS is usually doing too much work, even if the lines are not visually long.

**How to fix it:**

1. Split large methods into smaller helpers with names that reveal intent.
2. Split large classes by responsibility, not by incidental type grouping.
3. Keep orchestration thin and push real work into focused collaborators.
4. If the class is mostly data, consider a record or a small value object instead.

NCSS is a useful smell because it ignores formatting and measures actual source density.
If the count is high, the unit probably deserves a cleaner boundary.
