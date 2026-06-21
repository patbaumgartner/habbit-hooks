package com.patbaumgartner.habithooks.analyzer;

/** Maven-backed analyzer variant that captures command output as its report. */
public class CapturingMavenGoalAnalyzer extends MavenGoalAnalyzer {

    /**
     * Creates an output-capturing Maven analyzer.
     * @param toolPrefix prefix for normalized rule IDs
     * @param goal Maven goal or phase to execute
     * @param reportFile file that receives captured output
     * @param reportParser parser for the captured output
     */
    public CapturingMavenGoalAnalyzer(String toolPrefix, String goal, String reportFile, ReportParser reportParser) {
        super(toolPrefix, goal, reportFile, reportParser);
    }

    @Override
    boolean capturesOutput() {
        return true;
    }

}
