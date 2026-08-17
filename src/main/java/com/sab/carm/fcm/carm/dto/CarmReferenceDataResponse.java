package com.sab.carm.fcm.carm.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarmReferenceDataResponse {

    private Map<String, Object> header;
    private Body body;

    public Map<String, Object> getHeader() {
        return header;
    }

    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static class Body {

        private List<Map<String, Object>> referenceTable =
                new ArrayList<>();

        public List<Map<String, Object>> getReferenceTable() {
            return referenceTable;
        }

        public void setReferenceTable(
                List<Map<String, Object>> referenceTable) {

            this.referenceTable = referenceTable;
        }
    }
}