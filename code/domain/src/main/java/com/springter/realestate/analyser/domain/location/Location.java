package com.springter.realestate.analyser.domain.location;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

/**
 * Domain model representing a location/address.
 * This is framework-independent and contains only business logic.
 */
@Value
@Builder
public class Location {
    
    Integer id;
    String streetAddress;
    String city;
    String stateProvince;
    String zipPostalCode;
    BigDecimal latitude;
    BigDecimal longitude;
    BigDecimal schoolRatingAvg;
    Integer walkScore;
    Integer transitScore;
    
    /**
     * Gets the full address as a single string
     */
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        
        if (streetAddress != null) address.append(streetAddress);
        if (city != null) {
            if (address.length() > 0) address.append(", ");
            address.append(city);
        }
        if (stateProvince != null) {
            if (address.length() > 0) address.append(", ");
            address.append(stateProvince);
        }
        if (zipPostalCode != null) {
            if (address.length() > 0) address.append(" ");
            address.append(zipPostalCode);
        }
        
        return address.toString();
    }
    
    /**
     * Checks if location has geographic coordinates
     */
    public boolean hasCoordinates() {
        return latitude != null && longitude != null;
    }
}