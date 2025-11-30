import { Skeleton } from '@/components/ui/skeleton';
import { VenueCard } from './VenueCard';
import type { VenueResponse } from '../types';

interface VenueListProps {
  venues: VenueResponse[];
  isLoading: boolean;
}

const VenueCardSkeleton = () => (
  <div className="space-y-3">
    <Skeleton className="h-48 w-full" />
    <Skeleton className="h-6 w-3/4" />
    <Skeleton className="h-4 w-full" />
    <Skeleton className="h-4 w-1/2" />
  </div>
);

export const VenueList = ({ venues, isLoading }: VenueListProps) => {
  if (isLoading) {
    return (
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {Array.from({ length: 6 }).map((_, i) => (
          <VenueCardSkeleton key={i} />
        ))}
      </div>
    );
  }

  if (venues.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center py-16 text-center">
        <div className="h-24 w-24 mb-4 rounded-full bg-gray-100 flex items-center justify-center">
          <svg
            className="h-12 w-12 text-gray-400"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
            />
          </svg>
        </div>
        <h3 className="text-lg font-semibold text-gray-900 mb-2">
          Không tìm thấy sân bóng
        </h3>
        <p className="text-gray-500">
          Thử thay đổi bộ lọc hoặc tìm kiếm với từ khóa khác
        </p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {venues.map((venue) => (
        <VenueCard key={venue.id} venue={venue} />
      ))}
    </div>
  );
};
