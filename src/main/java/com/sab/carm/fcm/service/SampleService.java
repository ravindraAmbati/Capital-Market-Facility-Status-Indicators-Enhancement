package com.sab.carm.fcm.service;

import com.sab.carm.fcm.dto.ApiResponse;
import com.sab.carm.fcm.entity.SampleEntity;
import com.sab.carm.fcm.repository.SampleRepository;
import org.springframework.stereotype.Service;

/**
 * Sample service proving controller-service-repository wiring.
 */
@Service
public class SampleService {

    private final SampleRepository repository;

    public SampleService(SampleRepository repository) {
        this.repository = repository;
    }

    public ApiResponse readSample() {
        return new ApiResponse("sample read successful");
    }

    public ApiResponse createSample() {
        repository.save(new SampleEntity("sample write successful"));
        return new ApiResponse("sample write successful");
    }

    public ApiResponse readAdminSample() {
        return new ApiResponse("admin sample successful");
    }

    public ApiResponse readOnlySample() {
        return new ApiResponse("readonly sample successful");
    }
}
