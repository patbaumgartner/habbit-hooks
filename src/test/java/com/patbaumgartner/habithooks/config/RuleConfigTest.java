package com.patbaumgartner.habithooks.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RuleConfigTest {

    @Test
    void defaultsAreNeutral() {
        RuleConfig config = new RuleConfig();
        assertThat(config.isDisabled()).isFalse();
        assertThat(config.getInclude()).isEmpty();
        assertThat(config.getExclude()).isEmpty();
        assertThat(config.getSeverity()).isNull();
    }

    @Test
    void settersRoundTrip() {
        RuleConfig config = new RuleConfig();
        config.setDisabled(true);
        config.setInclude(new String[] { "**/*.java" });
        config.setExclude(new String[] { "**/*Test.java" });
        config.setSeverity("error");

        assertThat(config.isDisabled()).isTrue();
        assertThat(config.getInclude()).containsExactly("**/*.java");
        assertThat(config.getExclude()).containsExactly("**/*Test.java");
        assertThat(config.getSeverity()).isEqualTo("error");
    }

    @Test
    void nullArraysAreNormalizedToEmpty() {
        RuleConfig config = new RuleConfig();
        config.setInclude(null);
        config.setExclude(null);
        assertThat(config.getInclude()).isEmpty();
        assertThat(config.getExclude()).isEmpty();
    }

    @Test
    void blankSeverityIsNormalizedToNull() {
        RuleConfig config = new RuleConfig();
        config.setSeverity("   ");
        assertThat(config.getSeverity()).isNull();
    }

    @Test
    void gettersReturnDefensiveCopies() {
        RuleConfig config = new RuleConfig();
        config.setInclude(new String[] { "a" });
        String[] firstRead = config.getInclude();
        firstRead[0] = "mutated";
        assertThat(config.getInclude()).containsExactly("a");
    }

}
