package com.patbaumgartner.habithooks.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.patbaumgartner.habithooks.model.AnalysisResult;
import com.patbaumgartner.habithooks.report.ReportFinding;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/** Exports analyzer findings as agent-sized work items. */
public final class AgentTaskExporter {

    private static final int LOCATION_LIMIT = 10;

    private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    /** Writes agent tasks and returns the generated path. */
    public Path write(AnalysisResult result, Path outputDir, String format) throws IOException {
        Files.createDirectories(outputDir);
        List<AgentTask> tasks = tasks(result);
        Path output = outputDir.resolve("tasks." + format);
        if ("json".equals(format)) {
            MAPPER.writeValue(output.toFile(), tasks);
        }
        else {
            Files.writeString(output, markdown(tasks), StandardCharsets.UTF_8);
        }
        return output;
    }

    /** Builds agent-sized tasks grouped by normalized rule ID. */
    public List<AgentTask> tasks(AnalysisResult result) {
        AtomicInteger sequence = new AtomicInteger(1);
        return result.violations()
            .stream()
            .map(ReportFinding::from)
            .collect(Collectors.groupingBy(ReportFinding::ruleId))
            .entrySet()
            .stream()
            .sorted(MapEntryComparator.INSTANCE)
            .map(entry -> toTask(sequence.getAndIncrement(), entry.getKey(), entry.getValue()))
            .toList();
    }

    private static AgentTask toTask(int sequence, String ruleId, List<ReportFinding> findings) {
        ReportFinding first = findings.getFirst();
        return new AgentTask("HH-" + String.format("%03d", sequence), title(first), ruleId, first.dimension(),
                first.severity(), findings.size(), locations(findings));
    }

    private static String title(ReportFinding finding) {
        return switch (finding.dimension()) {
            case "supply-chain" -> "Resolve " + finding.ruleId();
            case "test-signal" -> "Improve test signal for " + finding.ruleId();
            case "architecture" -> "Restore architecture rule " + finding.ruleId();
            default -> "Fix " + finding.ruleId();
        };
    }

    private static List<String> locations(List<ReportFinding> findings) {
        return findings.stream().map(AgentTaskExporter::location).distinct().limit(LOCATION_LIMIT).toList();
    }

    private static String markdown(List<AgentTask> tasks) {
        StringBuilder output = new StringBuilder("# habit-hooks agent tasks\n\n");
        tasks.forEach(task -> appendTask(output, task));
        return output.toString();
    }

    private static void appendTask(StringBuilder output, AgentTask task) {
        output.append("## ").append(task.id()).append(' ').append(task.title()).append("\n\n");
        output.append("- Rule: `").append(task.ruleId()).append("`\n");
        output.append("- Dimension: ").append(task.dimension()).append("\n");
        output.append("- Severity: ").append(task.severity()).append("\n");
        output.append("- Findings: ").append(task.count()).append("\n");
        task.locations().forEach(location -> output.append("- Location: ").append(location).append('\n'));
        output.append('\n');
    }

    private static String location(ReportFinding finding) {
        return finding.line() > 0 ? finding.file() + ":" + finding.line() : finding.file();
    }

    private enum MapEntryComparator implements Comparator<java.util.Map.Entry<String, List<ReportFinding>>> {

        INSTANCE;

        @Override
        public int compare(java.util.Map.Entry<String, List<ReportFinding>> left,
                java.util.Map.Entry<String, List<ReportFinding>> right) {
            return left.getKey().compareTo(right.getKey());
        }

    }

}
