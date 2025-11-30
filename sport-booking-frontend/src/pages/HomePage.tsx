import { useState } from 'react';
import { MainLayout } from '@/components/layout/MainLayout';
import { SearchBar } from '@/features/venue/components/SearchBar';
import { VenueList } from '@/features/venue/components/VenueList';
import { useVenues } from '@/features/venue/hooks/useVenues';

export const HomePage = () => {
  const [district, setDistrict] = useState('All');

  // Fetch venues with district filter
  const { data, isLoading, error } = useVenues({
    district: district === 'All' ? undefined : district,
    page: 0,
    size: 12,
  });

  return (
    <MainLayout>
      <div className="space-y-6">
        {/* Hero Section */}
        <div className="text-center py-8">
          <h1 className="text-4xl font-bold text-gray-900 mb-2">
            Đặt Sân Bóng Dễ Dàng
          </h1>
          <p className="text-gray-600 text-lg">
            Tìm và đặt sân bóng đá tốt nhất tại Hà Nội
          </p>
        </div>

        {/* Search Bar */}
        <SearchBar district={district} onDistrictChange={setDistrict} />

        {/* Error State */}
        {error && (
          <div className="bg-red-50 border border-red-200 rounded-lg p-4 text-center">
            <p className="text-red-600">
              Có lỗi xảy ra khi tải dữ liệu. Vui lòng thử lại sau.
            </p>
          </div>
        )}

        {/* Venue List */}
        <VenueList
          venues={data?.content || []}
          isLoading={isLoading}
        />

        {/* Pagination Info (Simple MVP version) */}
        {data && data.content.length > 0 && (
          <div className="text-center text-sm text-gray-600 pt-4">
            Hiển thị {data.content.length} trên tổng {data.totalElements} sân bóng
          </div>
        )}
      </div>
    </MainLayout>
  );
};
