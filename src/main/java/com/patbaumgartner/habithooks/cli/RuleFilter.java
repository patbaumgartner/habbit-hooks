package com.patbaumgartner.habithooks.cli;

import com.patbaumgartner.habithooks.config.RuleConfig;
import com.patbaumgartner.habithooks.model.Violation;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.Map;

/**
 * Applies per-rule {@link RuleConfig} overrides to a set of violations.
 *
 * <p>
 * Four knobs are honored:
 * <ul>
 * <li>{@code disabled} &mdash; drops all violations for the rule.</li>
 * <li>{@code include} &mdash; keeps a violation only when its file matches one of the
 * glob patterns (when any are configured).</li>
 * <li>{@code exclude} &mdash; drops a violation when its file matches one of the glob
 * patterns.</li>
 * <li>{@code severity} &mdash; {@code "warning"} makes the rule informational (it is
 * still reported but does not fail the run); anything else fails the run.</li>
 * </ul>
 */
final class RuleFilter {

    private static final String WARNING = "warning";

    private final Map<String, RuleConfig> rules;

    RuleFilter(Map<String, RuleConfig> rules) {
        this.rules = Map.copyOf(rules);
    }

    /**
     * Removes violations that are disabled or fall outside their rule's include/exclude
     * file scope.
     * @param violations the violations to filter
     * @return the reportable violations
     */
    List<Violation> apply(List<Violation> violations) {
        return violations.stream().filter(this::isReportable).toList();
    }

    /**
     * Determines whether any violation should fail the run.
     * @param violations the (already filtered) violations
     * @return {@code true} when at least one violation belongs to a non-warning rule
     */
    boolean hasFailures(List<Violation> violations) {
        return violations.stream().anyMatch(this::isFailure);
    }

    private boolean isReportable(Violation violation) {
        RuleConfig cfg = rules.get(violation.ruleId());
        if (cfg == null) {
            return true;
        }
        if (cfg.isDisabled()) {
            return false;
        }
        String file = violation.file();
        return matchesIncludes(cfg, file) && !matchesAny(cfg.getExclude(), file);
    }

    private boolean isFailure(Violation violation) {
        RuleConfig cfg = rules.get(violation.ruleId());
        return cfg == null || !WARNING.equalsIgnoreCase(cfg.getSeverity());
    }

    private static boolean matchesIncludes(RuleConfig cfg, String file) {
        String[] includes = cfg.getInclude();
        return includes.length == 0 || matchesAny(includes, file);
    }

    private static boolean matchesAny(String[] patterns, String file) {
        Path path = Path.of(file);
        for (String pattern : patterns) {
            PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
            if (matcher.matches(path)) {
                return true;
            }
        }
        return false;
    }

}
