package com.springter.realestate.analyser.controller;

import com.springter.realestate.analyser.api.RealEstateApi;
import com.springter.realestate.analyser.domain.common.Page;
import com.springter.realestate.analyser.domain.common.PageRequest;
import com.springter.realestate.analyser.model.RealEstatePageResponse;
import com.springter.realestate.analyser.model.RealEstateProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@Slf4j
public class RealEstateController implements RealEstateApi {

    @Override
    public ResponseEntity<RealEstatePageResponse> getAllRealEstate(
            Integer page,
            Integer size,
            String location,
            Double minPrice,
            Double maxPrice,
            String propertyType) {
        log.info("Getting real estate properties - page: {}, size: {}, location: {}, minPrice: {}, maxPrice: {}, propertyType: {},",
                page, size, location, minPrice, maxPrice, propertyType);

        PageRequest pageRequest = PageRequest.of(
            page != null ? page : 0,
            size != null ? size : 20
        );

        List<RealEstateProperty> allProperties = createSampleProperties();

        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getSize(), allProperties.size());
        List<RealEstateProperty> pageContent = start < allProperties.size() ?
            allProperties.subList(start, end) : List.of();

        Page<RealEstateProperty> propertiesPage = Page.of(pageContent, pageRequest, allProperties.size());

        RealEstatePageResponse response = convertToPageResponse(propertiesPage);

        return ResponseEntity.ok(response);
    }

    private RealEstatePageResponse convertToPageResponse(Page<RealEstateProperty> page) {
        return new RealEstatePageResponse()
                .content(page.getContent())
                .page(page.getPage())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .numberOfElements(page.getNumberOfElements());
    }

    /**
     * Create sample real estate properties for demonstration
     * Expanded to show proper pagination with more properties
     */
    private List<RealEstateProperty> createSampleProperties() {
        return List.of(
            createProperty(1001L, "Beautiful 3-bedroom house in downtown Madrid", "Madrid, Spain", 350000.00,
                RealEstateProperty.PropertyTypeEnum.HOUSE, 3, 2, 120.5,
                List.of("parking", "garden", "balcony", "elevator"), 30),

            createProperty(1002L, "Modern 2-bedroom apartment with sea view", "Barcelona, Spain", 280000.00,
                RealEstateProperty.PropertyTypeEnum.APARTMENT, 2, 1, 85.0,
                List.of("sea_view", "balcony", "air_conditioning", "elevator"), 15),

            createProperty(1003L, "Charming villa with pool in Valencia", "Valencia, Spain", 450000.00,
                RealEstateProperty.PropertyTypeEnum.VILLA, 4, 3, 200.0,
                List.of("pool", "garden", "parking", "fireplace", "terrace"), 10),

            createProperty(1004L, "Cozy 1-bedroom condo in Bilbao", "Bilbao, Spain", 180000.00,
                RealEstateProperty.PropertyTypeEnum.CONDO, 1, 1, 55.0,
                List.of("elevator", "balcony"), 5),

            createProperty(1005L, "Spacious townhouse with garage", "Seville, Spain", 320000.00,
                RealEstateProperty.PropertyTypeEnum.TOWNHOUSE, 3, 2, 140.0,
                List.of("garage", "terrace", "storage"), 20),

            createProperty(1006L, "Commercial space in city center", "Madrid, Spain", 550000.00,
                RealEstateProperty.PropertyTypeEnum.COMMERCIAL, null, 1, 95.0,
                List.of("elevator", "parking", "reception"), 7),

            createProperty(1007L, "Luxury penthouse with panoramic views", "Barcelona, Spain", 780000.00,
                RealEstateProperty.PropertyTypeEnum.APARTMENT, 3, 2, 160.0,
                List.of("penthouse", "panoramic_view", "terrace", "elevator", "parking"), 12)
        );
    }

    private RealEstateProperty createProperty(Long id, String title, String location, Double price,
            RealEstateProperty.PropertyTypeEnum type, Integer bedrooms, Integer bathrooms, Double area,
            List<String> features, int daysAgo) {
        return new RealEstateProperty()
                .id(id)
                .title(title)
                .description("Sample property description for " + title.toLowerCase())
                .location(location)
                .price(price)
                .propertyType(type)
                .bedrooms(bedrooms)
                .bathrooms(bathrooms)
                .area(area)
                .features(features)
                .createdAt(OffsetDateTime.now().minusDays(daysAgo))
                .updatedAt(OffsetDateTime.now().minusDays(daysAgo / 2));
    }
}