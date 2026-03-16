import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 

@Component(
{
  selector: 'app-search-bar',
  standalone: true,
  imports: [CommonModule, FormsModule], 
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss']
})
export class SearchBarComponent 
{
  searchCity: string = '';
  contractType: string = '';

  @Output() toggleFilters = new EventEmitter<void>();

  onSearch(): void 
  {
    console.log('Search initiated for:', this.searchCity, 'Type:', this.contractType);
  }

  onFilterClick() 
  {
    this.toggleFilters.emit();
  }
}