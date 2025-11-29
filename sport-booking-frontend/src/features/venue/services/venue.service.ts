import axiosInstance from '@/lib/axios';
import type { PageResponse, PageData } from '@/types/api';
import type { ApiResponse } from '@/types/api';
import type { VenueResponse, VenueDetailResponse, VenueSearchParams } from '../types';

class VenueService {
  /**
   * Search venues with optional district filter and pagination
   */
  async searchVenues(params: VenueSearchParams = {}): Promise<PageData<VenueResponse>> {
    const { district, page = 0, size = 10 } = params;
    
    const queryParams = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
    });
    
    if (district) {
      queryParams.append('district', district);
    }

    const response = await axiosInstance.get<PageResponse<VenueResponse>>(
      `/venues/search?${queryParams.toString()}`
    );

    if (response.data.code !== 1000) {
      throw new Error(response.data.msg);
    }

    return response.data.data;
  }

  /**
   * Get venue details by ID (for future detail page)
   */
  async getVenueById(id: number): Promise<VenueDetailResponse> {
    const response = await axiosInstance.get<ApiResponse<VenueDetailResponse>>(
      `/venues/${id}`
    );

    if (response.data.code !== 1000) {
      throw new Error(response.data.msg);
    }

    return response.data.data;
  }
}

export const venueService = new VenueService();

