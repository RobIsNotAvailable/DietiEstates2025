import { Component, EventEmitter, Output, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, filter, switchMap, catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs';
import { LocationService } from '../../services/location';


@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './search-bar.html',
  styleUrls: ['./search-bar.scss']
})
export class SearchBarComponent implements OnInit 
{
    searchCity: string = '';
    suggestions: any[] = [];
    isSearching: boolean = false;
    inputError: boolean = false;          
    private suggestionSelected = false; 

    private input$ = new Subject<string>();

    @Output() toggleFilters = new EventEmitter<void>();
    @Output() search = new EventEmitter<any>();
    @Output() locationSelected = new EventEmitter<any>();

    constructor(private locationService: LocationService, private cd: ChangeDetectorRef) {}

    ngOnInit() 
    {
        this.input$.pipe(
            tap(value => {
                if (!value || value.length <= 2) {
                    this.suggestions = [];
                    this.isSearching = false;
                }
            }),
            filter(value => typeof value === 'string' && value.length > 2),
            debounceTime(400),
            distinctUntilChanged(),
            tap(() => { this.isSearching = true; this.suggestions = []; }),
            switchMap(value => this.locationService.normalizeAddress(value).pipe(
                catchError(() => of([]))
            ))
        ).subscribe({
            next: (results: any) => 
            {
                setTimeout(() => 
                {
                    this.suggestions = results?.length > 0 ? results.map((res: any) => 
                    {
                        const streetPart = (res.street || '') + (res.housenumber ? ' ' + res.housenumber : '');
                        const full = [streetPart, res.postcode, res.city, res.state].filter(Boolean).join(', ');
                        return {
                            ...res,
                            main_text: streetPart || res.city || '',
                            secondary_text: `${res.city || ''}, ${res.state || ''}`,
                            full_address: full
                        };
                    }) : [];
                    
                    this.isSearching = false;
                    this.cd.markForCheck(); 
                    this.cd.detectChanges();
                }, 0);
            },
            error: () => {
                this.suggestions = [];
                this.isSearching = false;
                this.cd.detectChanges();
            }
        });
    }

    onInputChange(value: string) 
    {
        this.searchCity = value;
        this.suggestionSelected = false;  
        this.inputError = false;          
        this.input$.next(value);
    }

    selectSuggestion(s: any) 
    {
        this.suggestionSelected = true;   
        this.inputError = false;          
        this.searchCity = s.full_address || s.main_text;
        this.suggestions = [];

        const isStreet = !!s.street;
        const locationData = isStreet
            ? { city: null, lat: s.lat, lon: s.lon }
            : { city: s.city, lat: null, lon: null };

        this.locationSelected.emit(locationData);
        this.cd.detectChanges();
    }

    onSearch(): void 
    {
        if (this.suggestions.length > 0 || (this.searchCity && !this.suggestionSelected)) {
            this.searchCity = '';
            this.suggestions = [];
            this.inputError = true;
            this.cd.detectChanges();
            return;
        }
        this.suggestions = [];
        this.search.emit();
    }

    onFilterClick() {
        this.toggleFilters.emit();
    }

    reset() 
    {
        this.searchCity = '';
        this.suggestions = [];
        this.isSearching = false;
        this.inputError = false;          
        this.suggestionSelected = false;  
        this.cd.detectChanges();
    }
}