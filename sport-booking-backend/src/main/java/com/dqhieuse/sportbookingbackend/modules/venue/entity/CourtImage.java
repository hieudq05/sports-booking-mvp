package com.dqhieuse.sportbookingbackend.modules.venue.entity;

import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "court_images")
public class CourtImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}