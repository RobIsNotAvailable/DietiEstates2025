import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SearchBarComponent } from '../../components/search-bar/search-bar';
import { FilterPanelComponent } from '../../components/filter-panel/filter-panel';

@Component({
  selector: 'app-listings-page',
  standalone: true,
  imports: [CommonModule, SearchBarComponent, FilterPanelComponent],
  templateUrl: './listings-page.html',
  styleUrls: ['./listings-page.scss']
})
export class ListingsPageComponent 
{
  isSidebarOpen: boolean = false;
  currentCity: string = ''; 

  onFilterApply(filters: any) 
  {
    if (filters.city) 
    {
      this.currentCity = filters.city;
    }
    this.isSidebarOpen = false;
  }

  onCityChange(newCity: string) 
  {
    this.currentCity = newCity;
  }
}