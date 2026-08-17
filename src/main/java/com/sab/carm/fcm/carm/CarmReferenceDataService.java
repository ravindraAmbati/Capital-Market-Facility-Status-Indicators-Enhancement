package com.sab.carm.fcm.carm;

import com.sab.carm.fcm.carm.dto.CarmReferenceDataResponse;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class CarmReferenceDataService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    CarmReferenceDataService.class);

    private final CarmClient carmClient;
    private final CarmProperties properties;

    public CarmReferenceDataService(
            CarmClient carmClient,
            CarmProperties properties) {

        this.carmClient = carmClient;
        this.properties = properties;
    }

    public Map<String, List<Map<String, Object>>>
    fetchConfiguredReferenceData() {

        Map<String, List<Map<String, Object>>> result =
                new LinkedHashMap<>();

        for (String tableName :
                getConfiguredTables()) {

            try {
                CarmReferenceDataResponse response =
                        carmClient.fetchReferenceData(
                                tableName);

                List<Map<String, Object>> rows =
                        extractRows(response);

                result.put(tableName, rows);

                LOGGER.info(
                        "CARM reference data fetched successfully. "
                                + "table={}, rowCount={}",
                        tableName,
                        rows.size());

            } catch (RestClientException ex) {

                LOGGER.error(
                        "Failed to fetch CARM reference data. "
                                + "table={}",
                        tableName,
                        ex);
            }
        }

        return result;
    }

    private List<String> getConfiguredTables() {

        List<String> tables =
                properties.getReferenceData().getTables();

        return tables == null
                ? Collections.emptyList()
                : tables;
    }

    private List<Map<String, Object>> extractRows(
            CarmReferenceDataResponse response) {

        if (response == null
                || response.getBody() == null
                || response.getBody().getReferenceTable() == null) {

            return Collections.emptyList();
        }

        return response.getBody()
                .getReferenceTable();
    }
}