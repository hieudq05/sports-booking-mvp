package com.dqhieuse.sportbookingbackend.modules.venue.dto;

import java.time.LocalTime;
import java.util.List;

public record VenueDetailResponse(
        Long id,
        String name,
        String address,
        String district,
        String description,
        String thumbnail,
        String status,
        LocalTime openTime,
        LocalTime closeTime,
        OwnerResponse owner,

        List<CourtResponse> courts,
        List<VenueImageResponse> images
) {
}
