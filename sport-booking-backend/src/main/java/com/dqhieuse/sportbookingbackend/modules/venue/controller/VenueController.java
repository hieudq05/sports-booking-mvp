package com.dqhieuse.sportbookingbackend.modules.venue.controller;

import com.dqhieuse.sportbookingbackend.common.dto.ApiResponse;
import com.dqhieuse.sportbookingbackend.common.dto.PageResponse;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import com.dqhieuse.sportbookingbackend.modules.venue.dto.CreateVenueRequest;
import com.dqhieuse.sportbookingbackend.modules.venue.dto.VenueDetailResponse;
import com.dqhieuse.sportbookingbackend.modules.venue.dto.VenueSummaryResponse;
import com.dqhieuse.sportbookingbackend.modules.venue.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/venues")
@RequiredArgsConstructor
class VenueController {

    private static final Logger log = LoggerFactory.getLogger(VenueController.class);
    private final VenueService venueService;

    @PostMapping
    public ApiResponse<VenueDetailResponse> createVenue(
            @Valid @RequestBody CreateVenueRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        VenueDetailResponse createdVenue = venueService.createVenue(request, currentUser);

        return ApiResponse.created(createdVenue, "Venue created successfully");
    }

    @GetMapping
    public ApiResponse<PageResponse<VenueSummaryResponse>> getAllVenues(
            @PageableDefault(
                    size = 10,
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        PageResponse<VenueSummaryResponse> venues = venueService.findAllActiveVenues(pageable);

        return ApiResponse.success(venues, "Fetched all active venues successfully");
    }
}
