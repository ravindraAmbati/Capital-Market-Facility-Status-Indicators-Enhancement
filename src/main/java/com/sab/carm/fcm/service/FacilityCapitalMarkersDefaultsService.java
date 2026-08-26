package com.sab.carm.fcm.service;

import com.sab.carm.fcm.dto.integration.FacilityCapitalMarkersDefaultsResponse;
import com.sab.carm.fcm.dto.integration.FacilityTypeDefault;
import com.sab.carm.fcm.dto.integration.PurposeCodeDefault;
import com.sab.carm.fcm.entity.FacilityTypeMaintenance;
import com.sab.carm.fcm.entity.PurposeCodeMaintenance;
import com.sab.carm.fcm.repository.FacilityTypeMaintenanceRepository;
import com.sab.carm.fcm.repository.PurposeCodeMaintenanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacilityCapitalMarkersDefaultsService {

    private final FacilityTypeMaintenanceRepository facilityTypeRepository;
    private final PurposeCodeMaintenanceRepository purposeCodeRepository;

    public FacilityCapitalMarkersDefaultsService(
            FacilityTypeMaintenanceRepository facilityTypeRepository,
            PurposeCodeMaintenanceRepository purposeCodeRepository) {
        this.facilityTypeRepository = facilityTypeRepository;
        this.purposeCodeRepository = purposeCodeRepository;
    }

    public FacilityCapitalMarkersDefaultsResponse findAll() {

        List<FacilityTypeDefault> facilityTypes =
                facilityTypeRepository.findAll()
                        .stream()
                        .map(this::toFacilityTypeDefault)
                        .collect(Collectors.toList());

        List<PurposeCodeDefault> purposeCodes =
                purposeCodeRepository.findAll()
                        .stream()
                        .map(this::toPurposeCodeDefault)
                        .collect(Collectors.toList());

        FacilityCapitalMarkersDefaultsResponse response =
                new FacilityCapitalMarkersDefaultsResponse();

        response.setFacilityTypes(facilityTypes);
        response.setPurposeCodes(purposeCodes);

        return response;
    }

    private FacilityTypeDefault toFacilityTypeDefault(
            FacilityTypeMaintenance entity) {

        FacilityTypeDefault result = new FacilityTypeDefault();

        result.setFacilityTypeCode(entity.getFacilityTypeCode());
        result.setFacilityTypeDescription(
                entity.getFacilityTypeDescription());
        result.setAdvised(entity.getAdvised());
        result.setCommitted(entity.getCommitted());

        return result;
    }

    private PurposeCodeDefault toPurposeCodeDefault(
            PurposeCodeMaintenance entity) {

        PurposeCodeDefault result = new PurposeCodeDefault();

        result.setPurposeCodeCarm(entity.getPurposeCodeCarm());
        result.setPurposeCodeHub(entity.getPurposeCodeHub());
        result.setPurposeCodeDescription(
                entity.getDescription());
        result.setUnconditionalCancellable(
                entity.getUnconditionalCancellable());

        return result;
    }
}
