import { MapPin, Clock, DollarSign } from 'lucide-react';
import { Card, CardContent } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import type { VenueResponse } from '../types';

interface VenueCardProps {
  venue: VenueResponse;
}

export const VenueCard = ({ venue }: VenueCardProps) => {
  const formatPrice = (price: number) => {
    return price.toLocaleString('vi-VN') + ' đ';
  };

  const priceRange = venue.minPrice === venue.maxPrice
    ? formatPrice(venue.minPrice)
    : `${formatPrice(venue.minPrice)} - ${formatPrice(venue.maxPrice)}`;

  return (
    <Card className="overflow-hidden transition-all hover:shadow-lg hover:-translate-y-1 cursor-pointer">
      {/* Image */}
      <div className="relative h-48 w-full overflow-hidden bg-gray-200">
        <img
          src={venue.imageUrl || '/placeholder-venue.jpg'}
          alt={venue.name}
          className="h-full w-full object-cover"
          onError={(e) => {
            e.currentTarget.src = 'https://via.placeholder.com/400x300?text=Venue+Image';
          }}
        />
        <Badge className="absolute top-2 right-2 bg-blue-600">
          {venue.district}
        </Badge>
      </div>

      {/* Content */}
      <CardContent className="p-4 space-y-2">
        <h3 className="font-semibold text-lg line-clamp-1">{venue.name}</h3>
        
        <div className="flex items-start gap-2 text-sm text-gray-600">
          <MapPin className="h-4 w-4 mt-0.5 shrink-0" />
          <p className="line-clamp-1">{venue.address}</p>
        </div>

        <div className="flex items-center gap-2 text-sm text-gray-600">
          <Clock className="h-4 w-4 shrink-0" />
          <p>{venue.openTime} - {venue.closeTime}</p>
        </div>

        <div className="flex items-center justify-between pt-2 border-t">
          <div className="flex items-center gap-1 text-sm font-semibold text-blue-600">
            <DollarSign className="h-4 w-4" />
            <span>{priceRange}/giờ</span>
          </div>
          <button className="text-sm text-blue-600 hover:underline font-medium">
            Xem chi tiết →
          </button>
        </div>
      </CardContent>
    </Card>
  );
};
