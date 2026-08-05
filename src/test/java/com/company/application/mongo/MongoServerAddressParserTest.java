package com.company.application.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.company.application.exception.MongoInfrastructureException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class MongoServerAddressParserTest {

    @Test
    void parsesConfiguredAddresses() {
        assertThat(new MongoServerAddressParser().parse(Arrays.asList("host1:27017", "host2:27018"))).hasSize(2);
    }

    @Test
    void rejectsInvalidAddress() {
        assertThatThrownBy(() -> new MongoServerAddressParser().parse(Arrays.asList("bad")))
                .isInstanceOf(MongoInfrastructureException.class);
    }
}
