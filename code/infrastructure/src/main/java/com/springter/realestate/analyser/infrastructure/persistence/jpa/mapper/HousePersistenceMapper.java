package com.springter.realestate.analyser.infrastructure.persistence.jpa.mapper;

import com.springter.realestate.analyser.domain.house.House;
import com.springter.realestate.analyser.domain.house.RatingAnalysis;
import com.springter.realestate.analyser.domain.location.Location;
import com.springter.realestate.analyser.infrastructure.persistence.jpa.entity.HouseJpa;
import com.springter.realestate.analyser.infrastructure.persistence.jpa.entity.LocationJpa;
import com.springter.realestate.analyser.infrastructure.persistence.jpa.entity.RatingAnalysisJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * MapStruct mapper for converting between JPA entities and domain models.
 * 
 * This mapper belongs to the infrastructure layer and handles the conversion 
 * from database persistence models to domain business models.
 */
@Mapper(componentModel = "spring")
public interface HousePersistenceMapper {

    /**
     * Maps LocationJpa to Location domain model
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "streetAddress", source = "streetAddress")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "stateProvince", source = "stateProvince")
    @Mapping(target = "zipPostalCode", source = "zipPostalCode")
    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    @Mapping(target = "schoolRatingAvg", source = "schoolRatingAvg")
    @Mapping(target = "walkScore", source = "walkScore")
    @Mapping(target = "transitScore", source = "transitScore")
    Location toDomain(LocationJpa locationJpa);

    /**
     * Maps HouseJpa to House domain model
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "listingStatus", source = "listingStatus")
    @Mapping(target = "yearBuilt", source = "yearBuilt")
    @Mapping(target = "squareFootage", source = "squareFootage")
    @Mapping(target = "numBedrooms", source = "numBedrooms")
    @Mapping(target = "numBathrooms", source = "numBathrooms")
    @Mapping(target = "propertyType", source = "propertyType")
    @Mapping(target = "heatingType", source = "heatingType")
    @Mapping(target = "ratingAnalyses", source = "ratingAnalyses")
    House toDomain(HouseJpa houseJpa);

    /**
     * Maps RatingAnalysisJpa to RatingAnalysis domain model
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "houseId", source = "house.id")
    @Mapping(target = "overallScore", source = "overallScore")
    @Mapping(target = "userRatingCount", source = "userRatingCount")
    @Mapping(target = "priceToSqftRatio", source = "priceToSqftRatio")
    @Mapping(target = "marketCompScore", source = "marketCompScore")
    @Mapping(target = "lastSoldPrice", source = "lastSoldPrice")
    @Mapping(target = "timeOnMarketDays", source = "timeOnMarketDays")
    @Mapping(target = "ratingTimestamp", source = "ratingTimestamp")
    RatingAnalysis toDomain(RatingAnalysisJpa ratingAnalysisJpa);

    /**
     * Maps list of HouseJpa to list of House domain models
     */
    List<House> toDomainList(List<HouseJpa> houseJpaList);

    /**
     * Maps list of LocationJpa to list of Location domain models
     */
    List<Location> toLocationDomainList(List<LocationJpa> locationJpaList);

    /**
     * Maps list of RatingAnalysisJpa to list of RatingAnalysis domain models
     */
    List<RatingAnalysis> toRatingAnalysisDomainList(List<RatingAnalysisJpa> ratingAnalysisJpaList);
}