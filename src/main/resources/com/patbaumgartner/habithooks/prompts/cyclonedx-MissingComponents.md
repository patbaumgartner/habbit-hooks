A CycloneDX SBOM without components does not describe the dependency graph. It may exist as a file, but it is not useful evidence for security review.

**How to fix it:**

1. Check whether the CycloneDX goal ran after dependencies were resolved.
2. Review plugin include/exclude settings that may have filtered everything out.
3. Regenerate the SBOM and verify it lists application dependencies.

The useful habit is to make release artifacts explain themselves.
