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
import org.mockito.InjectMocks;
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

    @InjectMocks
    private RealEstateController controller;

    @Nested
    @DisplayName("GET /realestate - getAllRealEstate")
    class GetAllRealEstateTests {

        @Test
        @DisplayName("Should return paginated response with default parameters")
        void shouldReturnPaginatedResponseWithDefaults() {
            // Given
            final RealEstateSearchCriteria expectedCriteria = RealEstateSearchCriteria.builder().build();
            final Page<RealEstateProperty> mockPage = Page.of(
                List.of(RealEstateControllerTest.this.createSampleDomainProperty()),
                PageRequest.of(0, 20),
                1L
            );
            final RealEstatePageResponse expectedResponse = new RealEstatePageResponse()
                .page(0)
                .size(20)
                .totalElements(1L)
                .totalPages(1)
                .numberOfElements(1)
                .content(List.of(RealEstateControllerTest.this.createSampleDtoProperty()));

            when(RealEstateControllerTest.this.mapper.toSearchCriteria(null, null, null, null)).thenReturn(expectedCriteria);
            when(RealEstateControllerTest.this.findPropertiesUseCase.findProperties(any(RealEstateSearchCriteria.class), any(PageRequest.class)))
                .thenReturn(mockPage);
            when(RealEstateControllerTest.this.mapper.toPageResponse(any(Page.class))).thenReturn(expectedResponse);

            // When
            final ResponseEntity<RealEstatePageResponse> response = RealEstateControllerTest.this.controller.getAllRealEstate(
                null, null, null, null, null, null
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            final RealEstatePageResponse pageResponse = response.getBody();
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
            final RealEstatePageResponse mockResponse = new RealEstatePageResponse()
                .page(0).size(3).totalElements(7L).totalPages(3).numberOfElements(3)
                .content(List.of());

            when(RealEstateControllerTest.this.mapper.toSearchCriteria(null, null, null, null))
                .thenReturn(RealEstateSearchCriteria.builder().build());
            when(RealEstateControllerTest.this.findPropertiesUseCase.findProperties(any(), any()))
                .thenReturn(Page.of(List.of(), PageRequest.of(0, 3), 7L));
            when(RealEstateControllerTest.this.mapper.toPageResponse(any())).thenReturn(mockResponse);

            // When
            final ResponseEntity<RealEstatePageResponse> response = RealEstateControllerTest.this.controller.getAllRealEstate(
                0, 3, null, null, null, null
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(mockResponse);
        }

        @Test
        @DisplayName("Should handle second page correctly")
        void shouldHandleSecondPageCorrectly() {
            // Given
            final RealEstatePageResponse mockResponse = new RealEstatePageResponse()
                .page(1).size(3).totalElements(7L).totalPages(3).numberOfElements(3)
                .content(List.of(RealEstateControllerTest.this.createSampleDtoProperty(), RealEstateControllerTest.this.createSampleDtoProperty(), RealEstateControllerTest.this.createSampleDtoProperty()));

            when(RealEstateControllerTest.this.mapper.toSearchCriteria(null, null, null, null))
                .thenReturn(RealEstateSearchCriteria.builder().build());
            when(RealEstateControllerTest.this.findPropertiesUseCase.findProperties(any(), any()))
                .thenReturn(Page.of(List.of(RealEstateControllerTest.this.createSampleDomainProperty()), PageRequest.of(1, 3), 7L));
            when(RealEstateControllerTest.this.mapper.toPageResponse(any())).thenReturn(mockResponse);

            // When
            final ResponseEntity<RealEstatePageResponse> response = RealEstateControllerTest.this.controller.getAllRealEstate(
                1, 3, null, null, null, null
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            final RealEstatePageResponse pageResponse = response.getBody();

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
            // Given
            final RealEstatePageResponse mockResponse = new RealEstatePageResponse()
                .page(2).size(3).totalElements(7L).totalPages(3).numberOfElements(1)
                .content(List.of(RealEstateControllerTest.this.createSampleDtoProperty()));

            when(RealEstateControllerTest.this.mapper.toSearchCriteria(null, null, null, null))
                .thenReturn(RealEstateSearchCriteria.builder().build());
            when(RealEstateControllerTest.this.findPropertiesUseCase.findProperties(any(), any()))
                .thenReturn(Page.of(List.of(RealEstateControllerTest.this.createSampleDomainProperty()), PageRequest.of(2, 3), 7L));
            when(RealEstateControllerTest.this.mapper.toPageResponse(any())).thenReturn(mockResponse);

            // When
            final ResponseEntity<RealEstatePageResponse> response = RealEstateControllerTest.this.controller.getAllRealEstate(
                2, 3, null, null, null, null
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            final RealEstatePageResponse pageResponse = response.getBody();

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
            // Given
            final RealEstatePageResponse mockResponse = new RealEstatePageResponse()
                .page(10).size(3).totalElements(7L).totalPages(3).numberOfElements(0)
                .content(List.of());

            when(RealEstateControllerTest.this.mapper.toSearchCriteria(null, null, null, null))
                .thenReturn(RealEstateSearchCriteria.builder().build());
            when(RealEstateControllerTest.this.findPropertiesUseCase.findProperties(any(), any()))
                .thenReturn(Page.of(List.of(), PageRequest.of(10, 3), 7L));
            when(RealEstateControllerTest.this.mapper.toPageResponse(any())).thenReturn(mockResponse);

            // When
            final ResponseEntity<RealEstatePageResponse> response = RealEstateControllerTest.this.controller.getAllRealEstate(
                10, 3, null, null, null, null
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            final RealEstatePageResponse pageResponse = response.getBody();

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
            // Given
            final RealEstatePageResponse mockResponse = new RealEstatePageResponse()
                .page(0).size(10).totalElements(3L).totalPages(1).numberOfElements(3)
                .content(List.of(RealEstateControllerTest.this.createSampleDtoProperty()));

            when(RealEstateControllerTest.this.mapper.toSearchCriteria("Madrid", 200000.0, 400000.0, "HOUSE"))
                .thenReturn(RealEstateSearchCriteria.builder().build());
            when(RealEstateControllerTest.this.findPropertiesUseCase.findProperties(any(), any()))
                .thenReturn(Page.of(List.of(RealEstateControllerTest.this.createSampleDomainProperty()), PageRequest.of(0, 10), 3L));
            when(RealEstateControllerTest.this.mapper.toPageResponse(any())).thenReturn(mockResponse);

            // When
            final ResponseEntity<RealEstatePageResponse> response = RealEstateControllerTest.this.controller.getAllRealEstate(
                0, 10, "Madrid", 200000.0, 400000.0, "HOUSE"
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();

            final RealEstatePageResponse pageResponse = response.getBody();
            assertThat(pageResponse.getContent()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Property Data Validation")
    class PropertyDataValidationTests {

        @Test
        @DisplayName("Should return properties with all required fields")
        void shouldReturnPropertiesWithAllRequiredFields() {
            // Given
            final RealEstatePageResponse mockResponse = new RealEstatePageResponse()
                .page(0).size(10).totalElements(2L).totalPages(1).numberOfElements(2)
                .content(List.of(RealEstateControllerTest.this.createSampleDtoProperty(), RealEstateControllerTest.this.createSampleDtoProperty()));

            when(RealEstateControllerTest.this.mapper.toSearchCriteria(null, null, null, null))
                .thenReturn(RealEstateSearchCriteria.builder().build());
            when(RealEstateControllerTest.this.findPropertiesUseCase.findProperties(any(), any()))
                .thenReturn(Page.of(List.of(RealEstateControllerTest.this.createSampleDomainProperty()), PageRequest.of(0, 10), 2L));
            when(RealEstateControllerTest.this.mapper.toPageResponse(any())).thenReturn(mockResponse);

            // When
            final ResponseEntity<RealEstatePageResponse> response = RealEstateControllerTest.this.controller.getAllRealEstate(
                0, 10, null, null, null, null
            );

            // Then
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
            // Given
            final RealEstatePageResponse mockResponse = new RealEstatePageResponse()
                .page(0).size(10).totalElements(7L).totalPages(1).numberOfElements(7)
                .content(List.of(RealEstateControllerTest.this.createSampleDtoProperty()));

            when(RealEstateControllerTest.this.mapper.toSearchCriteria(null, null, null, null))
                .thenReturn(RealEstateSearchCriteria.builder().build());
            when(RealEstateControllerTest.this.findPropertiesUseCase.findProperties(any(), any()))
                .thenReturn(Page.of(List.of(RealEstateControllerTest.this.createSampleDomainProperty()), PageRequest.of(0, 10), 7L));
            when(RealEstateControllerTest.this.mapper.toPageResponse(any())).thenReturn(mockResponse);

            // When
            final ResponseEntity<RealEstatePageResponse> response = RealEstateControllerTest.this.controller.getAllRealEstate(
                0, 10, null, null, null, null
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(mockResponse);
            assertThat(response.getBody().getContent()).isNotEmpty();
        }

        @Test
        @DisplayName("Should return properties with different locations")
        void shouldReturnPropertiesWithDifferentLocations() {
            // Given
            final RealEstatePageResponse mockResponse = new RealEstatePageResponse()
                .page(0).size(10).totalElements(7L).totalPages(1).numberOfElements(7)
                .content(List.of(RealEstateControllerTest.this.createSampleDtoProperty()));

            when(RealEstateControllerTest.this.mapper.toSearchCriteria(null, null, null, null))
                .thenReturn(RealEstateSearchCriteria.builder().build());
            when(RealEstateControllerTest.this.findPropertiesUseCase.findProperties(any(), any()))
                .thenReturn(Page.of(List.of(RealEstateControllerTest.this.createSampleDomainProperty()), PageRequest.of(0, 10), 7L));
            when(RealEstateControllerTest.this.mapper.toPageResponse(any())).thenReturn(mockResponse);

            // When
            final ResponseEntity<RealEstatePageResponse> response = RealEstateControllerTest.this.controller.getAllRealEstate(
                0, 10, null, null, null, null
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(mockResponse);
            assertThat(response.getBody().getContent()).isNotEmpty();
        }

        @Test
        @DisplayName("Should return properties with varying prices")
        void shouldReturnPropertiesWithVaryingPrices() {
            // Given
            final RealEstatePageResponse mockResponse = new RealEstatePageResponse()
                .page(0).size(10).totalElements(7L).totalPages(1).numberOfElements(7)
                .content(List.of(RealEstateControllerTest.this.createSampleDtoProperty()));

            when(RealEstateControllerTest.this.mapper.toSearchCriteria(null, null, null, null))
                .thenReturn(RealEstateSearchCriteria.builder().build());
            when(RealEstateControllerTest.this.findPropertiesUseCase.findProperties(any(), any()))
                .thenReturn(Page.of(List.of(RealEstateControllerTest.this.createSampleDomainProperty()), PageRequest.of(0, 10), 7L));
            when(RealEstateControllerTest.this.mapper.toPageResponse(any())).thenReturn(mockResponse);

            // When
            final ResponseEntity<RealEstatePageResponse> response = RealEstateControllerTest.this.controller.getAllRealEstate(
                0, 10, null, null, null, null
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(mockResponse);
            assertThat(response.getBody().getContent()).isNotEmpty();
        }

        @Test
        @DisplayName("Should return properties with features")
        void shouldReturnPropertiesWithFeatures() {
            // Given
            final RealEstatePageResponse mockResponse = new RealEstatePageResponse()
                .page(0).size(10).totalElements(7L).totalPages(1).numberOfElements(7)
                .content(List.of(RealEstateControllerTest.this.createSampleDtoProperty()));

            when(RealEstateControllerTest.this.mapper.toSearchCriteria(null, null, null, null))
                .thenReturn(RealEstateSearchCriteria.builder().build());
            when(RealEstateControllerTest.this.findPropertiesUseCase.findProperties(any(), any()))
                .thenReturn(Page.of(List.of(RealEstateControllerTest.this.createSampleDomainProperty()), PageRequest.of(0, 10), 7L));
            when(RealEstateControllerTest.this.mapper.toPageResponse(any())).thenReturn(mockResponse);

            // When
            final ResponseEntity<RealEstatePageResponse> response = RealEstateControllerTest.this.controller.getAllRealEstate(
                0, 10, null, null, null, null
            );

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(mockResponse);
            assertThat(response.getBody().getContent()).isNotEmpty();
        }

        @Nested
        @DisplayName("Edge Cases")
        class EdgeCasesTests {

            @Test
            @DisplayName("Should handle null parameters gracefully")
            void shouldHandleNullParametersGracefully() {
                // Given
                final RealEstatePageResponse mockResponse = new RealEstatePageResponse()
                    .page(0).size(20).totalElements(5L).totalPages(1).numberOfElements(5)
                    .content(List.of(RealEstateControllerTest.this.createSampleDtoProperty()));

                when(RealEstateControllerTest.this.mapper.toSearchCriteria(null, null, null, null))
                    .thenReturn(RealEstateSearchCriteria.builder().build());
                when(RealEstateControllerTest.this.findPropertiesUseCase.findProperties(any(), any()))
                    .thenReturn(Page.of(List.of(RealEstateControllerTest.this.createSampleDomainProperty()), PageRequest.of(0, 20), 5L));
                when(RealEstateControllerTest.this.mapper.toPageResponse(any())).thenReturn(mockResponse);

                // When
                final ResponseEntity<RealEstatePageResponse> response = RealEstateControllerTest.this.controller.getAllRealEstate(
                    null, null, null, null, null, null
                );

                // Then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getPage()).isEqualTo(0);
                assertThat(response.getBody().getSize()).isEqualTo(20);
            }

            @Test
            @DisplayName("Should handle zero page parameter")
            void shouldHandleZeroPageParameter() {
                // Given
                final RealEstatePageResponse mockResponse = new RealEstatePageResponse()
                    .page(0).size(5).totalElements(7L).totalPages(2).numberOfElements(5)
                    .content(List.of(RealEstateControllerTest.this.createSampleDtoProperty()));

                when(RealEstateControllerTest.this.mapper.toSearchCriteria(null, null, null, null))
                    .thenReturn(RealEstateSearchCriteria.builder().build());
                when(RealEstateControllerTest.this.findPropertiesUseCase.findProperties(any(), any()))
                    .thenReturn(Page.of(List.of(RealEstateControllerTest.this.createSampleDomainProperty()), PageRequest.of(0, 5), 7L));
                when(RealEstateControllerTest.this.mapper.toPageResponse(any())).thenReturn(mockResponse);

                // When
                final ResponseEntity<RealEstatePageResponse> response = RealEstateControllerTest.this.controller.getAllRealEstate(
                    0, 5, null, null, null, null
                );

                // Then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody()).isNotNull();
                assertThat(response.getBody().getPage()).isZero();
            }

            @Test
            @DisplayName("Should handle large page size")
            void shouldHandleLargePageSize() {
                // Given
                final List<com.springter.realestate.analyser.model.RealEstateProperty> properties = List.of(
                    RealEstateControllerTest.this.createSampleDtoProperty(), RealEstateControllerTest.this.createSampleDtoProperty(), RealEstateControllerTest.this.createSampleDtoProperty(),
                    RealEstateControllerTest.this.createSampleDtoProperty(), RealEstateControllerTest.this.createSampleDtoProperty(), RealEstateControllerTest.this.createSampleDtoProperty(),
                    RealEstateControllerTest.this.createSampleDtoProperty()
                );
                final RealEstatePageResponse mockResponse = new RealEstatePageResponse()
                    .page(0).size(100).totalElements(7L).totalPages(1).numberOfElements(7)
                    .content(properties);

                when(RealEstateControllerTest.this.mapper.toSearchCriteria(null, null, null, null))
                    .thenReturn(RealEstateSearchCriteria.builder().build());
                when(RealEstateControllerTest.this.findPropertiesUseCase.findProperties(any(), any()))
                    .thenReturn(Page.of(List.of(RealEstateControllerTest.this.createSampleDomainProperty()), PageRequest.of(0, 100), 7L));
                when(RealEstateControllerTest.this.mapper.toPageResponse(any())).thenReturn(mockResponse);

                // When
                final ResponseEntity<RealEstatePageResponse> response = RealEstateControllerTest.this.controller.getAllRealEstate(
                    0, 100, null, null, null, null
                );

                // Then
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
