package com.sab.carm.fcm.repository;

import com.sab.carm.fcm.entity.CreditApplicationConsent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CreditApplicationConsentRepository
        extends MongoRepository<CreditApplicationConsent, String> {

    Optional<CreditApplicationConsent>
    findByRelationshipIdAndSerialNo(
            String relationshipId,
            String serialNo);
}
