package com.patbaumgartner.habithooks.analyzer;

import com.patbaumgartner.habithooks.model.Violation;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Parses CycloneDX JSON SBOM reports. */
final class CyclonedxReportParser {

    private CyclonedxReportParser() {
    }

    static List<Violation> parse(Path reportPath, Path workingDir, String toolPrefix) throws IOException {
        String content = Files.readString(reportPath, StandardCharsets.UTF_8);
        List<Violation> violations = new ArrayList<>();
        addIfMissing(violations, new Context(reportPath, workingDir, toolPrefix), content);
        return List.copyOf(violations);
    }

    private static void addIfMissing(List<Violation> violations, Context context, String content) {
        if (!content.contains("\"bomFormat\"") || !content.contains("CycloneDX")) {
            violations.add(context.violation("InvalidBom", "SBOM does not look like a CycloneDX document."));
        }
        if (!content.contains("\"components\"")) {
            violations.add(context.violation("MissingComponents", "SBOM does not contain a components section."));
        }
    }

    private record Context(Path reportPath, Path workingDir, String toolPrefix) {

        Violation violation(String rule, String message) {
            return new Violation(toolPrefix + ":" + rule, ReportSupport.reportFilePath(reportPath, workingDir), 1,
                    message);
        }
    }

}
