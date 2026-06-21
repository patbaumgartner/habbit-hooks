package com.patbaumgartner.habithooks.cli;

import com.patbaumgartner.habithooks.config.HabitHooksConfig;
import com.patbaumgartner.habithooks.model.AnalysisResult;

record AnalysisRun(HabitHooksConfig config, AnalysisResult result, RuleFilter ruleFilter, boolean skipped,
        String skipMessage) {

    static AnalysisRun skipped(HabitHooksConfig config, String message) {
        return new AnalysisRun(config, new AnalysisResult(java.util.List.of(), 0), new RuleFilter(config.getRules()),
                true, message);
    }

    boolean hasFailures() {
        return ruleFilter.hasFailures(result.violations());
    }

}
