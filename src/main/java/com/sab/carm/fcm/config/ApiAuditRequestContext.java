package com.sab.carm.fcm.config;

import javax.servlet.http.HttpServletRequest;

public final class ApiAuditRequestContext {

    public static final String RELATIONSHIP_ID =
            "carm.fcm.audit.relationshipId";
    public static final String SERIAL_NO =
            "carm.fcm.audit.serialNo";
    public static final String FACILITY_NO =
            "carm.fcm.audit.facilityNo";
    public static final String USER_ID =
            "carm.fcm.audit.userId";

    private ApiAuditRequestContext() {
    }

    public static void setRelationshipId(
            HttpServletRequest request,
            String value) {
        request.setAttribute(RELATIONSHIP_ID, value);
    }

    public static void setSerialNo(
            HttpServletRequest request,
            String value) {
        request.setAttribute(SERIAL_NO, value);
    }

    public static void setFacilityNo(
            HttpServletRequest request,
            String value) {
        request.setAttribute(FACILITY_NO, value);
    }

    public static void setUserId(
            HttpServletRequest request,
            String value) {
        request.setAttribute(USER_ID, value);
    }

    public static String get(
            HttpServletRequest request,
            String attribute) {
        Object value = request.getAttribute(attribute);
        return value == null ? null : value.toString();
    }
}
