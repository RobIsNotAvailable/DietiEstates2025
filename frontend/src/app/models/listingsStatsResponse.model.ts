export interface ListingStatsResponse 
{
    id: number;
    name: string;
    price: number;
    listingType: string;
    formattedAddress: string;
    imageUrl: string;
    
    views: number;
    visitsRecieved: number;
    offersRecieved: number;
    highestOfferedPrice: number;
    lastModified: string; 
    
    status: 'ACTIVE' | 'ENDED_SUCCESFULLY' | 'CANCELLED'; 
    closurePrice: number | null;
}