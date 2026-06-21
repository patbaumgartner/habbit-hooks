package com.patbaumgartner.habithooks.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

/**
 * Configuration for a single static-analysis analyzer (Checkstyle or PMD).
 */
public class AnalyzerConfig {

    /** Whether this analyzer is active. Defaults to {@code true}. */
    @JsonProperty("enabled")
    private boolean enabled = true;

    /**
     * Path to the Checkstyle config file (Checkstyle only). Defaults to
     * {@code "checkstyle.xml"}.
     */
    @JsonProperty("configFile")
    private String configFile = "checkstyle.xml";

    /**
     * PMD rulesets to apply (PMD only). Defaults to a single {@code "pmd-ruleset.xml"}
     * entry.
     */
    @JsonProperty("rulesets")
    private String[] rulesets = { "pmd-ruleset.xml" };

    /** Returns whether this analyzer is enabled. */
    public boolean isEnabled() {
        return enabled;
    }

    /** Sets whether this analyzer is enabled. */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /** Returns the Checkstyle config file path. */
    public String getConfigFile() {
        if (configFile == null || configFile.isBlank()) {
            return "checkstyle.xml";
        }
        return configFile;
    }

    /** Sets the Checkstyle config file path. */
    public void setConfigFile(String configFile) {
        this.configFile = configFile == null || configFile.isBlank() ? "checkstyle.xml" : configFile;
    }

    /** Returns the PMD ruleset paths. */
    public String[] getRulesets() {
        if (rulesets == null || rulesets.length == 0) {
            return new String[] { "pmd-ruleset.xml" };
        }
        return rulesets.clone();
    }

    /** Sets the PMD ruleset paths. */
    public void setRulesets(String[] rulesets) {
        if (rulesets == null || rulesets.length == 0) {
            this.rulesets = new String[] { "pmd-ruleset.xml" };
            return;
        }
        String[] filtered = Arrays.stream(rulesets).filter(r -> r != null && !r.isBlank()).toArray(String[]::new);
        this.rulesets = filtered.length == 0 ? new String[] { "pmd-ruleset.xml" } : filtered;
    }

    /**
     * Simple name of the JUnit test class to invoke for Taikai (Taikai analyzer only).
     * Defaults to {@code "ArchitectureTest"}.
     */
    @JsonProperty("testClass")
    private String testClass = "ArchitectureTest";

    /** Maven goal or phase for Maven-backed analyzers. */
    @JsonProperty("goal")
    private String goal;

    /** Report path for Maven-backed analyzers, relative to the project root. */
    @JsonProperty("reportFile")
    private String reportFile;

    /** Returns the Taikai test class simple name. */
    public String getTestClass() {
        if (testClass == null || testClass.isBlank()) {
            return "ArchitectureTest";
        }
        return testClass;
    }

    /** Sets the Taikai test class simple name. */
    public void setTestClass(String testClass) {
        this.testClass = testClass == null || testClass.isBlank() ? "ArchitectureTest" : testClass;
    }

    /** Returns the Maven goal override, or {@code null} when unset. */
    public String getGoal() {
        if (goal == null || goal.isBlank()) {
            return null;
        }
        return goal;
    }

    /** Sets the Maven goal override. */
    public void setGoal(String goal) {
        this.goal = goal == null || goal.isBlank() ? null : goal;
    }

    /** Returns the report file override, or {@code null} when unset. */
    public String getReportFile() {
        if (reportFile == null || reportFile.isBlank()) {
            return null;
        }
        return reportFile;
    }

    /** Sets the report file override. */
    public void setReportFile(String reportFile) {
        this.reportFile = reportFile == null || reportFile.isBlank() ? null : reportFile;
    }

}
