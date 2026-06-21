package com.patbaumgartner.habithooks.cli;

import com.patbaumgartner.habithooks.report.QualityReport;
import com.patbaumgartner.habithooks.report.QualityReportBuilder;
import com.patbaumgartner.habithooks.report.QualityReportWriter;
import com.patbaumgartner.habithooks.report.TrendStore;
import java.nio.file.Path;
import java.util.Locale;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

/** Generates local Sonar-style quality reports. */
@Command(name = "report", mixinStandardHelpOptions = true,
        description = "Write a local quality report for agents and humans.")
final class ReportCommand implements Callable<Integer> {

    @ParentCommand
    private HabitHooksCommand parent;

    @Option(names = { "--format" }, description = "markdown, json, html, or sarif", defaultValue = "markdown")
    private String format;

    @Option(names = { "--output" }, description = "Output directory", defaultValue = "target/habit-hooks")
    private Path outputDir;

    @Option(names = { "--no-fail" }, description = "Always exit 0 after writing the report")
    private boolean noFail;

    @Override
    public Integer call() throws Exception {
        AnalysisRun run = parent.analyzeConfigured(parent.workingDir());
        if (run.skipped()) {
            System.out.println(run.skipMessage());
            return 0;
        }
        String normalizedFormat = format.toLowerCase(Locale.ROOT);
        QualityReport report = new QualityReportBuilder().build(run.result(), run.hasFailures());
        new TrendStore().record(outputDir.resolve("history"), report);
        Path output = new QualityReportWriter().write(report, outputDir, normalizedFormat);
        System.out.println("Wrote " + output);
        return noFail || !run.hasFailures() ? 0 : 1;
    }

}
