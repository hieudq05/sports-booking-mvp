package com.dqhieuse.sportbookingbackend.modules.venue.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record AddVenueImagesRequest(
        @NotEmpty(message = "Images must not be empty")
        List<String> imageUrls
) {
}
