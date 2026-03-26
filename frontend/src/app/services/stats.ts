import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AgentMonthlyStatsResponse 
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

  getMonthlyStats(request: AgentStatsRequest): Observable<AgentMonthlyStatsResponse> 
  {

    return this.http.post<AgentMonthlyStatsResponse>(this.apiUrl, request);
  }
}