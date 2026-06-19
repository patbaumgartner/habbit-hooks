# ADR-0002: Coaching on top of Checkstyle and PMD

- Status: Accepted
- Date: 2026-06-19

## Context

Developers already use Checkstyle and PMD. Raw violations are useful but often
insufficient for AI coding agents, which need concise, actionable guidance.

## Decision

Use Checkstyle and PMD as the rule engines and augment violations with
coaching prompts that explain smell, impact, and fix direction.

## Consequences

- Reuses mature ecosystem tooling and team rule investments
- Improves AI agent output quality without replacing existing analyzers
- Requires prompt maintenance as rule coverage evolves
