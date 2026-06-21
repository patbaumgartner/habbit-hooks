package com.patbaumgartner.habithooks.cli;

import com.patbaumgartner.habithooks.tasks.AgentTaskExporter;
import java.nio.file.Path;
import java.util.Locale;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

/** Exports findings as small, agent-ready implementation tasks. */
@Command(name = "tasks", mixinStandardHelpOptions = true,
        description = "Export analyzer findings as agent task batches.")
final class TasksCommand implements Callable<Integer> {

    @ParentCommand
    private HabitHooksCommand parent;

    @Option(names = { "--format" }, description = "markdown or json", defaultValue = "markdown")
    private String format;

    @Option(names = { "--output" }, description = "Output directory", defaultValue = "target/habit-hooks")
    private Path outputDir;

    @Override
    public Integer call() throws Exception {
        AnalysisRun run = parent.analyzeConfigured(parent.workingDir());
        if (run.skipped()) {
            System.out.println(run.skipMessage());
            return 0;
        }
        String normalizedFormat = format.toLowerCase(Locale.ROOT);
        Path output = new AgentTaskExporter().write(run.result(), outputDir, normalizedFormat);
        System.out.println("Wrote " + output);
        return run.hasFailures() ? 1 : 0;
    }

}
