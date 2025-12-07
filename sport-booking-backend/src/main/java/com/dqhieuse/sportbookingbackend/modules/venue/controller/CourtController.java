package com.dqhieuse.sportbookingbackend.modules.venue.controller;

import com.dqhieuse.sportbookingbackend.common.dto.ApiResponse;
import com.dqhieuse.sportbookingbackend.modules.auth.entity.User;
import com.dqhieuse.sportbookingbackend.modules.venue.dto.CourtDetailResponse;
import com.dqhieuse.sportbookingbackend.modules.venue.dto.UpdateCourtRequest;
import com.dqhieuse.sportbookingbackend.modules.venue.service.CourtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courts")
@RequiredArgsConstructor
class CourtController {

    private final CourtService courtService;

    @PutMapping("/{courtId}")
    public ApiResponse<CourtDetailResponse> updateCourt(
            @PathVariable Long courtId,
            @Valid @RequestBody UpdateCourtRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        CourtDetailResponse updatedCourt = courtService.updateCourt(courtId, request, currentUser);

        return ApiResponse.success(updatedCourt, "Court updated successfully");
    }

    @PostMapping("/{courtId}/inactive")
    public ApiResponse<Void> inActiveCourt(@PathVariable Long courtId, @AuthenticationPrincipal User currentUser) {
        courtService.inActiveCourt(courtId, currentUser);

        return ApiResponse.success(null, "Court inactivated successfully");
    }

    @PostMapping("/{courtId}/active")
    public ApiResponse<Void> restoreCourt(@PathVariable Long courtId, @AuthenticationPrincipal User currentUser) {
        courtService.activeCourt(courtId, currentUser);

        return ApiResponse.success(null, "Court restored successfully");
    }

    @GetMapping("/{courtId}")
    public ApiResponse<CourtDetailResponse> getCourtDetail(@PathVariable Long courtId) {
        CourtDetailResponse courtDetailResponse = courtService.findCourtById(courtId);

        return ApiResponse.success(courtDetailResponse, "Court detail retrieved successfully");
    }
}
