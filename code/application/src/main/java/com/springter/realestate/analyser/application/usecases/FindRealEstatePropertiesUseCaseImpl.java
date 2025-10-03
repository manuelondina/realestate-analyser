package com.springter.realestate.analyser.application.usecases;

import com.springter.realestate.analyser.domain.common.Page;
import com.springter.realestate.analyser.domain.common.PageRequest;
import com.springter.realestate.analyser.domain.realestate.RealEstateProperty;
import com.springter.realestate.analyser.domain.realestate.RealEstateSearchCriteria;
import com.springter.realestate.analyser.domain.usecases.FindRealEstatePropertiesUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Application layer implementation of the find real estate properties use case.
 * 
 * This implementation contains the business logic for searching properties.
 * Currently uses sample data, but will be updated to use repositories 
 * when infrastructure layer is implemented.
 */
@Service
@Slf4j
public class FindRealEstatePropertiesUseCaseImpl implements FindRealEstatePropertiesUseCase {

    @Override
    public Page<RealEstateProperty> findProperties(RealEstateSearchCriteria searchCriteria, PageRequest pageRequest) {
        log.info("Finding real estate properties with criteria: {}, pageRequest: {}", searchCriteria, pageRequest);
        
        // TODO: Replace with real repository call when infrastructure is implemented
        List<RealEstateProperty> allProperties = createSampleProperties();
        
        // Apply filtering (basic implementation for now)
        List<RealEstateProperty> filteredProperties = allProperties; // TODO: implement filtering
        
        // Apply pagination
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getSize(), filteredProperties.size());
        List<RealEstateProperty> pageContent = start < filteredProperties.size() ?
            filteredProperties.subList(start, end) : List.of();
        
        return Page.of(pageContent, pageRequest, filteredProperties.size());
    }
    
    /**
     * Create sample real estate properties for demonstration.
     * This will be replaced by repository calls when infrastructure layer is implemented.
     */
    private List<RealEstateProperty> createSampleProperties() {
        return List.of(
            createProperty(1001L, "Beautiful 3-bedroom house in downtown Madrid", "Madrid, Spain", 350000.00,
                RealEstateProperty.PropertyType.HOUSE, 3, 2, 120.5,
                List.of("parking", "garden", "balcony", "elevator"), 30),

            createProperty(1002L, "Modern 2-bedroom apartment with sea view", "Barcelona, Spain", 280000.00,
                RealEstateProperty.PropertyType.APARTMENT, 2, 1, 85.0,
                List.of("sea_view", "balcony", "air_conditioning", "elevator"), 15),

            createProperty(1003L, "Charming villa with pool in Valencia", "Valencia, Spain", 450000.00,
                RealEstateProperty.PropertyType.VILLA, 4, 3, 200.0,
                List.of("pool", "garden", "parking", "fireplace", "terrace"), 10),

            createProperty(1004L, "Cozy 1-bedroom condo in Bilbao", "Bilbao, Spain", 180000.00,
                RealEstateProperty.PropertyType.CONDO, 1, 1, 55.0,
                List.of("elevator", "balcony"), 5),

            createProperty(1005L, "Spacious townhouse with garage", "Seville, Spain", 320000.00,
                RealEstateProperty.PropertyType.TOWNHOUSE, 3, 2, 140.0,
                List.of("garage", "terrace", "storage"), 20),

            createProperty(1006L, "Commercial space in city center", "Madrid, Spain", 550000.00,
                RealEstateProperty.PropertyType.COMMERCIAL, null, 1, 95.0,
                List.of("elevator", "parking", "reception"), 7),

            createProperty(1007L, "Luxury penthouse with panoramic views", "Barcelona, Spain", 780000.00,
                RealEstateProperty.PropertyType.APARTMENT, 3, 2, 160.0,
                List.of("penthouse", "panoramic_view", "terrace", "elevator", "parking"), 12)
        );
    }

    private RealEstateProperty createProperty(Long id, String title, String location, Double price,
            RealEstateProperty.PropertyType type, Integer bedrooms, Integer bathrooms, Double area,
            List<String> features, int daysAgo) {
        return RealEstateProperty.builder()
                .id(id)
                .title(title)
                .description("Sample property description for " + title.toLowerCase())
                .location(location)
                .price(price)
                .propertyType(type)
                .bedrooms(bedrooms)
                .bathrooms(bathrooms)
                .area(area)
                .features(features)
                .createdAt(OffsetDateTime.now().minusDays(daysAgo))
                .updatedAt(OffsetDateTime.now().minusDays(daysAgo / 2))
                .build();
    }
}