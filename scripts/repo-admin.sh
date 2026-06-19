#!/usr/bin/env bash
#
# repo-admin.sh — Enforce repository merge policy and optionally merge open PRs.
#
# Usage:
#   ./scripts/repo-admin.sh [--repo OWNER/REPO] [--merge-open-prs] [--dry-run]
#
# Defaults to the current repository when --repo is omitted.

set -euo pipefail

REPO=""
MERGE_OPEN_PRS=false
DRY_RUN=false

usage() {
    cat <<'EOF'
Usage: repo-admin.sh [--repo OWNER/REPO] [--merge-open-prs] [--dry-run]

Options:
  --repo OWNER/REPO     GitHub repository to manage (defaults to the current repo)
  --merge-open-prs      Merge open non-draft PRs with rebase merges
  --dry-run             Print the actions without changing anything
  -h, --help            Show this help text
EOF
}

log() {
    printf '[INFO]  %s\n' "$*"
}

warn() {
    printf '[WARN]  %s\n' "$*" >&2
}

error() {
    printf '[ERROR] %s\n' "$*" >&2
}

run() {
    if [[ "$DRY_RUN" == true ]]; then
        printf '[DRY]  '
        printf '%q ' "$@"
        printf '\n'
        return 0
    fi
    "$@"
}

while [[ $# -gt 0 ]]; do
    case "$1" in
        --repo)
            REPO="${2:-}"
            shift 2
            ;;
        --merge-open-prs)
            MERGE_OPEN_PRS=true
            shift
            ;;
        --dry-run)
            DRY_RUN=true
            shift
            ;;
        -h|--help)
            usage
            exit 0
            ;;
        *)
            error "Unknown argument: $1"
            usage
            exit 1
            ;;
    esac
done

if [[ -z "$REPO" ]]; then
    REPO="$(gh repo view --json nameWithOwner -q .nameWithOwner)"
fi

log "Using repository: $REPO"

log "Enforcing rebase-only merge policy"
run gh repo edit "$REPO" \
    --enable-rebase-merge \
    --enable-merge-commit=false \
    --enable-squash-merge=false \
    --delete-branch-on-merge=true

log "Protecting main branch (require CI, no force-push)"
if [[ "$DRY_RUN" == true ]]; then
    printf '[DRY]  gh api repos/%s/branches/main/protection --method PUT ...\n' "$REPO"
else
    gh api "repos/$REPO/branches/main/protection" \
        --method PUT \
        -H "Accept: application/vnd.github+json" \
        --field "required_status_checks[strict]=true" \
        --field "required_status_checks[checks][][context]=Build & Test (Java 25)" \
        --field "required_status_checks[checks][][app_id]=-1" \
        --field "enforce_admins=false" \
        --field "required_pull_request_reviews=null" \
        --field "restrictions=null" \
        --field "allow_force_pushes=false" \
        --field "allow_deletions=false" > /dev/null
fi

if [[ "$MERGE_OPEN_PRS" == true ]]; then
    log "Merging open non-draft PRs with rebase merges"
    while IFS=$'\t' read -r number title draft mergeable; do
        [[ -n "$number" ]] || continue
        if [[ "$draft" == true ]]; then
            warn "Skipping draft PR #$number: $title"
            continue
        fi
        if [[ "$mergeable" != MERGEABLE ]]; then
            warn "Skipping non-mergeable PR #$number ($mergeable): $title"
            continue
        fi
        log "Merging PR #$number: $title"
        run gh pr merge "$number" --repo "$REPO" --rebase --delete-branch
    done < <(gh pr list --state open --limit 100 --repo "$REPO" --json number,title,isDraft,mergeable \
        --jq '.[] | [.number, .title, .isDraft, .mergeable] | @tsv')
fi

log "Done"