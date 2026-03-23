import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap} from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService 
{
  private apiUrl = '/api/auth';
  private readonly TOKEN_KEY = 'dieti_token';

  constructor(private http: HttpClient) {}

  login(credentials: any): Observable<any> 
  {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
      tap(res => 
      {
        if (res.token) 
        {
          this.saveToken(res.token);
        }
      })
    );
  }

  register(userData: any): Observable<any> 
  {
    return this.http.post<any>(`${this.apiUrl}/register`, userData).pipe(
      tap(res => 
      {
        if (res.token) 
        {
          this.saveToken(res.token);
        }
      })
    );
  }

  private saveToken(token: string) 
  {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null 
  {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  logout() 
  {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  isLoggedIn(): boolean 
  {
    return !!this.getToken();
  }

  getUserRole(): string | null 
  {
    const token = this.getToken(); 
    if (!token) 
    {
      return null;
    }

    try 
    {
      const payload = JSON.parse(atob(token.split('.')[1]));
      
      return payload.role; 
    } 
    catch (e) 
    {
      return null;
    }
  }

  hasAtLeastRole(requiredRoleName: string): boolean
  {
    const userRoleName = this.getUserRole();
    if (!userRoleName) return false;


    const permissions: Record<string, string[]> =
    {
      'AGENT': ['AGENT', 'SUPPORT', 'ADMIN'],
      'SUPPORT': ['SUPPORT', 'ADMIN'],
      'ADMIN': ['ADMIN'],
      'CLIENT': ['CLIENT', 'AGENT', 'SUPPORT', 'ADMIN']
    };

    const authorizedRoles = permissions[requiredRoleName];

    if (!authorizedRoles) return false;

    return authorizedRoles.includes(userRoleName);
  }

}