export interface ListingSearchRequest 
{
  city?: string;
  latitude?: number | null;
  longitude?: number | null;
  listingType?: 'SALE' | 'RENT' | null;
  minPrice?: number | null;
  maxPrice?: number | null;
  minRooms?: number;
  energyClass?: string;
  nearStops?: boolean;
  nearParks?: boolean;
  nearSchools?: boolean;
  page?: number;
  size?: number;
}