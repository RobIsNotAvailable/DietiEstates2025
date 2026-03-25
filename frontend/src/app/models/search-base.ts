import { Directive, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { FilterPanelComponent } from '../components/filter-panel/filter-panel';

@Directive()
export abstract class SearchBaseComponent 
{

    @ViewChild(FilterPanelComponent) filterPanel!: FilterPanelComponent;
    
    currentFilters: any = {};
    isFiltersOpen: boolean = false;

    constructor(protected router: Router) {}

    handleLocationChange(locationData: any) {
        this.currentFilters = {
            ...this.currentFilters,
            city: locationData.city ?? null,
            latitude: locationData.lat ?? null,
            longitude: locationData.lon ?? null,
            label: locationData.label ?? null
        };
    }

    toggleFilterPanel() {
        this.isFiltersOpen = !this.isFiltersOpen;
    }

    abstract handleFilterChange(filterOptions: any): void;
    abstract handleSearchBarSearch(): void;
}