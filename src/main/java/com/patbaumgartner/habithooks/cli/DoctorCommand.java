package com.patbaumgartner.habithooks.cli;

import com.patbaumgartner.habithooks.analyzer.Analyzer;
import com.patbaumgartner.habithooks.config.ConfigLoader;
import com.patbaumgartner.habithooks.config.HabitHooksConfig;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

/** Checks whether the configured Java quality harness can actually run. */
@Command(name = "doctor", mixinStandardHelpOptions = true,
        description = "Check local analyzer prerequisites without running full analysis.")
final class DoctorCommand implements Callable<Integer> {

    @ParentCommand
    private HabitHooksCommand parent;

    @Override
    public Integer call() {
        Path workingDir = parent.workingDir();
        HabitHooksConfig config = ConfigLoader.load(parent.configPath(), workingDir);
        List<Analyzer> analyzers = AnalyzerFactory.create(config);
        boolean ok = printMavenStatus(workingDir);
        for (Analyzer analyzer : analyzers) {
            boolean available = analyzer.isAvailable(workingDir);
            ok &= available;
            print(available, analyzer.toolPrefix(), available ? "ready" : "not available");
        }
        if (analyzers.isEmpty()) {
            print(false, "analyzers", "none enabled");
            return 1;
        }
        return ok ? 0 : 1;
    }

    private static boolean printMavenStatus(Path workingDir) {
        boolean present = Files.isRegularFile(workingDir.resolve("mvnw"));
        print(present, "maven", present ? "./mvnw found" : "./mvnw missing; falling back to mvn if installed");
        return true;
    }

    private static void print(boolean ok, String name, String message) {
        System.out.println((ok ? "OK   " : "WARN ") + name + " - " + message);
    }

}
