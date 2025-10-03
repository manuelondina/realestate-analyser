package com.springter.realestate.analyser.infrastructure.persistence.adapter;

import com.springter.realestate.analyser.domain.house.House;
import com.springter.realestate.analyser.domain.repositories.HouseRepository;
import com.springter.realestate.analyser.infrastructure.persistence.jpa.entity.HouseJpa;
import com.springter.realestate.analyser.infrastructure.persistence.jpa.mapper.HousePersistenceMapper;
import com.springter.realestate.analyser.infrastructure.persistence.jpa.repository.HouseJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Infrastructure adapter implementing the HouseRepository domain interface.
 * 
 * This adapter bridges the domain layer (which defines what it needs)
 * with the infrastructure layer (which provides JPA implementation).
 * 
 * It uses MapStruct to convert between JPA entities and domain models,
 * maintaining clean architecture boundaries.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class HouseRepositoryImpl implements HouseRepository {

    private final HouseJpaRepository houseJpaRepository;
    private final HousePersistenceMapper mapper;

    @Override
    public List<House> findAll() {
        log.debug("Finding all houses from database");
        List<HouseJpa> houseJpaList = houseJpaRepository.findAllWithLocationAndRatings();
        List<House> houses = mapper.toDomainList(houseJpaList);
        log.debug("Found {} houses", houses.size());
        return houses;
    }

    @Override
    public Optional<House> findById(Integer id) {
        log.debug("Finding house by id: {}", id);
        Optional<HouseJpa> houseJpaOpt = houseJpaRepository.findById(id);
        Optional<House> house = houseJpaOpt.map(mapper::toDomain);
        log.debug("House with id {} found: {}", id, house.isPresent());
        return house;
    }

    @Override
    public List<House> findByCity(String city) {
        log.debug("Finding houses by city: {}", city);
        List<HouseJpa> houseJpaList = houseJpaRepository.findByCityContainingIgnoreCase(city);
        List<House> houses = mapper.toDomainList(houseJpaList);
        log.debug("Found {} houses in city: {}", houses.size(), city);
        return houses;
    }

    @Override
    public List<House> findByPropertyType(String propertyType) {
        log.debug("Finding houses by property type: {}", propertyType);
        List<HouseJpa> houseJpaList = houseJpaRepository.findByPropertyTypeIgnoreCase(propertyType);
        List<House> houses = mapper.toDomainList(houseJpaList);
        log.debug("Found {} houses of type: {}", houses.size(), propertyType);
        return houses;
    }

    @Override
    public List<House> findByListingStatus(String status) {
        log.debug("Finding houses by listing status: {}", status);
        List<HouseJpa> houseJpaList = houseJpaRepository.findByListingStatusIgnoreCase(status);
        List<House> houses = mapper.toDomainList(houseJpaList);
        log.debug("Found {} houses with status: {}", houses.size(), status);
        return houses;
    }

    @Override
    public House save(House house) {
        log.debug("Saving house with id: {}", house.getId());
        // TODO: Implement reverse mapping from domain to JPA entity when needed
        throw new UnsupportedOperationException("Save operation not yet implemented");
    }

    @Override
    public void deleteById(Integer id) {
        log.debug("Deleting house with id: {}", id);
        houseJpaRepository.deleteById(id);
        log.debug("House with id {} deleted", id);
    }
}