import { Component, OnInit, ChangeDetectorRef, ViewChild} from '@angular/core';
import { CommonModule } from '@angular/common';
import { SearchBarComponent } from '../../components/search-bar/search-bar';
import { FilterPanelComponent } from '../../components/filter-panel/filter-panel';
import { RouterModule } from '@angular/router';
import { ListingService } from '../../services/listing';
import { SummaryListingResponse } from '../../models/listing.model';
import { LucideAngularModule } from 'lucide-angular';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { SearchBaseComponent } from '../../models/search-base';

@Component({
  selector: 'app-listings-page',
  standalone: true,
  imports: [CommonModule, SearchBarComponent, FilterPanelComponent, RouterModule, LucideAngularModule],
  templateUrl: './listings-page.html',
  styleUrls: ['./listings-page.scss']
})

export class ListingsPageComponent extends SearchBaseComponent implements OnInit 
{
  @ViewChild(SearchBarComponent) searchBar!: SearchBarComponent;

  listings: SummaryListingResponse[] = [];
  isLoading: boolean = false; 
  initialFilters: any = null;
  currentPage: number = 0;
  pageSize: number = 20;
  totalElements: number = 0;
  isLast: boolean = false;
  pendingLabel: string | null = null;
  
  searchLabel: string = ''; 
  lastPanelFilters: any = {}; 

  selectedListingType: 'SALE' | 'RENT' | null = null;

  constructor(
    private listingService: ListingService, 
    private cd: ChangeDetectorRef, 
    router: Router,  
    private route: ActivatedRoute
  ) {
    super(router);
  }




  ngOnInit() 
  {
      this.route.queryParams.subscribe(params => {
          if (Object.keys(params).length > 0) {
              this.currentFilters = {
                  city: params['city'] || null,
                  latitude: params['latitude'] ? +params['latitude'] : null,
                  longitude: params['longitude'] ? +params['longitude'] : null,
                  label: params['label'] || null,
                  listingType: params['listingType'] || null,
                  minPrice: params['minPrice'] ? +params['minPrice'] : null,
                  maxPrice: params['maxPrice'] ? +params['maxPrice'] : null,
                  minRooms: params['minRooms'] ? +params['minRooms'] : 1,
                  energyClass: params['energyClass'] || '',
                  nearStops: params['nearStops'] === 'true',
                  nearParks: params['nearParks'] === 'true',
                  nearSchools: params['nearSchools'] === 'true',
              };

              this.initialFilters = { ...this.currentFilters };

              const label = params['label'] || params['city'] || null;
              if (label) {
                  const isStreet = /\d/.test(label) || label.includes(',');
                  this.searchLabel = isStreet
                      ? `Searching listings near "${label}"`
                      : `Searching in ${label}`;

                  setTimeout(() => {
                      if (this.searchBar) {
                          this.searchBar.searchCity = label;
                          this.cd.detectChanges();
                      }
                  }, 0);
              }
          }
          this.loadListings();
      });
  }

  loadListings(page: number = 0) 
  {
    this.isLoading = true;
    this.listings = [];
    this.currentPage = page;
    this.cd.detectChanges();

    if (!this.searchLabel && this.searchBar && this.searchBar.searchCity) {
        const text = this.searchBar.searchCity;
        const isStreet = /\d/.test(text) || text.includes(',');
        this.searchLabel = isStreet 
            ? `Searching listings near "${text}"` 
            : `Searching in ${text}`;
    }

    const searchRequest = { ...this.currentFilters, page, size: this.pageSize };
    this.listingService.searchListings(searchRequest).subscribe({
        next: (data: any) => this.handleResponse(data),
        error: () => this.handleError()
    });
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
    this.isLoading = false;
    alert("Something went wrong on our side, please reload the page and retry.");
    this.cd.detectChanges();
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