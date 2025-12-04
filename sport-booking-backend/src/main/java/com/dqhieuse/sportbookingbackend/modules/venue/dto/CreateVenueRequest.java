package com.dqhieuse.sportbookingbackend.modules.venue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;

public record CreateVenueRequest (
        @NotBlank(message = "Venue name is required")
        String name,

        @NotBlank(message = "Venue address is required")
        String address,

        String district,

        String description,

        @NotBlank(message = "Thumbnail URL is required")
        String thumbnail,

        @NotNull(message = "Open time is required")
        LocalTime openTime,

        @NotNull(message = "Close time is required")
        LocalTime closeTime,

        @NotNull(message = "Image description is required")
        List<String> images
) {}
