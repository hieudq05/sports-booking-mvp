package com.dqhieuse.sportbookingbackend.modules.venue.entity;

import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "venues")
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

    @Column(length = 20, nullable = false)
    private String status;

}