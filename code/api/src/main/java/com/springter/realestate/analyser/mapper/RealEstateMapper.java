package com.springter.realestate.analyser.mapper;

import com.springter.realestate.analyser.domain.common.Page;
import com.springter.realestate.analyser.domain.realestate.RealEstateProperty;
import com.springter.realestate.analyser.domain.realestate.RealEstateSearchCriteria;
import com.springter.realestate.analyser.model.RealEstatePageResponse;
import com.springter.realestate.analyser.model.RealEstateProperty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * MapStruct mapper for converting between domain entities and OpenAPI DTOs.
 * 
 * This mapper belongs to the API layer and handles the conversion between
 * the internal domain representation and the external API representation.
 */
@Mapper(componentModel = "spring")
public interface RealEstateMapper {

    /**
     * Maps domain RealEstateProperty to OpenAPI DTO
     */
    @Mapping(target = "propertyType", source = "propertyType")
    com.springter.realestate.analyser.model.RealEstateProperty toDto(
        com.springter.realestate.analyser.domain.realestate.RealEstateProperty domainProperty);

    /**
     * Maps list of domain properties to list of DTOs
     */
    List<com.springter.realestate.analyser.model.RealEstateProperty> toDtoList(
        List<com.springter.realestate.analyser.domain.realestate.RealEstateProperty> domainProperties);

    /**
     * Maps domain PropertyType enum to OpenAPI DTO enum
     */
    default com.springter.realestate.analyser.model.RealEstateProperty.PropertyTypeEnum mapPropertyType(
        com.springter.realestate.analyser.domain.realestate.RealEstateProperty.PropertyType domainType) {
        if (domainType == null) {
            return null;
        }
        
        return switch (domainType) {
            case HOUSE -> com.springter.realestate.analyser.model.RealEstateProperty.PropertyTypeEnum.HOUSE;
            case APARTMENT -> com.springter.realestate.analyser.model.RealEstateProperty.PropertyTypeEnum.APARTMENT;
            case VILLA -> com.springter.realestate.analyser.model.RealEstateProperty.PropertyTypeEnum.VILLA;
            case CONDO -> com.springter.realestate.analyser.model.RealEstateProperty.PropertyTypeEnum.CONDO;
            case TOWNHOUSE -> com.springter.realestate.analyser.model.RealEstateProperty.PropertyTypeEnum.TOWNHOUSE;
            case COMMERCIAL -> com.springter.realestate.analyser.model.RealEstateProperty.PropertyTypeEnum.COMMERCIAL;
        };
    }

    /**
     * Converts a domain Page to OpenAPI PageResponse DTO
     */
    @Mapping(target = "content", source = "content")
    @Mapping(target = "page", source = "page")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "totalElements", source = "totalElements")
    @Mapping(target = "totalPages", source = "totalPages")
    @Mapping(target = "numberOfElements", source = "numberOfElements")
    RealEstatePageResponse toPageResponse(
        Page<com.springter.realestate.analyser.domain.realestate.RealEstateProperty> domainPage);

    /**
     * Creates domain search criteria from API parameters
     */
    default RealEstateSearchCriteria toSearchCriteria(String location, Double minPrice, Double maxPrice, String propertyType) {
        return RealEstateSearchCriteria.builder()
                .location(location)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .propertyType(mapPropertyTypeFromString(propertyType))
                .build();
    }

    /**
     * Maps string property type to domain enum
     */
    default RealEstateProperty.PropertyType mapPropertyTypeFromString(String propertyType) {
        if (propertyType == null) {
            return null;
        }
        
        try {
            return RealEstateProperty.PropertyType.valueOf(propertyType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Invalid property type, ignore filter
        }
    }
}