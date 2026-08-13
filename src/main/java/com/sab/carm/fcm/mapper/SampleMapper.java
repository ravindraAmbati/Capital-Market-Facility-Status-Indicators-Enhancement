package com.sab.carm.fcm.mapper;

import com.sab.carm.fcm.dto.ApiResponse;
import com.sab.carm.fcm.entity.SampleEntity;
import org.springframework.stereotype.Component;

/**
 * Maps sample entities to API responses.
 */
@Component
public class SampleMapper {

    public ApiResponse toResponse(SampleEntity entity) {
        return new ApiResponse(entity.getMessage());
    }
}
