The configured CycloneDX Maven goal exited non-zero and habit-hooks could not extract a concrete SBOM finding. Treat this as a release-readiness feedback failure.

**How to fix it:**

1. Run the configured CycloneDX Maven goal directly and inspect the first Maven error.
2. Fix dependency resolution, lifecycle, or plugin configuration problems first.
3. Regenerate the SBOM and re-run habit-hooks.

Supply-chain checks should be boring and repeatable. When they fail, restore the evidence trail before shipping.
