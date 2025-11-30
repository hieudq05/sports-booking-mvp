package com.dqhieuse.sportbookingbackend.modules.venue.service;

import com.dqhieuse.sportbookingbackend.common.dto.MetaResponse;
import com.dqhieuse.sportbookingbackend.common.dto.PageResponse;
import com.dqhieuse.sportbookingbackend.common.exception.AppException;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.Role;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import com.dqhieuse.sportbookingbackend.modules.venue.dto.CreateVenueRequest;
import com.dqhieuse.sportbookingbackend.modules.venue.dto.VenueDetailResponse;
import com.dqhieuse.sportbookingbackend.modules.venue.dto.VenueSummaryResponse;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.Venue;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.VenueStatus;
import com.dqhieuse.sportbookingbackend.modules.venue.mapper.VenueMapper;
import com.dqhieuse.sportbookingbackend.modules.venue.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueMapper venueMapper;
    private final VenueRepository venueRepository;

    @Transactional
    public VenueDetailResponse createVenue(CreateVenueRequest request, User currentUser) {
        if (currentUser.getRole() != Role.VENDOR) {
            throw new AppException(HttpStatus.NOT_ACCEPTABLE, "You are not authorized to create venue");
        }

        Venue venue = venueMapper.toEntity(request);

        venue.setOwner(currentUser);
        venue.setStatus(VenueStatus.PENDING);

        Venue savedVenue = venueRepository.save(venue);

        return venueMapper.toDetailResponse(savedVenue);
    }

    public PageResponse<VenueSummaryResponse> findAllActiveVenues(Pageable pageable) {
        Page<Venue> activeVenues = venueRepository.findAllByStatus(VenueStatus.ACTIVE, pageable);

        List<VenueSummaryResponse> venueSummaries = venueMapper.toSummaryResponseList(activeVenues.getContent());

        MetaResponse meta = MetaResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElements(activeVenues.getTotalElements())
                .totalPages(activeVenues.getTotalPages())
                .build();

        return PageResponse.<VenueSummaryResponse>builder()
                .content(venueSummaries)
                .meta(meta)
                .build();
    }
}
