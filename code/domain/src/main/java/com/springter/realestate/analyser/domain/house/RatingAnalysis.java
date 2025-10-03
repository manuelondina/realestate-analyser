package com.springter.realestate.analyser.domain.house;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Domain model representing a rating analysis for a house.
 * This is framework-independent and contains only business logic.
 */
@Value
@Builder
public class RatingAnalysis {
    
    Integer id;
    Integer houseId; // Reference to house ID instead of full object to avoid circular dependency
    BigDecimal overallScore;
    Integer userRatingCount;
    BigDecimal priceToSqftRatio;
    BigDecimal marketCompScore;
    BigDecimal lastSoldPrice;
    Integer timeOnMarketDays;
    OffsetDateTime ratingTimestamp;
    
    /**
     * Checks if this is a recent analysis (within last 30 days)
     */
    public boolean isRecent() {
        if (ratingTimestamp == null) {
            return false;
        }
        return ratingTimestamp.isAfter(OffsetDateTime.now().minusDays(30));
    }
    
    /**
     * Gets a human-readable rating description
     */
    public String getRatingDescription() {
        if (overallScore == null) {
            return "No rating";
        }
        
        double score = overallScore.doubleValue();
        if (score >= 4.5) return "Excellent";
        if (score >= 4.0) return "Very Good";
        if (score >= 3.5) return "Good";
        if (score >= 3.0) return "Fair";
        if (score >= 2.0) return "Poor";
        return "Very Poor";
    }
    
    /**
     * Checks if the property has been on market for a long time
     */
    public boolean isStaleProperty() {
        return timeOnMarketDays != null && timeOnMarketDays > 90;
    }
}