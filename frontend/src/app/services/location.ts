import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface GeoapifyProperties {
  city: string;
  street: string;
  houseNumber: string;
  province: string;
  zipCode: string;
  country: string;
  formattedAddress: string;
  latitude: number;
  longitude: number;
}

@Injectable({
  providedIn: 'root'
})
export class LocationService 
{
  private apiUrl = '/api/location';

  constructor(private http: HttpClient) {}
  

  normalizeAddress(rawAddress: string): Observable<GeoapifyProperties> 
  {
    return this.http.get<GeoapifyProperties>(`${this.apiUrl}/suggestions`, 
    {
      params: { rawAddress }
    });
  }

  getSurroundings(lat: number, lon: number): Observable<any> 
  {
    return this.http.get<any>(`${this.apiUrl}/surroundings`, {
      params: { lat: lat.toString(), lon: lon.toString() }
    });
  }
}