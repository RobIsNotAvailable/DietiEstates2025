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

@Injectable({
  providedIn: 'root'
})
export class StatsService 
{
  private apiUrl = '/api/company/stats'; 

  constructor(private http: HttpClient) {}

  getMonthlyStats(request: AgentStatsRequest): Observable<AgentStatsResponse> 
  {
    return this.http.post<AgentStatsResponse>(this.apiUrl, request);
  }
}