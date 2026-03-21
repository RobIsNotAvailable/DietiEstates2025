export interface SummaryListingResponse 
{
  id: number;
  name: string;
  price: number;
  listingType: string;
  formattedAddress: string;
  squareMeters: number;
  nearStops: boolean;
  nearParks: boolean;
  nearSchools: boolean;
  imageUrl: string;
}