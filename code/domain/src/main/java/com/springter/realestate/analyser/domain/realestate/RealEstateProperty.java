package com.springter.realestate.analyser.domain.realestate;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Domain entity representing a real estate property.
 * This is framework-independent and contains only business logic.
 */
@Value
@Builder
public class RealEstateProperty {
    
    Long id;
    String title;
    String description;
    String location;
    Double price;
    PropertyType propertyType;
    Integer bedrooms;
    Integer bathrooms;
    Double area;
    List<String> features;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
    
    /**
     * Domain enumeration for property types
     */
    public enum PropertyType {
        HOUSE,
        APARTMENT, 
        VILLA,
        CONDO,
        TOWNHOUSE,
        COMMERCIAL
    }
}