package com.sab.carm.fcm.carm.dto;

public class CarmReferenceDataRequest {

    private Header header;
    private Body body;

    public CarmReferenceDataRequest() {
    }

    public CarmReferenceDataRequest(String siteId, String tableName) {
        this.header = new Header(siteId);
        this.body = new Body(tableName);
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static class Header {

        private String siteId;

        public Header() {
        }

        public Header(String siteId) {
            this.siteId = siteId;
        }

        public String getSiteId() {
            return siteId;
        }

        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }
    }

    public static class Body {

        private String tableName;

        public Body() {
        }

        public Body(String tableName) {
            this.tableName = tableName;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }
    }
}