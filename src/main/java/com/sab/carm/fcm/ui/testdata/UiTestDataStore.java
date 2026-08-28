package com.sab.carm.fcm.ui.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sab.carm.fcm.dto.FacilityTypeMaintenanceResponse;
import com.sab.carm.fcm.dto.PurposeCodeMaintenanceResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UiTestDataStore {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    private List<UiTestDataFixture.FacilityTypeFixture> facilityTypes =
            new ArrayList<UiTestDataFixture.FacilityTypeFixture>();

    private List<UiTestDataFixture.PurposeCodeFixture> purposeCodes =
            new ArrayList<UiTestDataFixture.PurposeCodeFixture>();

    public UiTestDataStore(
            ObjectMapper objectMapper,
            ResourceLoader resourceLoader) {

        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
    }

    public synchronized void reset(String location)
            throws IOException {

        Resource resource =
                resourceLoader.getResource(location);

        try (InputStream inputStream =
                     resource.getInputStream()) {

            UiTestDataFixture fixture =
                    objectMapper.readValue(
                            inputStream,
                            UiTestDataFixture.class);

            facilityTypes =
                    copyFacilityTypes(
                            fixture.getFacilityTypes());

            purposeCodes =
                    copyPurposeCodes(
                            fixture.getPurposeCodes());
        }
    }

    public synchronized List<FacilityTypeMaintenanceResponse>
    getFacilityTypes() {

        return facilityTypes.stream()
                .filter(
                        UiTestDataFixture.FacilityTypeFixture
                                ::isActive)
                .map(this::facilityTypeResponse)
                .collect(Collectors.toList());
    }

    public synchronized List<PurposeCodeMaintenanceResponse>
    getPurposeCodes() {

        return purposeCodes.stream()
                .filter(
                        UiTestDataFixture.PurposeCodeFixture
                                ::isActive)
                .map(this::purposeCodeResponse)
                .collect(Collectors.toList());
    }

    public synchronized boolean facilityTypeExists(
            String code) {

        return findFacilityType(code) != null;
    }

    public synchronized void saveFacilityType(
            String code,
            String description,
            String advised,
            String committed) {

        UiTestDataFixture.FacilityTypeFixture existing =
                findFacilityType(code);

        if (existing == null) {

            existing =
                    new UiTestDataFixture.FacilityTypeFixture();

            existing.setFacilityTypeCode(code);
            existing.setActive(true);

            facilityTypes.add(existing);
        }

        existing.setFacilityTypeDescription(description);
        existing.setAdvised(advised);
        existing.setCommitted(committed);
    }

    public synchronized void deleteFacilityType(
            String code) {

        UiTestDataFixture.FacilityTypeFixture existing =
                findFacilityType(code);

        if (existing != null) {
            existing.setActive(false);
        }
    }

    public synchronized boolean purposeCodeExists(
            String hub,
            String carm) {

        return findPurposeCode(hub, carm) != null;
    }

    public synchronized void savePurposeCode(
            String hub,
            String carm,
            String description,
            String unconditionalCancellable) {

        UiTestDataFixture.PurposeCodeFixture existing =
                findPurposeCode(hub, carm);

        if (existing == null) {

            existing =
                    new UiTestDataFixture.PurposeCodeFixture();

            existing.setPurposeCodeHub(hub);
            existing.setPurposeCodeCarm(carm);
            existing.setActive(true);

            purposeCodes.add(existing);
        }

        existing.setDescription(description);
        existing.setUnconditionalCancellable(
                unconditionalCancellable);
    }

    public synchronized void deletePurposeCode(
            String hub,
            String carm) {

        UiTestDataFixture.PurposeCodeFixture existing =
                findPurposeCode(hub, carm);

        if (existing != null) {
            existing.setActive(false);
        }
    }

    private UiTestDataFixture.FacilityTypeFixture
    findFacilityType(String code) {

        return facilityTypes.stream()
                .filter(item ->
                        code.equals(
                                item.getFacilityTypeCode()))
                .findFirst()
                .orElse(null);
    }

    private UiTestDataFixture.PurposeCodeFixture
    findPurposeCode(
            String hub,
            String carm) {

        return purposeCodes.stream()
                .filter(item ->
                        hub.equals(
                                item.getPurposeCodeHub())
                                && carm.equals(
                                item.getPurposeCodeCarm()))
                .findFirst()
                .orElse(null);
    }

    private FacilityTypeMaintenanceResponse
    facilityTypeResponse(
            UiTestDataFixture.FacilityTypeFixture item) {

        FacilityTypeMaintenanceResponse response =
                new FacilityTypeMaintenanceResponse();

        response.setFacilityTypeCode(
                item.getFacilityTypeCode());

        response.setFacilityTypeDescription(
                item.getFacilityTypeDescription());

        response.setAdvised(
                item.getAdvised());

        response.setCommitted(
                item.getCommitted());

        response.setActive(
                item.isActive());

        return response;
    }

    private PurposeCodeMaintenanceResponse
    purposeCodeResponse(
            UiTestDataFixture.PurposeCodeFixture item) {

        PurposeCodeMaintenanceResponse response =
                new PurposeCodeMaintenanceResponse();

        response.setPurposeCodeHub(
                item.getPurposeCodeHub());

        response.setPurposeCodeCarm(
                item.getPurposeCodeCarm());

        response.setDescription(
                item.getDescription());

        response.setUnconditionalCancellable(
                item.getUnconditionalCancellable());

        response.setActive(
                item.isActive());

        return response;
    }

    private List<UiTestDataFixture.FacilityTypeFixture>
    copyFacilityTypes(
            List<UiTestDataFixture.FacilityTypeFixture> source) {

        if (source == null) {
            return new ArrayList<
                    UiTestDataFixture.FacilityTypeFixture>();
        }

        return source.stream()
                .map(item -> {

                    UiTestDataFixture.FacilityTypeFixture copy =
                            new UiTestDataFixture.FacilityTypeFixture();

                    copy.setFacilityTypeCode(
                            item.getFacilityTypeCode());

                    copy.setFacilityTypeDescription(
                            item.getFacilityTypeDescription());

                    copy.setAdvised(
                            item.getAdvised());

                    copy.setCommitted(
                            item.getCommitted());

                    copy.setActive(
                            item.isActive());

                    return copy;

                })
                .collect(Collectors.toList());
    }

    private List<UiTestDataFixture.PurposeCodeFixture>
    copyPurposeCodes(
            List<UiTestDataFixture.PurposeCodeFixture> source) {

        if (source == null) {
            return new ArrayList<
                    UiTestDataFixture.PurposeCodeFixture>();
        }

        return source.stream()
                .map(item -> {

                    UiTestDataFixture.PurposeCodeFixture copy =
                            new UiTestDataFixture.PurposeCodeFixture();

                    copy.setPurposeCodeHub(
                            item.getPurposeCodeHub());

                    copy.setPurposeCodeCarm(
                            item.getPurposeCodeCarm());

                    copy.setDescription(
                            item.getDescription());

                    copy.setUnconditionalCancellable(
                            item.getUnconditionalCancellable());

                    copy.setActive(
                            item.isActive());

                    return copy;

                })
                .collect(Collectors.toList());
    }
}