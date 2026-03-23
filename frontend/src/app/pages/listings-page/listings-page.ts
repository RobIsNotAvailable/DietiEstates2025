import { Component, OnInit, ChangeDetectorRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SearchBarComponent } from '../../components/search-bar/search-bar';
import { FilterPanelComponent } from '../../components/filter-panel/filter-panel';
import { RouterModule } from '@angular/router';
import { ListingService } from '../../services/listing';
import { SummaryListingResponse } from '../../models/listing.model';
import { Page } from '../../models/page.model';
import { LucideAngularModule } from 'lucide-angular';
import { Router } from '@angular/router';
import { ListingSearchRequest } from '../../models/listingSearchRequest';

@Component({
  selector: 'app-listings-page',
  standalone: true,
  imports: [CommonModule, SearchBarComponent, FilterPanelComponent, RouterModule, LucideAngularModule],
  templateUrl: './listings-page.html',
  styleUrls: ['./listings-page.scss']
})
export class ListingsPageComponent implements OnInit 
{
  @ViewChild(FilterPanelComponent) filterPanel!: FilterPanelComponent;
  @ViewChild(SearchBarComponent) searchBar!: SearchBarComponent;


  listings: SummaryListingResponse[] = [];
  isLoading: boolean = false;
  isFiltersOpen: boolean = false;

  currentPage: number = 0;
  pageSize: number = 20;
  totalElements: number = 0;
  isLast: boolean = false;
  
  searchLabel: string = ''; 
  currentFilters: ListingSearchRequest = {};
  lastPanelFilters: any = {}; 

  selectedListingType: 'SALE' | 'RENT' | null = null;

  constructor(private listingService: ListingService, private cd: ChangeDetectorRef, private router: Router) {}

  ngOnInit() 
  {
    this.loadListings();
  }

  loadListings(page: number = 0) 
  {
    this.isLoading = true;
    this.currentPage = page;

    if (this.searchBar && this.searchBar.searchCity) 
    {
      const text = this.searchBar.searchCity;
      const isStreet = /\d/.test(text) || text.includes(',');
      this.searchLabel = isStreet ? `Searching listings near "${text}"` : `Searching in ${text}`;
    } 
    else 
    {
      this.searchLabel = '';
    }

    const hasFilters = Object.keys(this.currentFilters).length > 0;

    if (hasFilters) {
      const searchRequest = { ...this.currentFilters, page, size: this.pageSize };
      console.log(searchRequest);
      this.listingService.searchListings(searchRequest).subscribe({
        next: (data: any) => this.handleResponse(data),
        error: () => this.handleError()
      });
    } else {
      this.listingService.getActiveListings(page, this.pageSize).subscribe({
        next: (data: Page<SummaryListingResponse>) => this.handleResponse(data),
        error: () => this.handleError()
      });
    }
  }

  handleLocationChange(locationData: any) 
  {
    this.currentFilters = 
    {
        ...this.currentFilters, 
        city: locationData.city ?? null,
        latitude: locationData.lat ?? null,
        longitude: locationData.lon ?? null
    };
  }

  handleFilterChange(filterOptions: any) 
  {
    setTimeout(() => 
    {
        this.currentFilters = {
        ...this.currentFilters, 
        ...filterOptions        
    };
    
    this.loadListings(0);
    }, 0);
  }

  handleFullReset() 
  {
    if (this.searchBar) this.searchBar.reset();
    this.currentFilters = {};
    this.lastPanelFilters = {};  
    this.searchLabel = '';
  }

  toggleFilterPanel() 
  {
    this.isFiltersOpen = !this.isFiltersOpen;
  }

  goToNextPage() 
  {
    if (!this.isLast) this.loadListings(this.currentPage + 1);
  }

  goToPreviousPage() 
  {
    if (this.currentPage > 0) this.loadListings(this.currentPage - 1);
  }

  goToDetail(id: number | string) 
  {
    this.router.navigate(['/listings', id]);
  }

  onSave(event: Event) 
  {
    event.stopPropagation(); 
    this.router.navigate(['/not-implemented']);
  }

  private handleResponse(data: any) 
  {
    if (Array.isArray(data)) 
    {
      this.listings = data;
      this.totalElements = data.length;
      this.isLast = true;
    } 
    else 
    {
      this.listings = data.content;
      this.totalElements = data.totalElements;
      this.isLast = data.last;
    }
    this.isLoading = false;
    this.cd.detectChanges();
  }

  private handleError() 
  {
    alert("Something went wrong on our side, please reload the page and retry.");
    this.isLoading = false;
  }

  onSortChange(event: Event) 
  {
    const target = event.target as HTMLSelectElement;
    const criteria = target.value;

    if (!this.listings || this.listings.length === 0) return;

    switch (criteria) 
    {
      case 'Newest':
        this.listings.sort((a, b) => b.id - a.id);
        break;
        
      case 'Price: Low to High':
        this.listings.sort((a, b) => a.price - b.price);
        break;
        
      case 'Price: High to Low':
        this.listings.sort((a, b) => b.price - a.price);
        break;
        
      case 'Surface: Largest': 
        this.listings.sort((a, b) => b.squareMeters - a.squareMeters);
        break;
    }
    
    this.cd.detectChanges();
  }

  handleSearchBarSearch() 
  {
    setTimeout(() => 
    {
        if (this.filterPanel) 
        {
            this.filterPanel.applyFilters();   
        } 
        else 
        {
            this.loadListings(0);   
        }
        this.cd.detectChanges();
    }, 0);
  }
}