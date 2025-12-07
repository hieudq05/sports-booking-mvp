package com.dqhieuse.sportbookingbackend.modules.venue.controller;

import com.dqhieuse.sportbookingbackend.common.dto.ApiResponse;
import com.dqhieuse.sportbookingbackend.common.dto.PageResponse;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import com.dqhieuse.sportbookingbackend.modules.venue.dto.*;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.Court;
import com.dqhieuse.sportbookingbackend.modules.venue.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/venues")
@RequiredArgsConstructor
class VenueController {

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
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        PageResponse<VenueSummaryResponse> venues = venueService.findAllActiveVenues(pageable);

        return ApiResponse.success(venues, "Fetched all active venues successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<VenueDetailResponse> getVenueById(@PathVariable Long id) {
        VenueDetailResponse venue = venueService.findVenueDetailById(id);

        return ApiResponse.success(venue, "Fetched venue successfully");
    }

    @PutMapping("/{id}")
    public ApiResponse<VenueDetailResponse> updateVenue(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVenueRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        VenueDetailResponse updatedVenue = venueService.updateVenue(id, request, currentUser);

        return ApiResponse.success(updatedVenue, "Venue updated successfully");
    }

    @PostMapping("/{id}/images")
    public ApiResponse<VenueDetailResponse> addImageToVenue(
            @PathVariable Long id,
            @Valid @RequestBody AddVenueImagesRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        VenueDetailResponse updatedVenue = venueService.addImageToVenue(id, request, currentUser);

        return ApiResponse.success(updatedVenue, "Venue image added successfully");
    }

    @GetMapping("/my-venues")
    public ApiResponse<PageResponse<VenueSummaryResponse>> getAllVenuesByOwner(
            @AuthenticationPrincipal User currentUser,
            @PageableDefault(
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        PageResponse<VenueSummaryResponse> venues = venueService.findAllVenuesForVendor(pageable, currentUser);

        return ApiResponse.success(venues, "Fetched all venues successfully");
    }

    @PostMapping("/{id}/close")
    public ApiResponse<Void> closeVenue(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        venueService.closeVenue(id, currentUser);

        return ApiResponse.success(null, "Venue closed successfully");
    }

    @PostMapping("/{id}/open")
    public ApiResponse<Void> openVenue(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        venueService.openVenue(id, currentUser);

        return ApiResponse.success(null, "Venue opened successfully");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteVenue(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        venueService.softDeleteVenue(id, currentUser);

        return ApiResponse.success(null, "Venue deleted successfully", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{venueId}/courts")
    public ApiResponse<CourtResponse> createCourt(
            @PathVariable Long venueId,
            @Valid @RequestBody CreateCourtRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        CourtResponse createdCourt = venueService.createCourt(venueId, request, currentUser);

        return ApiResponse.created(createdCourt, "Court created successfully");
    }

    @GetMapping("/{venueId}/my-courts")
    public ApiResponse<PageResponse<CourtResponse>> getAllCourts(
            @PathVariable Long venueId,
            @PageableDefault(
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            @AuthenticationPrincipal User currentUser
    ) {
        PageResponse<CourtResponse> courts = venueService.findAllCourtForOwner(venueId, pageable, currentUser);

        return ApiResponse.success(courts, "Fetched all courts successfully");
    }

    @GetMapping("/{venueId}/courts")
    public ApiResponse<PageResponse<CourtResponse>> getAllActiveCourts(
            @PathVariable Long venueId,
            @PageableDefault(
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        PageResponse<CourtResponse> courts = venueService.findAllActiveCourt(venueId, pageable);

        return ApiResponse.success(courts, "Fetched all active courts successfully");
    }
}
