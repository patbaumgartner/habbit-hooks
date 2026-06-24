OpenRewrite finished but no patch artifact was found at the configured report path.

**How to fix it:**

1. Verify the configured OpenRewrite goal and report path match your plugin setup.
2. Ensure the recipe set is active and produces dry-run output.
3. Re-run habit-hooks after confirming the patch file is generated.

Without the patch artifact, agents cannot convert modernization opportunities into actionable tasks.
