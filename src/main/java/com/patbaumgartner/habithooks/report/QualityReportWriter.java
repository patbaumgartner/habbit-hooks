package com.patbaumgartner.habithooks.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/** Writes local quality reports in agent- and tool-friendly formats. */
public final class QualityReportWriter {

    private static final int MARKDOWN_FINDING_LIMIT = 50;

    private static final int HTML_FINDING_LIMIT = 100;

    private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    /** Writes the report and returns the generated artifact path. */
    public Path write(QualityReport report, Path outputDir, String format) throws IOException {
        Files.createDirectories(outputDir);
        Path output = outputDir.resolve("report." + extension(format));
        switch (format) {
            case "json" -> MAPPER.writeValue(output.toFile(), report);
            case "html" -> Files.writeString(output, html(report), StandardCharsets.UTF_8);
            case "sarif" -> MAPPER.writeValue(output.toFile(), sarif(report));
            default -> Files.writeString(output, markdown(report), StandardCharsets.UTF_8);
        }
        return output;
    }

    private static String extension(String format) {
        return "sarif".equals(format) ? "sarif" : format;
    }

    private static String markdown(QualityReport report) {
        StringBuilder output = new StringBuilder("# habit-hooks local quality report\n\n");
        appendSummary(output, report);
        appendMap(output, "By dimension", report.byDimension());
        appendMap(output, "By tool", report.byTool());
        appendFindings(output, report.findings());
        return output.toString();
    }

    private static void appendSummary(StringBuilder output, QualityReport report) {
        output.append("- Generated: ").append(report.generatedAt()).append('\n');
        output.append("- Files checked: ").append(report.filesChecked()).append('\n');
        output.append("- Findings: ").append(report.totalFindings()).append('\n');
        output.append("- Gate: ").append(report.failing() ? "failing" : "passing").append("\n\n");
    }

    private static void appendMap(StringBuilder output, String heading, Map<String, Long> counts) {
        output.append("## ").append(heading).append("\n\n");
        if (counts.isEmpty()) {
            output.append("No findings.\n\n");
            return;
        }
        counts.forEach((key, value) -> output.append("- ").append(key).append(": ").append(value).append('\n'));
        output.append('\n');
    }

    private static void appendFindings(StringBuilder output, List<ReportFinding> findings) {
        output.append("## Agent task feed\n\n");
        findings.stream().limit(MARKDOWN_FINDING_LIMIT).forEach(finding -> appendFinding(output, finding));
        if (findings.size() > MARKDOWN_FINDING_LIMIT) {
            output.append("\n_Only the first 50 findings are shown._\n");
        }
    }

    private static void appendFinding(StringBuilder output, ReportFinding finding) {
        output.append("- [")
            .append(finding.severity())
            .append("] ")
            .append(finding.ruleId())
            .append(" at ")
            .append(location(finding))
            .append(" — ")
            .append(finding.message())
            .append('\n');
    }

    private static String html(QualityReport report) {
        return "<!doctype html><html><head><meta charset=\"utf-8\"><title>habit-hooks report</title>"
                + "<style>body{font-family:system-ui;margin:2rem;max-width:72rem}li{margin:.35rem 0}"
                + "code{background:#eee;padding:.1rem .25rem}</style></head><body>" + htmlBody(report)
                + "</body></html>";
    }

    private static String htmlBody(QualityReport report) {
        StringBuilder body = new StringBuilder("<h1>habit-hooks local quality report</h1>");
        body.append("<p>Findings: ")
            .append(report.totalFindings())
            .append(". Gate: ")
            .append(report.failing() ? "failing" : "passing")
            .append(".</p><ul>");
        report.findings()
            .stream()
            .limit(HTML_FINDING_LIMIT)
            .forEach(finding -> body.append("<li><code>")
                .append(escape(finding.ruleId()))
                .append("</code> ")
                .append(escape(location(finding)))
                .append(" - ")
                .append(escape(finding.message()))
                .append("</li>"));
        return body.append("</ul>").toString();
    }

    private static Map<String, Object> sarif(QualityReport report) {
        List<Map<String, Object>> results = report.findings().stream().map(QualityReportWriter::sarifResult).toList();
        Map<String, Object> driver = Map.of("name", "habit-hooks", "informationUri",
                "https://github.com/patbaumgartner/habbit-hooks");
        return Map.of("version", "2.1.0", "$schema", "https://json.schemastore.org/sarif-2.1.0.json", "runs",
                List.of(Map.of("tool", Map.of("driver", driver), "results", results)));
    }

    private static Map<String, Object> sarifResult(ReportFinding finding) {
        Map<String, Object> region = Map.of("startLine", Math.max(1, finding.line()));
        Map<String, Object> location = Map.of("physicalLocation",
                Map.of("artifactLocation", Map.of("uri", finding.file()), "region", region));
        return Map.of("ruleId", finding.ruleId(), "level", sarifLevel(finding.severity()), "message",
                Map.of("text", finding.message()), "locations", List.of(location));
    }

    private static String sarifLevel(String severity) {
        return switch (severity) {
            case "critical", "high" -> "error";
            case "medium" -> "warning";
            default -> "note";
        };
    }

    private static String location(ReportFinding finding) {
        return finding.line() > 0 ? finding.file() + ":" + finding.line() : finding.file();
    }

    private static String escape(String value) {
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

}
