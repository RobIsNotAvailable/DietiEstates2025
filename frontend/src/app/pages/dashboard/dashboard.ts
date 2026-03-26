import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { StatsService, AgentStatsRequest } from '../../services/stats';

export interface AgentMonthlyStatsResponse 
{
    activeListings: number;
    concludedListings: number;
    nViews: number;
    activeVisits: number;
    concludedVisits: number;
    nOffers: number;
}

export interface MonthOption 
{
    value: number;
    year: number;
    label: string;
}

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './dashboard.html',
    styleUrl: './dashboard.scss',
})
export class DashboardComponent implements OnInit 
{
    agentName: string = '';
    agentEmail: string = '';
    today: Date = new Date();
    isLoading: boolean = false;
    stats: AgentMonthlyStatsResponse | null = null;

    selectedMonth: number = new Date().getMonth() + 1;
    selectedYear: number = new Date().getFullYear();
    availableMonths: MonthOption[] = [];

    constructor(
        private statsService: StatsService, 
        private cd: ChangeDetectorRef,
        private router: Router
    ) {} 

    ngOnInit(): void 
    {
        this.initializeUserData();
        this.generateMonthOptions();
        
        // Carica le statistiche solo se abbiamo un'email valida
        if (this.agentEmail) 
        {
            this.loadStats();
        } 
        else 
        {
            console.warn('No user data found, redirecting to login...');
            this.router.navigate(['/login']);
        }
    }

    /**
     * Recupera i dati dell'utente dal localStorage salvati dalla HomeComponent
     */
    private initializeUserData(): void 
    {
        const savedUser = localStorage.getItem('user');
        if (savedUser) 
        {
            try 
            {
                const userData = JSON.parse(savedUser);
                this.agentEmail = userData.email;
                this.agentName = `${userData.firstName} ${userData.lastName}`;
                console.log('Dashboard initialized for:', this.agentEmail);
            } 
            catch (e) 
            {
                console.error('Error parsing user data from localStorage', e);
            }
        }
    }

    generateMonthOptions(): void 
    {
        const monthsToGenerate = 6;
        const now = new Date();
        
        for (let i = 0; i < monthsToGenerate; i++) 
        {
            const d = new Date(now.getFullYear(), now.getMonth() - i, 1);
            this.availableMonths.push({
                value: d.getMonth() + 1,
                year: d.getFullYear(),
                label: d.toLocaleString('en-US', { month: 'long', year: 'numeric' })
            });
        }
    }

    onMonthChange(event: Event): void 
    {
        const selectElement = event.target as HTMLSelectElement;
        const parts = selectElement.value.split('-');
        if (parts.length === 2) 
        {
            this.selectedMonth = Number(parts[0]);
            this.selectedYear = Number(parts[1]);
            this.loadStats();
        }
    }

    loadStats(): void 
    {
        this.isLoading = true;

        const request: AgentStatsRequest = 
        {
            agentEmail: this.agentEmail,
            year: this.selectedYear,
            month: this.selectedMonth
        };

        this.statsService.getMonthlyStats(request).subscribe({
            next: (data: AgentMonthlyStatsResponse) => 
            {
                this.stats = data;
                this.isLoading = false;
                this.cd.detectChanges();
            },
            error: (err: any) => 
            {
                console.error('Errore nel recupero statistiche:', err);
                this.isLoading = false;
                this.cd.detectChanges();
            }
        });
    }
}