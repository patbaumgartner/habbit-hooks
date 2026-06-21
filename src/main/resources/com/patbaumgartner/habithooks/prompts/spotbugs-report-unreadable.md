SpotBugs produced a report, but habit-hooks could not parse it. The quality signal exists, but the harness cannot translate it for the agent yet.

**How to fix it:**

1. Open the reported XML file and confirm it is valid SpotBugs XML.
2. If the file is truncated, regenerate it with the SpotBugs Maven goal.
3. If the format changed, update the analyzer parser and add a fixture test for the new shape.

The goal is not just a passing build; it is preserving readable feedback for the next coding loop.
