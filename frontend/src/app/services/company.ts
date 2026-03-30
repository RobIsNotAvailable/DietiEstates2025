import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AgentStatsResponse 
{
  activeListings: number;
  concludedListings: number;
  nViews: number;
  activeVisits: number;
  concludedVisits: number;
  nOffers: number;
}

export interface AgentStatsRequest 
{
  agentEmail: string;
  year: number;
  month: number;
}

export interface CreateStaffRequest
{
  firstName: string;
  lastName: string;
  email: string;
  securityLevel: string;
}

@Injectable({
  providedIn: 'root'
})
export class StatsService 
{
  private statsUrl = '/api/company/stats';
  private createUrl = '/api/company/create';

  constructor(private http: HttpClient) {}

  getMonthlyStats(request: AgentStatsRequest): Observable<AgentStatsResponse> 
  {
    return this.http.post<AgentStatsResponse>(this.statsUrl, request);
  }

  createStaffMember(request: CreateStaffRequest): Observable<string>
  {
    return this.http.post(this.createUrl, request, { responseType: 'text' });
  }
}