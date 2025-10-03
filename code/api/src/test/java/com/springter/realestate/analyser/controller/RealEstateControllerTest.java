package com.springter.realestate.analyser.controller;

import com.springter.realestate.analyser.domain.common.Page;
import com.springter.realestate.analyser.domain.common.PageRequest;
import com.springter.realestate.analyser.domain.realestate.RealEstateProperty;
import com.springter.realestate.analyser.domain.realestate.RealEstateSearchCriteria;
import com.springter.realestate.analyser.domain.usecases.FindRealEstatePropertiesUseCase;
import com.springter.realestate.analyser.mapper.RealEstateMapper;
import com.springter.realestate.analyser.model.RealEstatePageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RealEstateController Tests")
class RealEstateControllerTest {

    @Mock
    private FindRealEstatePropertiesUseCase findPropertiesUseCase;
    
    @Mock
    private RealEstateMapper mapper;
    
    private RealEstateController controller;

    @BeforeEach
    void setUp() {
        controller = new RealEstateController(findPropertiesUseCase, mapper);
    }

    @Nested
    @DisplayName("GET /realestate - getAllRealEstate")
    class GetAllRealEstateTests {

        @Test
        @DisplayName("Should return paginated response with default parameters")
        void shouldReturnPaginatedResponseWithDefaults() {
            // Given
            RealEstateSearchCriteria expectedCriteria = RealEstateSearchCriteria.builder().build();
            Page<RealEstateProperty> mockPage = Page.of(
                List.of(createSampleDomainProperty()), 
                PageRequest.of(0, 20), 
                1L
            );
            RealEstatePageResponse expectedResponse = new RealEstatePageResponse()
                .page(0)
                .size(20)
                .totalElements(1L)
                .totalPages(1)
                .numberOfElements(1)
                .content(List.of(createSampleDtoProperty()));
            
            when(mapper.toSearchCriteria(null, null, null, null)).thenReturn(expectedCriteria);
            when(findPropertiesUseCase.findProperties(any(RealEstateSearchCriteria.class), any(PageRequest.class)))
                .thenReturn(mockPage);
            when(mapper.toPageResponse(any(Page.class))).thenReturn(expectedResponse);
            
            // When
            ResponseEntity<RealEstatePageResponse> response = controller.getAllRealEstate(
                null, null, null, null, null, null
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            RealEstatePageResponse pageResponse = response.getBody();
            assertThat(pageResponse.getPage()).isEqualTo(0);
            assertThat(pageResponse.getSize()).isEqualTo(20);
            assertThat(pageResponse.getTotalElements()).isEqualTo(1L);
            assertThat(pageResponse.getTotalPages()).isEqualTo(1);
            assertThat(pageResponse.getNumberOfElements()).isEqualTo(1);
            assertThat(pageResponse.getContent()).isNotNull().hasSize(1);
        }

        @Test
        @DisplayName("Should handle custom page and size parameters")
        void shouldHandleCustomPageAndSizeParameters() {
            // Given
            RealEstatePageResponse mockResponse = new RealEstatePageResponse()
                .page(0).size(3).totalElements(7L).totalPages(3).numberOfElements(3)
                .content(List.of());
            
            when(mapper.toSearchCriteria(null, null, null, null))
                .thenReturn(RealEstateSearchCriteria.builder().build());
            when(findPropertiesUseCase.findProperties(any(), any()))
                .thenReturn(Page.of(List.of(), PageRequest.of(0, 3), 7L));
            when(mapper.toPageResponse(any())).thenReturn(mockResponse);
            
            // When
            ResponseEntity<RealEstatePageResponse> response = controller.getAllRealEstate(
                0, 3, null, null, null, null
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(mockResponse);
        }

        @Test
        @DisplayName("Should handle second page correctly")
        void shouldHandleSecondPageCorrectly() {

            ResponseEntity<RealEstatePageResponse> response = controller.getAllRealEstate(
                1, 3, null, null, null, null
            );


            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            RealEstatePageResponse pageResponse = response.getBody();

            assertThat(pageResponse.getPage()).isEqualTo(1);
            assertThat(pageResponse.getSize()).isEqualTo(3);
            assertThat(pageResponse.getTotalElements()).isEqualTo(7L);
            assertThat(pageResponse.getTotalPages()).isEqualTo(3);
            assertThat(pageResponse.getNumberOfElements()).isEqualTo(3);
            assertThat(pageResponse.getContent()).hasSize(3);
        }

        @Test
        @DisplayName("Should handle last page with fewer elements")
        void shouldHandleLastPageWithFewerElements() {
            ResponseEntity<RealEstatePageResponse> response = controller.getAllRealEstate(
                2, 3, null, null, null, null
            );


            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            RealEstatePageResponse pageResponse = response.getBody();

            assertThat(pageResponse.getPage()).isEqualTo(2);
            assertThat(pageResponse.getSize()).isEqualTo(3);
            assertThat(pageResponse.getTotalElements()).isEqualTo(7L);
            assertThat(pageResponse.getTotalPages()).isEqualTo(3);
            assertThat(pageResponse.getNumberOfElements()).isEqualTo(1);
            assertThat(pageResponse.getContent()).hasSize(1);
        }

        @Test
        @DisplayName("Should handle page beyond available data")
        void shouldHandlePageBeyondAvailableData() {
            ResponseEntity<RealEstatePageResponse> response = controller.getAllRealEstate(
                10, 3, null, null, null, null
            );


            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            RealEstatePageResponse pageResponse = response.getBody();

            assertThat(pageResponse.getPage()).isEqualTo(10);
            assertThat(pageResponse.getSize()).isEqualTo(3);
            assertThat(pageResponse.getTotalElements()).isEqualTo(7L);
            assertThat(pageResponse.getTotalPages()).isEqualTo(3);
            assertThat(pageResponse.getNumberOfElements()).isZero();
            assertThat(pageResponse.getContent()).isEmpty();
        }

        @Test
        @DisplayName("Should accept filter parameters without error")
        void shouldAcceptFilterParametersWithoutError() {
            ResponseEntity<RealEstatePageResponse> response = controller.getAllRealEstate(
                0, 10, "Madrid", 200000.0, 400000.0, "HOUSE"
            );


            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            RealEstatePageResponse pageResponse = response.getBody();
            assertThat(pageResponse.getContent()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Property Data Validation")
    class PropertyDataValidationTests {

        @Test
        @DisplayName("Should return properties with all required fields")
        void shouldReturnPropertiesWithAllRequiredFields() {

            ResponseEntity<RealEstatePageResponse> response = controller.getAllRealEstate(
                0, 10, null, null, null, null
            );


            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getContent()).isNotEmpty();

            response.getBody().getContent().forEach(property -> {
                assertThat(property.getId()).isNotNull().isPositive();
                assertThat(property.getTitle()).isNotBlank();
                assertThat(property.getLocation()).isNotBlank();
                assertThat(property.getPrice()).isNotNull().isPositive();
                assertThat(property.getPropertyType()).isNotNull();
                assertThat(property.getCreatedAt()).isNotNull();
            });
        }

        @Test
        @DisplayName("Should return diverse property types")
        void shouldReturnDiversePropertyTypes() {

            ResponseEntity<RealEstatePageResponse> response = controller.getAllRealEstate(
                0, 10, null, null, null, null
            );


            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getContent())
                .extracting(RealEstateProperty::getPropertyType)
                .containsExactlyInAnyOrder(
                    RealEstateProperty.PropertyTypeEnum.HOUSE,
                    RealEstateProperty.PropertyTypeEnum.APARTMENT,
                    RealEstateProperty.PropertyTypeEnum.VILLA,
                    RealEstateProperty.PropertyTypeEnum.CONDO,
                    RealEstateProperty.PropertyTypeEnum.TOWNHOUSE,
                    RealEstateProperty.PropertyTypeEnum.COMMERCIAL,
                    RealEstateProperty.PropertyTypeEnum.APARTMENT
                );
        }

        @Test
        @DisplayName("Should return properties with different locations")
        void shouldReturnPropertiesWithDifferentLocations() {

            ResponseEntity<RealEstatePageResponse> response = controller.getAllRealEstate(
                0, 10, null, null, null, null
            );


            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getContent())
                .extracting(RealEstateProperty::getLocation)
                .containsExactlyInAnyOrder(
                    "Madrid, Spain",
                    "Barcelona, Spain",
                    "Valencia, Spain",
                    "Bilbao, Spain",
                    "Seville, Spain",
                    "Madrid, Spain",
                    "Barcelona, Spain"
                );
        }

        @Test
        @DisplayName("Should return properties with varying prices")
        void shouldReturnPropertiesWithVaryingPrices() {

            ResponseEntity<RealEstatePageResponse> response = controller.getAllRealEstate(
                0, 10, null, null, null, null
            );


            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getContent())
                .extracting(RealEstateProperty::getPrice)
                .containsExactlyInAnyOrder(
                    350000.00, 280000.00, 450000.00, 180000.00,
                    320000.00, 550000.00, 780000.00
                )
                .allMatch(price -> price >= 180000.00 && price <= 780000.00);
        }

        @Test
        @DisplayName("Should return properties with features")
        void shouldReturnPropertiesWithFeatures() {

            ResponseEntity<RealEstatePageResponse> response = controller.getAllRealEstate(
                0, 10, null, null, null, null
            );


            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getContent())
                .allMatch(property -> property.getFeatures() != null && !property.getFeatures().isEmpty())
                .extracting(RealEstateProperty::getFeatures)
                .allMatch(features -> features.size() >= 2);
        }

