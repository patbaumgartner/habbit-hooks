package com.patbaumgartner.habithooks.analyzer;

import com.patbaumgartner.habithooks.model.Violation;
import java.nio.file.Path;
import java.util.List;

/**
 * Contract for a static-analysis tool wrapper.
 *
 * <p>
 * Each implementation wraps one tool (Checkstyle, PMD/CPD, SpotBugs, …) and returns the
 * violations it finds as a normalized list of {@link Violation} records.
 */
public sealed interface Analyzer
        permits CheckstyleAnalyzer, JSpecifyAnalyzer, MavenGoalAnalyzer, PmdAnalyzer, TaikaiAnalyzer {

    /**
     * Returns the tool prefix used in rule IDs (e.g. {@code "checkstyle"}).
     * @return the tool prefix, never {@code null}
     */
    String toolPrefix();

    /**
     * Returns whether this analyzer needs a non-empty Java file scope to produce useful
     * results.
     *
     * <p>
     * File-oriented analyzers such as Checkstyle and PMD use the changed-file scope.
     * Project-oriented analyzers such as coverage, mutation testing, and SBOM checks run
     * against the whole build and should still execute when no Java files changed.
     * @return {@code true} when an empty file list should skip this analyzer
     */
    default boolean requiresFiles() {
        return true;
    }

    /**
     * Runs the tool against the provided set of Java source files.
     * @param files absolute paths to the files to analyze
     * @param workingDir the project root (used to resolve config file paths)
     * @return the list of violations found; empty when the analysis is clean
     */
    List<Violation> analyze(List<Path> files, Path workingDir);

    /**
     * Returns {@code true} when this analyzer is able to run in the current environment
     * (e.g. config file is present and valid).
     * @param workingDir the project root
     * @return {@code true} if available, {@code false} if the analyzer should be skipped
     */
    boolean isAvailable(Path workingDir);

}
