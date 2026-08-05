package com.company.application.mapper;

import com.company.application.dto.ApiResponse;
import com.company.application.entity.SampleEntity;
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
