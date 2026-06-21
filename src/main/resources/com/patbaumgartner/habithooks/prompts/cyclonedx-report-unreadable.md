CycloneDX produced an SBOM file, but habit-hooks could not parse it. The supply-chain signal exists, but the harness cannot trust or explain it yet.

**How to fix it:**

1. Open the configured SBOM file and confirm it is valid CycloneDX JSON.
2. Regenerate the SBOM if it is partial, stale, or truncated.
3. Update the parser and fixture tests if the CycloneDX output shape changed.

Keep dependency evidence machine-readable so agents can reason about release risk.
