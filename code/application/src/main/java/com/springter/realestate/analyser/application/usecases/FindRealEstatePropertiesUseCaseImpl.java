package com.springter.realestate.analyser.application.usecases;

import com.springter.realestate.analyser.domain.common.Page;
import com.springter.realestate.analyser.domain.common.PageRequest;
import com.springter.realestate.analyser.domain.house.House;
import com.springter.realestate.analyser.domain.realestate.RealEstateProperty;
import com.springter.realestate.analyser.domain.realestate.RealEstateSearchCriteria;
import com.springter.realestate.analyser.domain.repositories.HouseRepository;
import com.springter.realestate.analyser.domain.usecases.FindRealEstatePropertiesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Application layer implementation of the find real estate properties use case.
 * 
 * This implementation contains the business logic for searching properties.
 * Currently uses sample data, but will be updated to use repositories 
 * when infrastructure layer is implemented.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FindRealEstatePropertiesUseCaseImpl implements FindRealEstatePropertiesUseCase {

    private final HouseRepository houseRepository;

    @Override
    public Page<RealEstateProperty> findProperties(RealEstateSearchCriteria searchCriteria, PageRequest pageRequest) {
        log.info("Finding real estate properties with criteria: {}, pageRequest: {}", searchCriteria, pageRequest);
        
        // Get houses from repository
        List<House> houses = houseRepository.findAll();
        
        // Convert House domain models to RealEstateProperty domain models
        List<RealEstateProperty> allProperties = houses.stream()
            .map(this::convertHouseToRealEstateProperty)
            .collect(Collectors.toList());
        
        // Apply filtering based on search criteria
        List<RealEstateProperty> filteredProperties = applyFiltering(allProperties, searchCriteria);
        
        // Apply pagination
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getSize(), filteredProperties.size());
        List<RealEstateProperty> pageContent = start < filteredProperties.size() ?
            filteredProperties.subList(start, end) : List.of();
        
        return Page.of(pageContent, pageRequest, filteredProperties.size());
    }
    
    /**
     * Converts House domain model to RealEstateProperty domain model
     */
    private RealEstateProperty convertHouseToRealEstateProperty(House house) {
        return RealEstateProperty.builder()
                .id(house.getId() != null ? house.getId().longValue() : null)
                .title(house.getName())
                .description(generateDescription(house))
                .location(house.getLocation() != null ? house.getLocation().getFullAddress() : null)
                .price(calculatePrice(house))
                .propertyType(mapPropertyType(house.getPropertyType()))
                .bedrooms(house.getNumBedrooms())
                .bathrooms(house.getNumBathrooms() != null ? house.getNumBathrooms().intValue() : null)
                .area(house.getSquareFootage() != null ? house.getSquareFootage().doubleValue() : null)
                .features(generateFeatures(house))
                .createdAt(OffsetDateTime.now()) // TODO: Get from house creation timestamp when available
                .updatedAt(OffsetDateTime.now()) // TODO: Get from house update timestamp when available
                .build();
    }
    
    /**
     * Applies search criteria filtering to the properties list
     */
    private List<RealEstateProperty> applyFiltering(List<RealEstateProperty> properties, RealEstateSearchCriteria criteria) {
        return properties.stream()
            .filter(property -> matchesLocation(property, criteria.getLocation()))
            .filter(property -> matchesPropertyType(property, criteria.getPropertyType()))
            .filter(property -> matchesPriceRange(property, criteria.getMinPrice(), criteria.getMaxPrice()))
            .collect(Collectors.toList());
    }
    
    private boolean matchesLocation(RealEstateProperty property, String location) {
        if (location == null || location.trim().isEmpty()) {
            return true;
        }
        return property.getLocation() != null && 
               property.getLocation().toLowerCase().contains(location.toLowerCase());
    }
    
    private boolean matchesPropertyType(RealEstateProperty property, RealEstateProperty.PropertyType propertyType) {
        if (propertyType == null) {
            return true;
        }
        return property.getPropertyType() == propertyType;
    }
    
    private boolean matchesPriceRange(RealEstateProperty property, Double minPrice, Double maxPrice) {
        if (property.getPrice() == null) {
            return minPrice == null && maxPrice == null;
        }
        
        if (minPrice != null && property.getPrice() < minPrice) {
            return false;
        }
        
        if (maxPrice != null && property.getPrice() > maxPrice) {
            return false;
        }
        
        return true;
    }
    
    private String generateDescription(House house) {
        StringBuilder desc = new StringBuilder();
        if (house.getNumBedrooms() != null) {
            desc.append(house.getNumBedrooms()).append("-bedroom ");
        }
        if (house.getPropertyType() != null) {
            desc.append(house.getPropertyType().toLowerCase()).append(" ");
        }
        if (house.getLocation() != null && house.getLocation().getCity() != null) {
            desc.append("in ").append(house.getLocation().getCity());
        }
        return desc.toString().trim();
    }
    
    private Double calculatePrice(House house) {
        // TODO: Calculate price from rating analysis or other house data
        // For now, use last sold price if available
        if (house.getRatingAnalyses() != null && !house.getRatingAnalyses().isEmpty()) {
            return house.getLatestRating() != null && house.getLatestRating().getLastSoldPrice() != null ?
                house.getLatestRating().getLastSoldPrice().doubleValue() : null;
        }
        return null;
    }
    
    private RealEstateProperty.PropertyType mapPropertyType(String propertyType) {
        if (propertyType == null) {
            return null;
        }
        
        try {
            return RealEstateProperty.PropertyType.valueOf(propertyType.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Unknown property type: {}", propertyType);
            return null;
        }
    }
    
    private List<String> generateFeatures(House house) {
        List<String> features = new java.util.ArrayList<>();
        
        if (house.getHeatingType() != null) {
            features.add(house.getHeatingType().toLowerCase().replace("_", " "));
        }
        
        if (house.getLocation() != null) {
            if (house.getLocation().getWalkScore() != null && house.getLocation().getWalkScore() > 70) {
                features.add("walkable");
            }
            if (house.getLocation().getTransitScore() != null && house.getLocation().getTransitScore() > 70) {
                features.add("good_transit");
            }
            if (house.getLocation().getSchoolRatingAvg() != null && house.getLocation().getSchoolRatingAvg().doubleValue() > 4.0) {
                features.add("good_schools");
            }
        }
        
        if (house.getYearBuilt() != null && house.getAgeInYears() != null && house.getAgeInYears() < 10) {
            features.add("new_construction");
        }
        
        return features;
    }
}