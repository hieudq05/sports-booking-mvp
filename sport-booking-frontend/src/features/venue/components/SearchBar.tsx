import { Search } from 'lucide-react';
import { Input } from '@/components/ui/input';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';

interface SearchBarProps {
  district: string;
  onDistrictChange: (value: string) => void;
}

const DISTRICTS = [
  'All',
  'Ba Dinh',
  'Hoan Kiem',
  'Dong Da',
  'Hai Ba Trung',
  'Cau Giay',
  'Tay Ho',
  'Thanh Xuan',
  'Long Bien',
  'Hoang Mai',
  'Ha Dong',
];

export const SearchBar = ({ district, onDistrictChange }: SearchBarProps) => {
  return (
    <div className="flex flex-col sm:flex-row gap-4 mb-8">
      {/* Search Input (for future implementation) */}
      <div className="relative flex-1">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
        <Input
          placeholder="Tìm kiếm sân bóng..."
          className="pl-10"
          disabled
        />
      </div>

      {/* District Filter */}
      <Select value={district} onValueChange={onDistrictChange}>
        <SelectTrigger className="w-full sm:w-[200px]">
          <SelectValue placeholder="Chọn quận" />
        </SelectTrigger>
        <SelectContent>
          {DISTRICTS.map((d) => (
            <SelectItem key={d} value={d}>
              {d === 'All' ? 'Tất cả quận' : d}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>
    </div>
  );
};
