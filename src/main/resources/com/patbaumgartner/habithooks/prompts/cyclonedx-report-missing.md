The CycloneDX goal failed without producing the configured SBOM file. Without that file, habit-hooks cannot verify release dependency evidence.

**How to fix it:**

1. Run the CycloneDX Maven goal directly and inspect the first failure.
2. Confirm the configured report path matches the plugin output.
3. Fix the build or config, then regenerate the SBOM.

Do not treat a missing SBOM as harmless just because the application compiles.
