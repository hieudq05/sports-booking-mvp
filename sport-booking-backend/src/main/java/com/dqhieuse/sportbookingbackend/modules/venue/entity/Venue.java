package com.dqhieuse.sportbookingbackend.modules.venue.entity;

import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "venues")
@FilterDef(
        name = "deletedVenueFilter",
        parameters = @ParamDef(name = "isDeleted", type = Boolean.class)
)
@Filter(
        name = "deletedVenueFilter",
        condition = "is_deleted = :isDeleted"
)
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(length = 50)
    private String district;

    @Column
    private String description;

    @Column(nullable = false)
    private String thumbnail;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private VenueStatus status;

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Builder.Default
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Court> courts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VenueImage> images = new ArrayList<>();

    public void addCourt(Court court) {
        this.courts.add(court);
        court.setVenue(this);
    }

    public void addImage(VenueImage venueImage) {
        images.add(venueImage);
        venueImage.setVenue(this);
    }

    public void removeCourt(Court court) {
        this.courts.remove(court);
        court.setVenue(null);
    }

    public void removeImage(VenueImage venueImage) {
        this.images.remove(venueImage);
        venueImage.setVenue(null);
    }
}