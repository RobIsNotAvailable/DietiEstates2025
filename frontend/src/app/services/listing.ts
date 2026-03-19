import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../models/page.model';
import { SummaryListingResponse } from '../models/listing.model';

@Injectable({ providedIn: 'root' })
export class ListingService 
{
  private apiUrl = '/api/listings';

  constructor(private http: HttpClient) {}

  getActiveListings(page: number, size: number): Observable<Page<SummaryListingResponse>> 
  {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page<SummaryListingResponse>>(`${this.apiUrl}/active`, { params });
  }

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
}