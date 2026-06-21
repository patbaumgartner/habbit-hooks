A mutation with no coverage points to behavior that no test executed at all. This is the clearest possible sign that the code can change silently.

**How to fix it:**

1. Find the public path that should exercise the mutated code.
2. Add a focused test for the missing behavior.
3. Re-run PIT to confirm the mutation is either killed or no longer relevant.

Start with the behavior that matters most to users; mutation testing is a guide, not a scoreboard.
