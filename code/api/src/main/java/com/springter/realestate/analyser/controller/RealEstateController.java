package com.springter.realestate.analyser.controller;

import com.springter.realestate.analyser.api.RealEstateApi;
import com.springter.realestate.analyser.domain.common.Page;
import com.springter.realestate.analyser.domain.common.PageRequest;
import com.springter.realestate.analyser.domain.realestate.RealEstateProperty;
import com.springter.realestate.analyser.domain.realestate.RealEstateSearchCriteria;
import com.springter.realestate.analyser.domain.usecases.FindRealEstatePropertiesUseCase;
import com.springter.realestate.analyser.mapper.RealEstateMapper;
import com.springter.realestate.analyser.model.RealEstatePageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RealEstateController implements RealEstateApi {
    
    private final FindRealEstatePropertiesUseCase findPropertiesUseCase;
    private final RealEstateMapper mapper;

    @Override
    public ResponseEntity<RealEstatePageResponse> getAllRealEstate(
            Integer page,
            Integer size,
            String location,
            Double minPrice,
            Double maxPrice,
            String propertyType) {
        log.info("Getting real estate properties - page: {}, size: {}, location: {}, minPrice: {}, maxPrice: {}, propertyType: {},",
                page, size, location, minPrice, maxPrice, propertyType);

        // Create domain objects from API parameters
        PageRequest pageRequest = PageRequest.of(
            page != null ? page : 0,
            size != null ? size : 20
        );
        
        RealEstateSearchCriteria searchCriteria = mapper.toSearchCriteria(
            location, minPrice, maxPrice, propertyType
        );

        // Use the domain use case to find properties
        Page<RealEstateProperty> propertiesPage = findPropertiesUseCase.findProperties(
            searchCriteria, pageRequest
        );

        // Convert domain result to API DTO
        RealEstatePageResponse response = mapper.toPageResponse(propertiesPage);

        return ResponseEntity.ok(response);
    }

}