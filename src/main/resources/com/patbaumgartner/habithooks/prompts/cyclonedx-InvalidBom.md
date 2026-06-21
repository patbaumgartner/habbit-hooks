The configured SBOM file does not look like a CycloneDX document. habit-hooks expects a machine-readable file with CycloneDX metadata so release and security tooling can inspect what the artifact contains.

**How to fix it:**

1. Regenerate the CycloneDX report with the configured Maven goal.
2. Confirm the output includes CycloneDX metadata such as `bomFormat`.
3. Fix plugin configuration before changing release automation.

A release without a trustworthy SBOM asks future maintainers to guess what was shipped.
