The configured OpenRewrite dry-run goal failed before habit-hooks could trust a fresh modernization signal.

**How to fix it:**

1. Run the configured OpenRewrite Maven goal directly and inspect the first error.
2. Fix plugin/profile wiring issues before changing application code.
3. Re-run habit-hooks and confirm OpenRewrite findings are reported from a fresh patch.

A failed modernization run is a tooling regression. Restore the signal, then act on findings.
