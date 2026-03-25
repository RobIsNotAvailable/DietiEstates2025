import { Component, Input, Output, EventEmitter, HostBinding, HostListener, ElementRef } from '@angular/core';
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';
import { LucideAngularModule } from 'lucide-angular';

interface FilterOptions 
{
  listingType: 'SALE' | 'RENT' | null;
  minPrice: number | null;
  maxPrice: number | null;
  minRooms: number;
  energyClass: string;
  nearStops: boolean;
  nearParks: boolean;
  nearSchools: boolean;
}

@Component
({
  selector: 'app-filter-panel',
  standalone: true,    
  imports: [FormsModule, CommonModule, LucideAngularModule], 
  templateUrl: './filter-panel.html',
  styleUrls: ['./filter-panel.scss'],
})
export class FilterPanelComponent 
{
  @Input() isOpen: boolean = false;
  @Input() listingType: 'SALE' | 'RENT' | null = null;
  @Input() initialFilters: Partial<FilterOptions> | null = null;

  @Output() close = new EventEmitter<void>();
  @Output() onSearch = new EventEmitter<any>();
  @Output() onReset = new EventEmitter<void>();

  constructor(private eRef: ElementRef) {}

  @HostListener('document:mousedown', ['$event'])
  onGlobalClick(event: MouseEvent): void 
  {
    if (!this.isOpen) return;

    if (!this.eRef.nativeElement.contains(event.target)) 
    {
      this.onCloseClick();
    }
  }

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

  energyClasses = ['A4', 'A3', 'A2', 'A1', 'B', 'C', 'D'];

  get typeChips(): ('SALE' | 'RENT')[] 
  {
    return ['SALE', 'RENT']; 
  }

  get priceChips() 
  {
      return this.filters.listingType === 'RENT' 
        ? [400, 700, 1000, 1500]
        : [100000, 200000, 300000, 500000]; 
  }

  filters: FilterOptions = 
  {
    listingType: null,
    minPrice: null, 
    maxPrice: null,
    minRooms: 1,
    energyClass: '',
    nearStops: false,
    nearParks: false,
    nearSchools: false
  };

  ngOnInit() 
  {
    if (this.listingType) 
      this.filters.listingType = this.listingType;
    
    if (this.initialFilters) 
        this.filters = { ...this.filters, ...this.initialFilters }; 
  }

  setType(t: 'SALE' | 'RENT' | null) 
  {
      this.filters.listingType = t;
      
      this.filters.minPrice = null;
      this.filters.maxPrice = null;
  }

  applyFilters() 
  {
      this.onSearch.emit(this.filters); 
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

  resetFilters() 
  {
    this.filters = 
    {
      listingType: null,
      minPrice: null,
      maxPrice: null,
      minRooms: 1,
      energyClass: '',
      nearStops: false,
      nearParks: false,
      nearSchools: false
    };
    
    this.onReset.emit();
    this.applyFilters();
  }

  blockInvalidChars(event: KeyboardEvent): void 
  {
    const invalidChars = ['e', 'E', '+', '-', ',', '.'];
    if (invalidChars.includes(event.key)) 
    {
      event.preventDefault();
    }
  }

  validateNumericInput(event: any, controlName: string): void 
  {
    const input = event.target as HTMLInputElement;
    let value = input.value.replace(/\D/g, ''); 
    
    const numericValue = value ? parseInt(value, 10) : null;
    
    (this.filters as any)[controlName] = numericValue;
    
    input.value = value;
  }
}