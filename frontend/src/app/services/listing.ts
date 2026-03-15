import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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
    
    photos.forEach(file => {
      formData.append('photos', file, file.name);
    });

    descriptions.forEach(desc => {
      formData.append('descriptions', desc);
    });
    
    return this.http.post(`${this.apiUrl}/photos/${id}`, formData);
  }
}