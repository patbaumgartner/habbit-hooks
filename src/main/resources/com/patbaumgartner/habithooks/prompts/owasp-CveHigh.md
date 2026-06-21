# High CVE

A high CVE means the build is carrying known vulnerable code. Prefer the smallest safe dependency update that removes the vulnerable artifact.

Check dependency paths, update the owning dependency or BOM, rerun tests, and avoid broad exclusions unless the vulnerable code is genuinely unreachable and documented.
