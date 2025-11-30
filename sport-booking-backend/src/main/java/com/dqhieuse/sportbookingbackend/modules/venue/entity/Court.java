package com.dqhieuse.sportbookingbackend.modules.venue.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "courts")
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20, nullable = false)
    private CourtType type;

    @Column(name = "price_per_hour", nullable = false)
    private BigDecimal pricePerHour;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CourtStatus status;

    @Version
    private Long version;

    @Column(name = "active", nullable = false)
    private Boolean active;
}