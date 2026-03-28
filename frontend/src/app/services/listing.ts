import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SummaryListingResponse } from '../models/listing.model';
import { ListingStatsResponse } from '../models/listingsStatsResponse.model';


@Injectable({ providedIn: 'root' })
export class ListingService 
{
  private apiUrl = '/api/listings';

  constructor(private http: HttpClient) {}

  createListing(data: any): Observable<number> 
  {
    return this.http.post<number>(`${this.apiUrl}/create`, data);
  }

  uploadPhotos(id: number, photos: File[], descriptions: string[]): Observable<any> 
  {
    const formData = new FormData();
    
    photos.forEach(file => 
    {
      formData.append('photos', file, file.name);
    });

    descriptions.forEach(desc => 
    {
      formData.append('descriptions', desc);
    });
    
    return this.http.post(`${this.apiUrl}/photos/${id}`, formData);
  }

  searchListings(request: any): Observable<SummaryListingResponse[]> 
  {
      let params = new HttpParams();

      Object.keys(request).forEach(key => 
      {
        const value = request[key];
        
        if (value !== null && value !== undefined && value !== '') 
        {
          params = params.set(key, value.toString());
        }
      });

      return this.http.get<SummaryListingResponse[]>(`${this.apiUrl}/search`, { params });
  }

  getAgentStats(): Observable<ListingStatsResponse[]> 
  {
      return this.http.get<ListingStatsResponse[]>(`${this.apiUrl}/stats`);
  }
}