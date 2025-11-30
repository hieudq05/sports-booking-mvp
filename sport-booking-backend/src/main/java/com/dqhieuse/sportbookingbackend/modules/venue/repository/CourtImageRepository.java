package com.dqhieuse.sportbookingbackend.modules.venue.repository;

import com.dqhieuse.sportbookingbackend.modules.venue.entity.CourtImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtImageRepository extends JpaRepository<CourtImage, Long> {
}