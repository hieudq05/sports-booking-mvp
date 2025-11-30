import { useQuery } from '@tanstack/react-query';
import { venueService } from '../services/venue.service';
import type { VenueSearchParams } from '../types';

// Query key factory for proper caching
export const venueKeys = {
  all: ['venues'] as const,
  lists: () => [...venueKeys.all, 'list'] as const,
  list: (params: VenueSearchParams) => [...venueKeys.lists(), params] as const,
  details: () => [...venueKeys.all, 'detail'] as const,
  detail: (id: number) => [...venueKeys.details(), id] as const,
};

export const useVenues = (params: VenueSearchParams = {}) => {
  return useQuery({
    queryKey: venueKeys.list(params),
    queryFn: () => venueService.searchVenues(params),
    staleTime: 2 * 60 * 1000, // Cache for 2 minutes
  });
};
