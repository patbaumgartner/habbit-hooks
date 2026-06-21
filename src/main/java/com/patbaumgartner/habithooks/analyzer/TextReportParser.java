package com.patbaumgartner.habithooks.analyzer;

import com.patbaumgartner.habithooks.model.Violation;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Parses captured text output from Maven-backed tools. */
final class TextReportParser {

    private TextReportParser() {
    }

    static List<Violation> parseSpringJavaFormat(Path reportPath, Path workingDir, String toolPrefix)
            throws IOException {
        String content = Files.readString(reportPath, StandardCharsets.UTF_8).strip();
        if (content.isBlank()) {
            return List.of();
        }
        return List.of(new Violation(toolPrefix + ":Formatting", ReportSupport.reportFilePath(reportPath, workingDir),
                1, firstLine(content)));
    }

    static List<Violation> parseErrorProne(Path reportPath, Path workingDir, String toolPrefix) throws IOException {
        List<String> lines = Files.readAllLines(reportPath, StandardCharsets.UTF_8);
        List<Violation> violations = new ArrayList<>();
        Context context = new Context(reportPath, workingDir, toolPrefix);
        for (int index = 0; index < lines.size(); index++) {
            addErrorProneViolation(violations, context, new ReportLine(index, lines.get(index)));
        }
        return List.copyOf(violations);
    }

    private static void addErrorProneViolation(List<Violation> violations, Context context, ReportLine line) {
        errorProneRule(line.text()).ifPresent(rule -> violations.add(context.violation(rule, line)));
    }

    private static Optional<String> errorProneRule(String line) {
        int separator = line.indexOf("] [");
        int start = separator < 0 ? -1 : line.indexOf("[", separator + 1);
        int end = start < 0 ? -1 : line.indexOf("]", start);
        if (start < 0 || end < 0 || !line.contains("[ERROR]")) {
            return Optional.empty();
        }
        return Optional.of(line.substring(start + 1, end));
    }

    private static String firstLine(String content) {
        return content.lines().findFirst().orElse(content);
    }

    private record ReportLine(int index, String text) {

        int number() {
            return index + 1;
        }
    }

    private record Context(Path reportPath, Path workingDir, String toolPrefix) {

        Violation violation(String rule, ReportLine line) {
            return new Violation(toolPrefix + ":" + rule, ReportSupport.reportFilePath(reportPath, workingDir),
                    line.number(), line.text().strip());
        }
    }

}
