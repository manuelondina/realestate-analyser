package com.springter.realestate.analyser.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "location", schema = "public")
public class LocationJpa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "location_id", nullable = false)
  private Integer id;

  @Column(name = "street_address", nullable = false, length = 255)
  private String streetAddress;

  @Column(name = "city", nullable = false, length = 100)
  private String city;

  @Column(name = "state_province", length = 100)
  private String stateProvince;

  @Column(name = "zip_postal_code", length = 20)
  private String zipPostalCode;

  @Column(name = "latitude", precision = 10, scale = 8)
  private BigDecimal latitude;

  @Column(name = "longitude", precision = 11, scale = 8)
  private BigDecimal longitude;

  @Column(name = "school_rating_avg", precision = 2, scale = 1)
  private BigDecimal schoolRatingAvg;

  @Column(name = "walk_score")
  private Integer walkScore;

  @Column(name = "transit_score")
  private Integer transitScore;

  @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<HouseJpa> houses;

}