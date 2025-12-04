package com.dqhieuse.sportbookingbackend.modules.venue.repository;

import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.Venue;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.VenueStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    Page<Venue> findAllByStatus(VenueStatus status, Pageable pageable);

    Optional<Venue> findByIdAndStatusIn(Long id, Collection<VenueStatus> statuses);

    Page<Venue> findAllByOwner(User owner, Pageable pageable);
}