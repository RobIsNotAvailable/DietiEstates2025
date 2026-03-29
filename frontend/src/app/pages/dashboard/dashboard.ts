import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { StatsService } from '../../services/stats';
import { ListingService } from '../../services/listing';

export interface ListingStatsResponse {
    id: number;
    name: string;
    price: number;
    listingType: string;
    formattedAddress: string;
    imageUrl: string;
    views: number;
    visitsRecieved: number;
    offersRecieved: number;
    highestOfferedPrice: number;
    lastModified: string;
    status: string;
    closurePrice: number | null;
}

export interface AgentStatsResponse {
    activeListings: number;
    concludedListings: number;
    nViews: number;
    activeVisits: number;
    concludedVisits: number;
    nOffers: number;
}

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './dashboard.html',
    styleUrl: './dashboard.scss',
})
export class DashboardComponent implements OnInit {
    agentName: string = '';
    agentEmail: string = '';
    userRole: string = '';

    isLoading: boolean = false;
    isListLoading: boolean = false;
    stats: AgentStatsResponse | null = null;
    agentListings: ListingStatsResponse[] = [];

    currentMonthLabel: string = new Date().toLocaleString('en-US', { month: 'long', year: 'numeric' });

    constructor(
        private statsService: StatsService,
        private listingService: ListingService,
        private cd: ChangeDetectorRef,
        private router: Router
    ) {}

    ngOnInit(): void {
        this.initializeUserData();

        if (this.agentEmail) {
            this.loadDashboardData();
        } else {
            this.router.navigate(['/login']);
        }
    }

    navigateToListing(id: number): void {
        this.router.navigate(['/view', id]);
    }

    exportPdf(): void {
        const previousTitle = document.title;
        document.title = `DietiEstates_Report_${this.agentName}_${this.currentMonthLabel}`.replace(/\s+/g, '_');
        window.print();
        document.title = previousTitle;
    }

    private initializeUserData(): void {
        const savedUser = localStorage.getItem('user');
        if (!savedUser) return;

        try {
            const userData = JSON.parse(savedUser);
            this.agentEmail = userData.email;
            this.agentName = `${userData.firstName} ${userData.lastName}`;
            this.userRole = userData.role ?? '';
        } catch (e) {
            console.error('Error parsing user data', e);
        }
    }

    private loadDashboardData(): void {
        this.isLoading = true;
        this.isListLoading = true;

        const now = new Date();
        const statsRequest = {
            agentEmail: this.agentEmail,
            year: now.getFullYear(),
            month: now.getMonth() + 1
        };

        this.statsService.getMonthlyStats(statsRequest).subscribe({
            next: (data) => {
                this.stats = data;
                this.isLoading = false;
                this.cd.detectChanges();
            },
            error: () => {
                this.isLoading = false;
                this.cd.detectChanges();
            }
        });

        this.listingService.getAgentStats().subscribe({
            next: (data: ListingStatsResponse[]) => {
                this.agentListings = data;
                this.isListLoading = false;
                this.cd.detectChanges();
            },
            error: (err) => {
                console.error('Error loading listings:', err);
                this.isListLoading = false;
                this.cd.detectChanges();
            }
        });
    }
}