package com.springter.realestate.analyser.domain.house;

import com.springter.realestate.analyser.domain.location.Location;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

/**
 * Domain model representing a house/property.
 * This is framework-independent and contains only business logic.
 */
@Value
@Builder
public class House {
    
    Integer id;
    Location location;
    String name;
    String listingStatus;
    Integer yearBuilt;
    Integer squareFootage;
    Integer numBedrooms;
    BigDecimal numBathrooms;
    String propertyType;
    String heatingType;
    List<RatingAnalysis> ratingAnalyses;
    
    /**
     * Gets the age of the house in years
     */
    public Integer getAgeInYears() {
        if (yearBuilt == null) {
            return null;
        }
        return java.time.LocalDate.now().getYear() - yearBuilt;
    }
    
    /**
     * Checks if the house is currently for sale
     */
    public boolean isForSale() {
        return "FOR_SALE".equalsIgnoreCase(listingStatus) || 
               "ACTIVE".equalsIgnoreCase(listingStatus);
    }
    
    /**
     * Gets the latest rating analysis if available
     */
    public RatingAnalysis getLatestRating() {
        if (ratingAnalyses == null || ratingAnalyses.isEmpty()) {
            return null;
        }
        
        return ratingAnalyses.stream()
            .max((r1, r2) -> {
                if (r1.getRatingTimestamp() == null && r2.getRatingTimestamp() == null) return 0;
                if (r1.getRatingTimestamp() == null) return -1;
                if (r2.getRatingTimestamp() == null) return 1;
                return r1.getRatingTimestamp().compareTo(r2.getRatingTimestamp());
            })
            .orElse(null);
    }
}