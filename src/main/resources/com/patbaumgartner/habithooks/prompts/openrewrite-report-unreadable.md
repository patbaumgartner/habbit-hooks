OpenRewrite produced a patch artifact, but habit-hooks could not read it.

**How to fix it:**

1. Check file encoding and path permissions for the configured patch file.
2. Regenerate the patch with the OpenRewrite Maven goal.
3. Re-run habit-hooks and confirm findings are parsed.

The patch must be readable for agent workflows to consume modernization hints automatically.
