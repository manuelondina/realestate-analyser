package com.springter.realestate.analyser.domain.realestate;

import lombok.Builder;
import lombok.Value;

/**
 * Domain value object for real estate search criteria.
 * Encapsulates all the filtering parameters for property searches.
 */
@Value
@Builder
public class RealEstateSearchCriteria {
    
    String location;
    Double minPrice;
    Double maxPrice;
    RealEstateProperty.PropertyType propertyType;
    
    /**
     * Creates search criteria with no filters (returns all properties)
     */
    public static RealEstateSearchCriteria noFilter() {
        return builder().build();
    }
    
    /**
     * Checks if any search criteria is specified
     */
    public boolean hasFilters() {
        return location != null || 
               minPrice != null || 
               maxPrice != null || 
               propertyType != null;
    }
}