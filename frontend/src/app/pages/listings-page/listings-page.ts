import { Component, OnInit, ChangeDetectorRef} from '@angular/core';
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
  listings: SummaryListingResponse[] = [];
  isLoading: boolean = false;
  isFiltersOpen: boolean = false;
  currentCity: string = ''; 

  currentPage: number = 0;
  pageSize: number = 20;
  totalElements: number = 0;
  isLast: boolean = false;

  constructor(private listingService: ListingService, private cd: ChangeDetectorRef, private router: Router) {}

  ngOnInit() 
  {
    this.loadListings();
  }

  loadListings(page: number = 0) 
  {
    this.isLoading = true;
    
    this.listingService.getActiveListings(page, this.pageSize).subscribe
    ({
      next: (data: Page<SummaryListingResponse>) => 
      {
        this.listings = data.content;
        this.totalElements = data.totalElements;
        this.currentPage = data.number;
        this.isLast = data.last;
        this.isLoading = false;
        this.cd.detectChanges();
      },
      error: (err) => 
      {
        this.isLoading = false;
      }
    });
  }

  toggleFilterPanel() 
  {
    this.isFiltersOpen = !this.isFiltersOpen;
  }

  goToNextPage() 
  {
    if (!this.isLast) 
    {
      this.loadListings(this.currentPage + 1);
    }
  }

  goToPreviousPage() 
  {
    if (this.currentPage > 0) 
    {
      this.loadListings(this.currentPage - 1);
    }
  }

  goToDetail(id: number | string) 
  {
    this.router.navigate(['/listings', id]);
  }

  onSave(event: Event) 
  {
    //will be implemented in the future
    event.stopPropagation(); 
    this.router.navigate(['/not-implemented']);
  }
}