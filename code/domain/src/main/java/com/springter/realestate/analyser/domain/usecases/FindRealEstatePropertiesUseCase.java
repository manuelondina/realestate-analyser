package com.springter.realestate.analyser.domain.usecases;

import com.springter.realestate.analyser.domain.common.Page;
import com.springter.realestate.analyser.domain.common.PageRequest;
import com.springter.realestate.analyser.domain.realestate.RealEstateProperty;
import com.springter.realestate.analyser.domain.realestate.RealEstateSearchCriteria;

/**
 * Domain use case interface for finding real estate properties.
 * 
 * This interface belongs to the domain layer and defines the contract
 * for the business operation of searching properties. The implementation
 * belongs to the application layer.
 * 
 * The API layer depends only on this interface, not on concrete implementations.
 */
public interface FindRealEstatePropertiesUseCase {
    
    /**
     * Finds real estate properties based on search criteria with pagination.
     * 
     * @param searchCriteria The criteria to filter properties by
     * @param pageRequest The pagination parameters
     * @return A page of properties matching the search criteria
     */
    Page<RealEstateProperty> findProperties(RealEstateSearchCriteria searchCriteria, PageRequest pageRequest);
}