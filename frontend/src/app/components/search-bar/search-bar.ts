import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 

@Component(
{
  selector: 'app-search-bar',
  standalone: true,
  imports: [CommonModule, FormsModule], 
  templateUrl: './search-bar.html',
  styleUrls: ['./search-bar.scss']
})
export class SearchBarComponent 
{
  searchCity: string = '';
  contractType: string = '';

  @Output() toggleFilters = new EventEmitter<void>();
  @Output() search = new EventEmitter<string>();
  
  onSearch(): void 
  {
    this.search.emit(this.searchCity);
  }

  onFilterClick() 
  {
    this.toggleFilters.emit();
  }
}