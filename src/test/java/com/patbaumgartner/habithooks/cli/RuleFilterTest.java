package com.patbaumgartner.habithooks.cli;

import com.patbaumgartner.habithooks.config.RuleConfig;
import com.patbaumgartner.habithooks.model.Violation;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RuleFilterTest {

    private static final Violation MAIN = new Violation("checkstyle:MethodLength",
            "src/main/java/com/example/Service.java", 10, "Too long");

    private static final Violation TEST = new Violation("checkstyle:MethodLength",
            "src/test/java/com/example/ServiceTest.java", 20, "Too long");

    @Test
    void keepsAllViolationsWhenNoRulesConfigured() {
        RuleFilter filter = new RuleFilter(Map.of());
        assertThat(filter.apply(List.of(MAIN, TEST))).containsExactly(MAIN, TEST);
    }

    @Test
    void disabledRuleDropsAllItsViolations() {
        RuleConfig cfg = new RuleConfig();
        cfg.setDisabled(true);
        RuleFilter filter = new RuleFilter(Map.of("checkstyle:MethodLength", cfg));
        assertThat(filter.apply(List.of(MAIN, TEST))).isEmpty();
    }

    @Test
    void excludeGlobDropsMatchingFiles() {
        RuleConfig cfg = new RuleConfig();
        cfg.setExclude(new String[] { "**/*Test.java" });
        RuleFilter filter = new RuleFilter(Map.of("checkstyle:MethodLength", cfg));
        assertThat(filter.apply(List.of(MAIN, TEST))).containsExactly(MAIN);
    }

    @Test
    void includeGlobKeepsOnlyMatchingFiles() {
        RuleConfig cfg = new RuleConfig();
        cfg.setInclude(new String[] { "src/main/**" });
        RuleFilter filter = new RuleFilter(Map.of("checkstyle:MethodLength", cfg));
        assertThat(filter.apply(List.of(MAIN, TEST))).containsExactly(MAIN);
    }

    @Test
    void emptyIncludeMeansAllFilesAreInScope() {
        RuleConfig cfg = new RuleConfig();
        RuleFilter filter = new RuleFilter(Map.of("checkstyle:MethodLength", cfg));
        assertThat(filter.apply(List.of(MAIN, TEST))).containsExactly(MAIN, TEST);
    }

    @Test
    void warningSeverityDoesNotFailTheRun() {
        RuleConfig cfg = new RuleConfig();
        cfg.setSeverity("warning");
        RuleFilter filter = new RuleFilter(Map.of("checkstyle:MethodLength", cfg));

        assertThat(filter.apply(List.of(MAIN))).containsExactly(MAIN);
        assertThat(filter.hasFailures(List.of(MAIN))).isFalse();
    }

    @Test
    void errorSeverityFailsTheRun() {
        RuleConfig cfg = new RuleConfig();
        cfg.setSeverity("error");
        RuleFilter filter = new RuleFilter(Map.of("checkstyle:MethodLength", cfg));
        assertThat(filter.hasFailures(List.of(MAIN))).isTrue();
    }

    @Test
    void unconfiguredRuleFailsTheRunByDefault() {
        RuleFilter filter = new RuleFilter(Map.of());
        assertThat(filter.hasFailures(List.of(MAIN))).isTrue();
    }

    @Test
    void noViolationsNeverFailsTheRun() {
        RuleFilter filter = new RuleFilter(Map.of());
        assertThat(filter.hasFailures(List.of())).isFalse();
    }

    @Test
    void mixedWarningAndErrorRulesFailTheRun() {
        RuleConfig warning = new RuleConfig();
        warning.setSeverity("warning");
        RuleFilter filter = new RuleFilter(Map.of("checkstyle:MethodLength", warning));
        Violation other = new Violation("pmd:GodClass", "src/main/java/com/example/Big.java", 1, "God class");

        assertThat(filter.hasFailures(List.of(MAIN, other))).isTrue();
    }

}
