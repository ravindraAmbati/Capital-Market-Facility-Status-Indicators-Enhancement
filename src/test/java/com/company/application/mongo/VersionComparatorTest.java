package com.company.application.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class VersionComparatorTest {

    @Test
    void comparesSemanticVersions() {
        VersionComparator comparator = new VersionComparator();

        assertThat(comparator.compare("1.2.0", "1.1.9")).isGreaterThan(0);
        assertThat(comparator.compare("1.0", "1.0.0")).isZero();
        assertThat(comparator.compare("1.0.0", "1.0.1")).isLessThan(0);
    }
}
