package com.patbaumgartner.habithooks.analyzer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patbaumgartner.habithooks.model.Violation;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

final class OwaspDependencyCheckReportParser {

    private static final int DESCRIPTION_LIMIT = 180;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private OwaspDependencyCheckReportParser() {
    }

    static List<Violation> parse(Path reportPath, String toolPrefix) throws IOException {
        JsonNode dependencies = MAPPER.readTree(reportPath.toFile()).path("dependencies");
        List<Violation> violations = new ArrayList<>();
        if (dependencies.isArray()) {
            dependencies.forEach(dependency -> addDependency(toolPrefix, dependency, violations));
        }
        return List.copyOf(violations);
    }

    private static void addDependency(String toolPrefix, JsonNode dependency, List<Violation> violations) {
        String file = dependency.path("filePath").asText(dependency.path("fileName").asText("dependency-check"));
        dependency.path("vulnerabilities")
            .forEach(vulnerability -> violations.add(toViolation(toolPrefix, file, vulnerability)));
    }

    private static Violation toViolation(String toolPrefix, String file, JsonNode vulnerability) {
        String name = vulnerability.path("name").asText("vulnerability");
        String severity = vulnerability.path("severity").asText("UNKNOWN").toUpperCase();
        String score = cvssScore(vulnerability);
        String message = name + " " + severity + " CVSS " + score + ": " + description(vulnerability);
        return new Violation(toolPrefix + ":" + ruleName(severity, vulnerability), file, -1, message);
    }

    private static String ruleName(String severity, JsonNode vulnerability) {
        if (vulnerability.path("isSuppressed").asBoolean(false) || vulnerability.path("suppressed").asBoolean(false)) {
            return "SuppressedVulnerability";
        }
        return switch (severity) {
            case "CRITICAL" -> "CveCritical";
            case "HIGH" -> "CveHigh";
            case "MEDIUM" -> "CveMedium";
            default -> "CveLow";
        };
    }

    private static String cvssScore(JsonNode vulnerability) {
        JsonNode cvssv3 = vulnerability.path("cvssv3").path("baseScore");
        JsonNode cvssv2 = vulnerability.path("cvssv2").path("score");
        return cvssv3.isMissingNode() ? cvssv2.asText("unknown") : cvssv3.asText("unknown");
    }

    private static String description(JsonNode vulnerability) {
        String description = vulnerability.path("description").asText("No description provided.");
        return description.length() > DESCRIPTION_LIMIT ? description.substring(0, DESCRIPTION_LIMIT) + "..."
                : description;
    }

}
