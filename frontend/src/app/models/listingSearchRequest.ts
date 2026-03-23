export interface ListingSearchRequest 
{
  city?: string;
  latitude?: number;
  longitude?: number;
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