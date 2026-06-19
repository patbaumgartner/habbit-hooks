# ADR-0005: Supply-chain integrity controls

- Status: Accepted
- Date: 2026-06-19

## Context

habit-hooks is distributed as executable artifacts and should be verifiable by
consumers in automated environments.

## Decision

Adopt layered supply-chain controls:

- CycloneDX SBOM generation during package builds.
- Keyless Sigstore/Cosign signatures for release artifacts.
- SLSA provenance attestations in release workflows.
- CodeQL, OWASP dependency checks, OpenSSF Scorecard, and Dependabot in CI.

## Consequences

- Improves provenance and tamper-evidence for consumers.
- Increases CI complexity and maintenance overhead for pinned actions.
- Requires periodic policy and workflow updates as ecosystem guidance evolves.
