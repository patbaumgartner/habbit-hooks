package com.patbaumgartner.habithooks.cli;

import java.io.IOException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VersionProviderTest {

    @Test
    void returnsVersionStringWithToolName() throws IOException {
        String[] version = new VersionProvider().getVersion();
        assertThat(version).hasSize(1);
        assertThat(version[0]).startsWith("habit-hooks ");
    }

}
