package com.springter.realestate.analyser.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Table(name = "rating_analysis", schema = "public")
public class RatingAnalysisJpa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "analysis_id", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "house_id", nullable = false)
  private HouseJpa house;

  @Column(name = "overall_score", precision = 3, scale = 2, nullable = false)
  private BigDecimal overallScore;

  @Column(name = "user_rating_count")
  private Integer userRatingCount = 0;

  @Column(name = "price_to_sqft_ratio", precision = 10, scale = 2)
  private BigDecimal priceToSqftRatio;

  @Column(name = "market_comp_score", precision = 3, scale = 2)
  private BigDecimal marketCompScore;

  @Column(name = "last_sold_price", precision = 15, scale = 2)
  private BigDecimal lastSoldPrice;

  @Column(name = "time_on_market_days")
  private Integer timeOnMarketDays;

  @Column(name = "rating_timestamp")
  private OffsetDateTime ratingTimestamp;

}