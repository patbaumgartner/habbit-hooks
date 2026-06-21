# OWASP Report Missing

The Dependency Check goal failed and no JSON report was available for habit-hooks to parse.

Ensure the Maven goal runs with JSON output and writes `target/dependency-check-report.json`. Then rerun habit-hooks with the `owasp` analyzer enabled.
