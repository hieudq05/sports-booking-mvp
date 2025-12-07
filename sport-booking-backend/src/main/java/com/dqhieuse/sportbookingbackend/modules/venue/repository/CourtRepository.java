package com.dqhieuse.sportbookingbackend.modules.venue.repository;

import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.Court;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.CourtStatus;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {
    Page<Court> findAllByUserAndVenue(User user, Venue venue, Pageable pageable);

    Page<Court> findAllByVenueAndActiveAndStatusIn(Venue venue, Boolean active, Collection<CourtStatus> statuses, Pageable pageable);
}