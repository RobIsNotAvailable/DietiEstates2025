import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SearchBarComponent } from '../../components/search-bar/search-bar';
import { FilterPanelComponent } from '../../components/filter-panel/filter-panel';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-listings-page',
  standalone: true,
  imports: [CommonModule, SearchBarComponent, FilterPanelComponent, RouterModule],
  templateUrl: './listings-page.html',
  styleUrls: ['./listings-page.scss']
})
export class ListingsPageComponent 
{
  isFiltersOpen: boolean = false;
  currentCity: string = ''; 

  toggleFilterPanel() 
  {
    this.isFiltersOpen = !this.isFiltersOpen;
  }
}