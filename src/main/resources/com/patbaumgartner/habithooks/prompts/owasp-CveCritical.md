# Critical CVE

A critical CVE is a release blocker. Treat it as a dependency hygiene task first, not a code-style task.

Fix by upgrading the direct dependency when possible, checking whether the vulnerable component is transitive, and documenting any temporary suppression with the CVE, affected path, and expiry date.
