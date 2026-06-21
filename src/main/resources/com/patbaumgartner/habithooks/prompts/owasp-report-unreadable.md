# OWASP Report Unreadable

habit-hooks found the Dependency Check report but could not parse it. That usually means a partial file, unexpected format, or plugin failure output was written instead of JSON.

Regenerate the report, confirm it is valid JSON, and keep the configured `reportFile` pointed at the Dependency Check JSON artifact.
