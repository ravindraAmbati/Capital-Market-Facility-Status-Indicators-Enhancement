package com.sab.carm.fcm.carm;

import com.sab.carm.fcm.carm.dto.CarmMaintenanceReferenceData;
import com.sab.carm.fcm.carm.dto.CarmReferenceDataResponse;
import com.sab.carm.fcm.carm.dto.FacilityTypeMaintenanceData;
import com.sab.carm.fcm.carm.dto.PurposeCodeMaintenanceData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CarmMaintenanceReferenceDataService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    CarmMaintenanceReferenceDataService.class);

    private static final String FACILITY_TYPE_TABLE = "1200";
    private static final String PURPOSE_CODE_TABLE = "1060";
    private static final String ACTIVE = "N";

    private final CarmReferenceDataService referenceDataService;
    private final CarmProperties properties;

    public CarmMaintenanceReferenceDataService(
            CarmReferenceDataService referenceDataService,
            CarmProperties properties) {

        this.referenceDataService = referenceDataService;
        this.properties = properties;
    }

    public CarmMaintenanceReferenceData refresh() {

        validateDefaultIndicators();

        Map<String, List<Map<String, Object>>> referenceData =
                referenceDataService.fetchConfiguredReferenceData();

        CarmMaintenanceReferenceData result =
                new CarmMaintenanceReferenceData();

        result.setFacilityTypes(
                buildFacilityTypes(
                        referenceData.get(FACILITY_TYPE_TABLE)));

        result.setPurposeCodes(
                buildPurposeCodes(
                        referenceData.get(PURPOSE_CODE_TABLE)));

        logResult(result);

        return result;
    }

    private List<FacilityTypeMaintenanceData> buildFacilityTypes(
            List<Map<String, Object>> rows) {

        if (rows == null) {
            return Collections.emptyList();
        }

        List<FacilityTypeMaintenanceData> result =
                new ArrayList<>();

        for (Map<String, Object> row : rows) {

            if (!isActive(row, "FGRPARCIND")) {
                continue;
            }

            FacilityTypeMaintenanceData data =
                    new FacilityTypeMaintenanceData();

            data.setFacilityTypeCode(
                    value(row, "FGRPFACGRP"));

            data.setFacilityTypeDescription(
                    value(row, "FGRPFGDESC"));

            data.setAdvised(
                    properties.getReferenceData()
                            .getDefaults()
                            .getFacilityType()
                            .getAdvised());

            data.setCommitted(
                    properties.getReferenceData()
                            .getDefaults()
                            .getFacilityType()
                            .getCommitted());

            result.add(data);
        }

        return result;
    }

    private List<PurposeCodeMaintenanceData> buildPurposeCodes(
            List<Map<String, Object>> rows) {

        if (rows == null) {
            return Collections.emptyList();
        }

        List<PurposeCodeMaintenanceData> result =
                new ArrayList<>();

        for (Map<String, Object> row : rows) {

            if (!isActive(row, "FACUARCIND")) {
                continue;
            }

            PurposeCodeMaintenanceData data =
                    new PurposeCodeMaintenanceData();

            data.setPurposeCodeHub(
                    value(row, "FACUADVPUR"));

            data.setPurposeCodeCarm(
                    value(row, "FACUFACPUR"));

            data.setDescription(
                    value(row, "FACUFCPDES"));

            data.setUnconditionalCancellable(
                    properties.getReferenceData()
                            .getDefaults()
                            .getPurposeCode()
                            .getUnconditionalCancellable());

            result.add(data);
        }

        return result;
    }

    private boolean isActive(
            Map<String, Object> row,
            String archiveColumn) {

        return ACTIVE.equals(
                value(row, archiveColumn));
    }

    private String value(
            Map<String, Object> row,
            String column) {

        if (row == null || !row.containsKey(column)) {
            return null;
        }

        Object value = row.get(column);

        return value == null
                ? null
                : String.valueOf(value);
    }

    private void validateDefaultIndicators() {

        String advised =
                properties.getReferenceData()
                        .getDefaults()
                        .getFacilityType()
                        .getAdvised();

        String committed =
                properties.getReferenceData()
                        .getDefaults()
                        .getFacilityType()
                        .getCommitted();

        String unconditionalCancellable =
                properties.getReferenceData()
                        .getDefaults()
                        .getPurposeCode()
                        .getUnconditionalCancellable();

        validateIndicator(
                "facility-type.advised",
                advised);

        validateIndicator(
                "facility-type.committed",
                committed);

        validateIndicator(
                "purpose-code.unconditional-cancellable",
                unconditionalCancellable);
    }

    private void validateIndicator(
            String propertyName,
            String value) {

        if (!"Y".equals(value) && !"N".equals(value)) {

            throw new IllegalStateException(
                    "Invalid CARM maintenance default indicator: "
                            + propertyName
                            + ". Expected Y or N.");
        }
    }

    private void logResult(
            CarmMaintenanceReferenceData result) {

        LOGGER.info(
                "CARM logical maintenance data prepared. "
                        + "facilityTypeCount={}, purposeCodeCount={}",
                result.getFacilityTypes().size(),
                result.getPurposeCodes().size());

        for (FacilityTypeMaintenanceData data :
                result.getFacilityTypes()) {

            LOGGER.info(
                    "CARM_SYNC_FACILITY_TYPE "
                            + "facilityTypeCode={}, "
                            + "facilityTypeDescription={}, "
                            + "advised={}, "
                            + "committed={}",
                    data.getFacilityTypeCode(),
                    data.getFacilityTypeDescription(),
                    data.getAdvised(),
                    data.getCommitted());
        }

        for (PurposeCodeMaintenanceData data :
                result.getPurposeCodes()) {

            LOGGER.info(
                    "CARM_SYNC_PURPOSE_CODE "
                            + "purposeCodeHub={}, "
                            + "purposeCodeCarm={}, "
                            + "description={}, "
                            + "unconditionalCancellable={}",
                    data.getPurposeCodeHub(),
                    data.getPurposeCodeCarm(),
                    data.getDescription(),
                    data.getUnconditionalCancellable());
        }
    }
}