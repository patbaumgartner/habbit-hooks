package com.patbaumgartner.habithooks.report;

import java.util.List;
import java.util.Map;

/**
 * Local quality report that can be rendered as JSON, Markdown, HTML, or SARIF.
 */
public record QualityReport(String generatedAt, int filesChecked, boolean clean, boolean failing, int totalFindings,
        Map<String, Long> byTool, Map<String, Long> byRule, Map<String, Long> byDimension,
        List<ReportFinding> findings) {

    /** Returns a defensive-copy report instance. */
    public QualityReport {
        byTool = Map.copyOf(byTool);
        byRule = Map.copyOf(byRule);
        byDimension = Map.copyOf(byDimension);
        findings = List.copyOf(findings);
    }

}
