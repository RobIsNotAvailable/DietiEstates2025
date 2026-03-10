import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountService 
{
    private apiUrl = `/api/accounts`;

    constructor(private http: HttpClient) {}

    changePassword(formData: any): Observable<string> 
    {
        return this.http.patch(`${this.apiUrl}/change-password`, formData, 
        { 
            responseType: 'text' 
        });
    }

    getAccountDetails(): Observable<any> 
    {
        return this.http.get<any>(`${this.apiUrl}/me`);
    }

    forgotPassword(formData: any): Observable<string> 
    {
        return this.http.post(`${this.apiUrl}/forgot-password`, formData, 
        { 
            responseType: 'text' 
        });
    }
}