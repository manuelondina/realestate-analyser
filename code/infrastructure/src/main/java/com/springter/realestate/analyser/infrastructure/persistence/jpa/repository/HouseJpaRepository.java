package com.springter.realestate.analyser.infrastructure.persistence.jpa.repository;

import com.springter.realestate.analyser.infrastructure.persistence.jpa.entity.HouseJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for House entities.
 * Provides database access methods for house data.
 */
@Repository
public interface HouseJpaRepository extends JpaRepository<HouseJpa, Integer> {

    /**
     * Find all houses with their locations and rating analyses
     */
    @Query("SELECT DISTINCT h FROM HouseJpa h " +
           "LEFT JOIN FETCH h.location " +
           "LEFT JOIN FETCH h.ratingAnalyses")
    List<HouseJpa> findAllWithLocationAndRatings();

    /**
     * Find houses by city
     */
    @Query("SELECT h FROM HouseJpa h " +
           "LEFT JOIN FETCH h.location l " +
           "WHERE LOWER(l.city) LIKE LOWER(CONCAT('%', :city, '%'))")
    List<HouseJpa> findByCityContainingIgnoreCase(@Param("city") String city);

    /**
     * Find houses by property type
     */
    @Query("SELECT h FROM HouseJpa h " +
           "LEFT JOIN FETCH h.location " +
           "WHERE LOWER(h.propertyType) = LOWER(:propertyType)")
    List<HouseJpa> findByPropertyTypeIgnoreCase(@Param("propertyType") String propertyType);

    /**
     * Find houses by listing status
     */
    @Query("SELECT h FROM HouseJpa h " +
           "LEFT JOIN FETCH h.location " +
           "WHERE LOWER(h.listingStatus) = LOWER(:status)")
    List<HouseJpa> findByListingStatusIgnoreCase(@Param("status") String status);
}