package com.dqhieuse.sportbookingbackend.modules.venue.service;

import com.dqhieuse.sportbookingbackend.common.exception.AppException;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.Role;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import com.dqhieuse.sportbookingbackend.modules.venue.dto.CourtDetailResponse;
import com.dqhieuse.sportbookingbackend.modules.venue.dto.UpdateCourtRequest;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.Court;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.CourtStatus;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.Venue;
import com.dqhieuse.sportbookingbackend.modules.venue.mapper.VenueMapper;
import com.dqhieuse.sportbookingbackend.modules.venue.repository.CourtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourtService {

    private final CourtRepository courtRepository;
    private final VenueMapper venueMapper;

    @Transactional
    public CourtDetailResponse updateCourt(Long courtId, UpdateCourtRequest request, User currentUser) {
        Court court = courtRepository.findById(courtId).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Court not found with id: " + courtId)
        );

        Venue venue = court.getVenue();

        boolean hasPermission = isHasPermission(currentUser, court, venue);

        if (!hasPermission) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "You are not authorized to update court");
        }

        venueMapper.updateCourtFromRequest(request, court);

        Court updatedCourt = courtRepository.save(court);

        return venueMapper.toCourtDetailResponse(updatedCourt);
    }

    @Transactional
    public void inActivateCourt(Long courtId, User currentUser) {
        Court court = courtRepository.findById(courtId).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Court not found with id: " + courtId)
        );

        Venue venue = court.getVenue();

        boolean hasPermission = isHasPermission(currentUser, court, venue);

        if (!hasPermission) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "You are not authorized to inActive court");
        }

        if (!court.getActive()) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "Court is already inactive");
        }

        court.setActive(false);
        courtRepository.save(court);
    }

    private static boolean isHasPermission(User currentUser, Court court, Venue venue) {
        boolean isVendor = currentUser.getRole() == Role.VENDOR;
        boolean isCourtOwner = court.getUser().getId().equals(currentUser.getId());
        boolean isVenueOwner = venue.getOwner().getId().equals(currentUser.getId());

        boolean hasPermission = (isCourtOwner || isVenueOwner) && isVendor;
        return hasPermission;
    }

    @Transactional
    public void activateCourt(Long courtId, User currentUser) {
        Court court = courtRepository.findById(courtId).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Court not found with id: " + courtId)
        );

        Venue venue = court.getVenue();

        boolean hasPermission = isHasPermission(currentUser, court, venue);

        if (!hasPermission) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "You are not authorized to active court");
        }

        if (court.getActive()) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "Court is already active");
        }

        court.setActive(true);
        courtRepository.save(court);
    }

    public CourtDetailResponse findCourtById(Long courtId) {
        Court court = courtRepository.findById(courtId).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "Court not found with id: " + courtId)
        );

        List<CourtStatus> allowedStatuses = List.of(CourtStatus.BOOKED, CourtStatus.AVAILABLE);

        if (!court.getActive() || court.getVenue().isDeleted() || !allowedStatuses.contains(court.getStatus())) {
            throw new AppException(HttpStatus.NOT_FOUND, "Court is not active or deleted");
        }

        return venueMapper.toCourtDetailResponse(court);
    }
}
