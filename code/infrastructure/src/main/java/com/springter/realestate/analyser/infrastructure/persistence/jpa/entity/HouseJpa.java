package com.springter.realestate.analyser.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "house", schema = "public")
public class HouseJpa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "house_id", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "location_id", nullable = false)
  private LocationJpa location;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Column(name = "listing_status", nullable = false, length = 50)
  private String listingStatus;

  @Column(name = "year_built")
  private Integer yearBuilt;

  @Column(name = "square_footage")
  private Integer squareFootage;

  @Column(name = "num_bedrooms")
  private Integer numBedrooms;

  @Column(name = "num_bathrooms", precision = 2, scale = 1)
  private BigDecimal numBathrooms;

  @Column(name = "property_type", length = 50)
  private String propertyType;

  @Column(name = "heating_type", length = 50)
  private String heatingType;

  @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RatingAnalysisJpa> ratingAnalyses;

}