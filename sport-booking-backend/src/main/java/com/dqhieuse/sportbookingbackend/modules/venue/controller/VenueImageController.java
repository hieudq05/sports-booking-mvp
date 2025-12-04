package com.dqhieuse.sportbookingbackend.modules.venue.controller;

import com.dqhieuse.sportbookingbackend.common.dto.ApiResponse;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import com.dqhieuse.sportbookingbackend.modules.venue.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/venue-images")
@RequiredArgsConstructor
class VenueImageController {

    private final VenueService venueService;

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteVenueImage(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        venueService.deleteVenueImage(id, currentUser);

        return ApiResponse.success(null, "Venue image deleted successfully");
    }
}
