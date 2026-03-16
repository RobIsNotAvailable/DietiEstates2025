import { Component, Input, Output, EventEmitter, HostBinding } from '@angular/core';
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';

interface FilterOptions 
{
  mode: 'sale' | 'rent';
  minPrice: number | null;
  maxPrice: number | null;
  minRooms: number;
  energyClass: string;
}

@Component
({
  selector: 'app-filter-panel',
  standalone: true,    
  imports: [FormsModule, CommonModule], 
  templateUrl: './filter-panel.html',
  styleUrls: ['./filter-panel.scss'],
})
export class FilterPanelComponent 
{
  @Input() isOpen: boolean = false;
  @Input() mode: 'sale' | 'rent' = 'sale';

  @Output() close = new EventEmitter<void>();
  @Output() filterChanged = new EventEmitter<any>();

  private hasBeenOpened = false;

  @HostBinding('class.active') get active() 
  {
    if (this.isOpen) 
    {
      this.hasBeenOpened = true;
    }
    return this.isOpen;
  }

  @HostBinding('class.closing') get closing() 
  {
    return !this.isOpen && this.hasBeenOpened;
  }

  energyClasses = ['A', 'B', 'C', 'D'];

  get priceChips() 
  {
      return this.filters.mode === 'sale' 
        ? [100000, 200000, 300000, 500000] 
        : [400, 700, 1000, 1500]; 
  }

  filters: FilterOptions = 
  {
      mode: 'sale',
      minPrice: null, 
      maxPrice: null,
      minRooms: 1,
      energyClass: ''
  };

  ngOnChanges() 
  {
    if (this.isOpen) 
    {
      this.filters.mode = this.mode;
    }
  }

  setMode(m: 'sale' | 'rent') 
  {
    this.filters.mode = m;

    this.filters.minPrice = null;
    this.filters.maxPrice = null;
  }

  applyFilters() 
  {
      this.filterChanged.emit(this.filters); 
      this.close.emit();
  }

  onCloseClick() 
  { 
    this.close.emit(); 
  }

  selectMinPrice(p: number | null) 
  { 
    this.filters.minPrice = p; 
  }

  selectMaxPrice(p: number | null) 
  { 
    this.filters.maxPrice = p; 
  }
}