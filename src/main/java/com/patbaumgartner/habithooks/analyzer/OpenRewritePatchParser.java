package com.patbaumgartner.habithooks.analyzer;

import com.patbaumgartner.habithooks.model.Violation;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Parses OpenRewrite dry-run patch files produced by rewrite:dryRunNoFork. */
final class OpenRewritePatchParser {

    private static final Pattern TARGET_LINE = Pattern.compile("^@@\\s+-\\d+(?:,\\d+)?\\s+\\+(\\d+)(?:,\\d+)?\\s+@@.*");

    private OpenRewritePatchParser() {
    }

    static List<Violation> parse(Path reportPath, String toolPrefix) throws IOException {
        List<String> lines = java.nio.file.Files.readAllLines(reportPath, StandardCharsets.UTF_8);
        Map<String, Integer> changedFiles = new LinkedHashMap<>();
        String currentFile = null;
        for (String rawLine : lines) {
            currentFile = applyPatchLine(changedFiles, currentFile, rawLine.strip());
        }
        return toViolations(changedFiles, toolPrefix);
    }

    private static String applyPatchLine(Map<String, Integer> changedFiles, String currentFile, String line) {
        String nextFile = parseFileHeader(line);
        if (nextFile != null) {
            changedFiles.putIfAbsent(nextFile, 1);
            return nextFile;
        }
        if (currentFile == null || !line.startsWith("@@")) {
            return currentFile;
        }
        Matcher matcher = TARGET_LINE.matcher(line);
        if (matcher.matches()) {
            changedFiles.put(currentFile, ReportSupport.parseInt(matcher.group(1)));
        }
        return currentFile;
    }

    private static String parseFileHeader(String line) {
        if (!line.startsWith("+++ b/")) {
            return null;
        }
        return line.substring("+++ b/".length());
    }

    private static List<Violation> toViolations(Map<String, Integer> changedFiles, String toolPrefix) {
        List<Violation> violations = new ArrayList<>();
        for (Map.Entry<String, Integer> file : changedFiles.entrySet()) {
            violations.add(new Violation(toolPrefix + ":SuggestedRefactor", file.getKey(), file.getValue(),
                    "OpenRewrite proposed automated refactoring changes for this file. Review and apply them."));
        }
        return List.copyOf(violations);
    }

}
