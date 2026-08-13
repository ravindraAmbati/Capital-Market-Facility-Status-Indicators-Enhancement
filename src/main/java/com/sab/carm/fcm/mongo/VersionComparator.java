package com.sab.carm.fcm.mongo;

import org.springframework.stereotype.Component;

/**
 * Compares semantic application version strings.
 */
@Component
public class VersionComparator {

    public int compare(String left, String right) {
        String[] leftParts = left.split("\\.");
        String[] rightParts = right.split("\\.");
        int max = Math.max(leftParts.length, rightParts.length);
        for (int index = 0; index < max; index++) {
            int leftValue = index < leftParts.length ? Integer.parseInt(leftParts[index]) : 0;
            int rightValue = index < rightParts.length ? Integer.parseInt(rightParts[index]) : 0;
            if (leftValue != rightValue) {
                return Integer.compare(leftValue, rightValue);
            }
        }
        return 0;
    }
}
