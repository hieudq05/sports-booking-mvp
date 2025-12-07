package com.dqhieuse.sportbookingbackend.modules.venue.dto;

import java.util.List;

public record CourtDetailResponse(
        CourtResponse court,
        OwnerResponse owner,
        String status,
        String description,
        String thumbnail,
        List<CourtImageResponse> images
) {
}
