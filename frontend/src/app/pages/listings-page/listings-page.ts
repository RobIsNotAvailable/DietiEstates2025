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

  listings: SummaryListingResponse[] = [];
  isLoading: boolean = false;
  isFiltersOpen: boolean = false;
  currentCity: string = ''; 

  currentPage: number = 0;
  pageSize: number = 20;
  totalElements: number = 0;
  isLast: boolean = false;

  currentFilters: any = null;
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
    
    this.listings = []; 
    this.totalElements = 0;
    this.cd.detectChanges();
    
    if (this.currentFilters) 
    {
      const searchRequest = { ...this.currentFilters, page, size: this.pageSize };
      
      this.listingService.searchListings(searchRequest).subscribe
      ({
        next: (data: any) => 
        {
          this.handleResponse(data)
          this.cd.detectChanges();
        },
        error: () => this.handleError()
      });
    } 
    else 
    {
      this.listingService.getActiveListings(page, this.pageSize).subscribe
      ({
        next: (data: Page<SummaryListingResponse>) => this.handleResponse(data),
        error: () => this.handleError()
      });
    }
  }

  handleSearchBarClick() 
  {

    if (this.filterPanel) 
    {
      const filtersFromPanel = this.filterPanel.filters;
      this.handleSearch(filtersFromPanel);
    }
    else 
    {
      this.loadListings(0);
    }
  }

  handleSearch(filters: any) 
  {
    this.currentFilters = filters;
    this.selectedListingType = filters?.listingType;
    this.isFiltersOpen = false;
    this.loadListings(0);
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
}