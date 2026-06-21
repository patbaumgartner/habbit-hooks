JaCoCo did not produce the configured coverage report. habit-hooks cannot include coverage evidence without that file.

**How to fix it:**

1. Confirm tests actually ran.
2. Check JaCoCo plugin bindings and the configured report path.
3. Regenerate the report and re-run habit-hooks.

A missing coverage report is a broken feedback loop, not a neutral result.
