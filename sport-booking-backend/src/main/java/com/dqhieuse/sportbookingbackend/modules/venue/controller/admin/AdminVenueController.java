package com.dqhieuse.sportbookingbackend.modules.venue.controller.admin;

import com.dqhieuse.sportbookingbackend.common.dto.ApiResponse;
import com.dqhieuse.sportbookingbackend.common.dto.PageResponse;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import com.dqhieuse.sportbookingbackend.modules.venue.dto.VenueSummaryResponse;
import com.dqhieuse.sportbookingbackend.modules.venue.entity.VenueStatus;
import com.dqhieuse.sportbookingbackend.modules.venue.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/venues")
@RequiredArgsConstructor
class AdminVenueController {

    private final VenueService venueService;

    @GetMapping
    public ApiResponse<PageResponse<VenueSummaryResponse>> getAllVenues(
            @PageableDefault(
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            @RequestParam(required = false) VenueStatus status
    ) {
        PageResponse<VenueSummaryResponse> venues;

        if (status == null) {
            venues = venueService.findAllVenuesForAdmin(pageable);
        } else {
            venues = venueService.findAllVenuesByStatusForAdmin(status, pageable);
        }

        return ApiResponse.success(venues, "Fetched all venues successfully");
    }

    @GetMapping("/deleted")
    public ApiResponse<PageResponse<VenueSummaryResponse>> getAllDeletedVenues(
            @PageableDefault(
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        PageResponse<VenueSummaryResponse> venues = venueService.findAllVenuesDeletedForAdmin(pageable);

        return ApiResponse.success(venues, "Fetched all deleted venues successfully");
    }

    @PostMapping("/{id}/restore")
    public ApiResponse<Void> restoreVenue(@PathVariable Long id) {
        venueService.restoreVenue(id);

        return ApiResponse.success(null, "Venue restored successfully", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/reject")
    public ApiResponse<Void> rejectVenue(@PathVariable Long id) {
        venueService.rejectVenue(id);

        return ApiResponse.success(null, "Venue rejected successfully", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/approve")
    public ApiResponse<Void> approveVenue(@PathVariable Long id) {
        venueService.approveVenue(id);

        return ApiResponse.success(null, "Venue approved successfully", HttpStatus.NO_CONTENT);
    }
}
