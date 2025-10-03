package com.springter.realestate.analyser.domain.repositories;

import com.springter.realestate.analyser.domain.house.House;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository interface for House entities.
 * 
 * This interface belongs to the domain layer and defines the contract
 * for house data access operations. The implementation belongs to the 
 * infrastructure layer.
 * 
 * Following the Dependency Inversion Principle, the domain defines
 * what it needs, and infrastructure provides the implementation.
 */
public interface HouseRepository {

    /**
     * Finds all houses with their complete data (location, ratings)
     * 
     * @return List of all houses
     */
    List<House> findAll();

    /**
     * Finds a house by its ID
     * 
     * @param id The house ID
     * @return Optional containing the house if found
     */
    Optional<House> findById(Integer id);

    /**
     * Finds houses by city (partial match, case insensitive)
     * 
     * @param city The city name to search for
     * @return List of houses in the specified city
     */
    List<House> findByCity(String city);

    /**
     * Finds houses by property type (exact match, case insensitive)
     * 
     * @param propertyType The property type to search for
     * @return List of houses of the specified type
     */
    List<House> findByPropertyType(String propertyType);

    /**
     * Finds houses by listing status (exact match, case insensitive)
     * 
     * @param status The listing status to search for
     * @return List of houses with the specified status
     */
    List<House> findByListingStatus(String status);

    /**
     * Saves a house (create or update)
     * 
     * @param house The house to save
     * @return The saved house
     */
    House save(House house);

    /**
     * Deletes a house by ID
     * 
     * @param id The house ID to delete
     */
    void deleteById(Integer id);
}