package com.springter.realestate.analyser.controller;

import com.springter.realestate.analyser.api.RealEstateApi;
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
            String sort,
            String location,
            Double minPrice,
            Double maxPrice,
            String propertyType) {

        log.info("Getting real estate properties - page: {}, size: {}, sort: {}, location: {}, minPrice: {}, maxPrice: {}, propertyType: {}",
                page, size, sort, location, minPrice, maxPrice, propertyType);

        // Create sample data for demonstration
        List<RealEstateProperty> sampleProperties = createSampleProperties();

        // Create paginated response
        RealEstatePageResponse response = new RealEstatePageResponse()
                .content(sampleProperties)
                .page(page)
                .size(size)
                .totalElements(150L)
                .totalPages(8)
                .first(page == 0)
                .last(page == 7)
                .numberOfElements(sampleProperties.size());

        return ResponseEntity.ok(response);
    }

    /**
     * Create sample real estate properties for demonstration
     */
    private List<RealEstateProperty> createSampleProperties() {
        RealEstateProperty property1 = new RealEstateProperty()
                .id(1001L)
                .title("Beautiful 3-bedroom house in downtown Madrid")
                .description("Spacious family home with modern amenities and great location")
                .location("Madrid, Spain")
                .price(350000.00)
                .propertyType(RealEstateProperty.PropertyTypeEnum.HOUSE)
                .bedrooms(3)
                .bathrooms(2)
                .area(120.5)
                .features(List.of("parking", "garden", "balcony", "elevator"))
                .createdAt(OffsetDateTime.now().minusDays(30))
                .updatedAt(OffsetDateTime.now().minusDays(5));

        RealEstateProperty property2 = new RealEstateProperty()
                .id(1002L)
                .title("Modern 2-bedroom apartment with sea view")
                .description("Luxury apartment with stunning views and premium finishes")
                .location("Barcelona, Spain")
                .price(280000.00)
                .propertyType(RealEstateProperty.PropertyTypeEnum.APARTMENT)
                .bedrooms(2)
                .bathrooms(1)
                .area(85.0)
                .features(List.of("sea_view", "balcony", "air_conditioning", "elevator"))
                .createdAt(OffsetDateTime.now().minusDays(15))
                .updatedAt(OffsetDateTime.now().minusDays(2));

        RealEstateProperty property3 = new RealEstateProperty()
                .id(1003L)
                .title("Charming villa with pool in Valencia")
                .description("Exclusive villa with private pool and garden in residential area")
                .location("Valencia, Spain")
                .price(450000.00)
                .propertyType(RealEstateProperty.PropertyTypeEnum.VILLA)
                .bedrooms(4)
                .bathrooms(3)
                .area(200.0)
                .features(List.of("pool", "garden", "parking", "fireplace", "terrace"))
                .createdAt(OffsetDateTime.now().minusDays(10))
                .updatedAt(OffsetDateTime.now().minusDays(1));

        return List.of(property1, property2, property3);
    }
}