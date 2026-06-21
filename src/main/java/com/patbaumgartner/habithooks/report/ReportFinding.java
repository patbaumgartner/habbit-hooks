package com.patbaumgartner.habithooks.report;

import com.patbaumgartner.habithooks.model.Violation;
import java.util.List;

/** A report-friendly view of a normalized analyzer violation. */
public record ReportFinding(String ruleId, String tool, String dimension, String severity, String file, int line,
        String message) {

    private static final List<String> SUPPLY_CHAIN_TOOLS = List.of("owasp", "cyclonedx");

    private static final List<String> TEST_SIGNAL_TOOLS = List.of("jacoco", "pitest");

    private static final List<String> CORRECTNESS_TOOLS = List.of("spotbugs", "errorprone");

    /** Creates a report finding from a raw violation. */
    public static ReportFinding from(Violation violation) {
        String tool = toolOf(violation.ruleId());
        return new ReportFinding(violation.ruleId(), tool, dimensionOf(tool, violation.ruleId()),
                severityOf(violation.ruleId()), violation.file(), violation.line(), violation.message());
    }

    private static String toolOf(String ruleId) {
        int separator = ruleId.indexOf(':');
        return separator < 0 ? ruleId : ruleId.substring(0, separator);
    }

    private static String dimensionOf(String tool, String ruleId) {
        if (SUPPLY_CHAIN_TOOLS.contains(tool)) {
            return "supply-chain";
        }
        if (TEST_SIGNAL_TOOLS.contains(tool)) {
            return "test-signal";
        }
        if ("taikai".equals(tool)) {
            return "architecture";
        }
        if ("spring-javaformat".equals(tool)) {
            return "formatting";
        }
        if (CORRECTNESS_TOOLS.contains(tool) || ruleId.contains("Null")) {
            return "correctness";
        }
        return "maintainability";
    }

    private static String severityOf(String ruleId) {
        if (ruleId.contains("Critical")) {
            return "critical";
        }
        if (ruleId.contains("High") || ruleId.endsWith("goal-failed")) {
            return "high";
        }
        if (ruleId.contains("Medium") || ruleId.endsWith("report-missing")) {
            return "medium";
        }
        return "low";
    }

}