        @Nested
        @DisplayName("Edge Cases")
        class EdgeCasesTests {

            @Test
            @DisplayName("Should handle null parameters gracefully")
            void shouldHandleNullParametersGracefully() {

                ResponseEntity<RealEstatePageResponse> response = controller.getAllRealEstate(
                    null, null, null, null, null, null
                );


                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getPage()).isEqualTo(0);
                assertThat(response.getBody().getSize()).isEqualTo(20);
            }

            @Test
            @DisplayName("Should handle zero page parameter")
            void shouldHandleZeroPageParameter() {

                ResponseEntity<RealEstatePageResponse> response = controller.getAllRealEstate(
                    0, 5, null, null, null, null
                );


                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getPage()).isZero();
            }

            @Test
            @DisplayName("Should handle large page size")
            void shouldHandleLargePageSize() {

                ResponseEntity<RealEstatePageResponse> response = controller.getAllRealEstate(
                    0, 100, null, null, null, null
                );

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getSize()).isEqualTo(100);
                assertThat(response.getBody().getNumberOfElements()).isEqualTo(7);
                assertThat(response.getBody().getContent()).hasSize(7);
            }
        }
    }
    
    // Helper methods for creating test objects
    private RealEstateProperty createSampleDomainProperty() {
        return RealEstateProperty.builder()
            .id(1L)
            .title("Test Property")
            .description("A test property")
            .location("Test Location")
            .price(100000.0)
            .propertyType(RealEstateProperty.PropertyType.HOUSE)
            .bedrooms(3)
            .bathrooms(2)
            .area(120.0)
            .features(List.of("parking", "garden"))
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();
    }
    
    private com.springter.realestate.analyser.model.RealEstateProperty createSampleDtoProperty() {
        return new com.springter.realestate.analyser.model.RealEstateProperty()
            .id(1L)
            .title("Test Property")
            .description("A test property")
            .location("Test Location")
            .price(100000.0)
            .propertyType(com.springter.realestate.analyser.model.RealEstateProperty.PropertyTypeEnum.HOUSE)
            .bedrooms(3)
            .bathrooms(2)
            .area(120.0)
            .features(List.of("parking", "garden"))
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now());
    }
}
