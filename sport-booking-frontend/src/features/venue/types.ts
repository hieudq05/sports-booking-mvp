export interface VenueResponse {
  id: number;
  name: string;
  address: string;
  district: string;
  imageUrl: string;
  openTime: string;       // Format "HH:mm" (e.g., "07:00")
  closeTime: string;      // Format "HH:mm" (e.g., "22:00")
  minPrice: number;       // Lowest price among courts
  maxPrice: number;       // Highest price among courts
}

export interface CourtResponse {
  id: number;
  name: string;           // "Sân 1", "Sân 5 người A"
  type: 'SIZE_5' | 'SIZE_7' | 'SIZE_11';
  pricePerHour: number;
}

export interface VenueImageResponse {
  id: number;
  imageUrl: string;
}

export interface VenueDetailResponse extends VenueResponse {
  description: string;
  courts: CourtResponse[];
  images: VenueImageResponse[];
}

// Query params for search
export interface VenueSearchParams {
  district?: string;
  page?: number;
  size?: number;
}
