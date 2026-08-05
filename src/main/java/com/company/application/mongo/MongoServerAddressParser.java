package com.company.application.mongo;

import com.company.application.exception.MongoInfrastructureException;
import com.mongodb.ServerAddress;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Parses configured MongoDB server addresses.
 */
@Component
public class MongoServerAddressParser {

    public List<ServerAddress> parse(List<String> configuredAddresses) {
        List<ServerAddress> addresses = new ArrayList<>();
        for (String configuredAddress : configuredAddresses) {
            String[] parts = configuredAddress.split(":");
            if (parts.length != 2) {
                throw new MongoInfrastructureException("Invalid MongoDB server address: " + configuredAddress);
            }
            addresses.add(new ServerAddress(parts[0], Integer.parseInt(parts[1])));
        }
        return addresses;
    }
}
