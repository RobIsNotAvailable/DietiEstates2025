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
export class SearchBarComponent implements OnInit {
  searchCity: string = '';
  suggestions: any[] = [];
  isSearching: boolean = false;

  private input$ = new Subject<string>();

  @Output() toggleFilters = new EventEmitter<void>();
  @Output() search = new EventEmitter<string>();

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
      next: (results: any) => {
        this.suggestions = results?.length > 0
            ? results.map((res: any) => ({
                ...res,
                main_text: res.formattedAddress || (res.street || '') + (res.houseNumber ? ' ' + res.houseNumber : ''),
                secondary_text: `${res.city || ''}, ${res.province || ''}`,
                full_address: res.formattedAddress
            }))
            : [];
        this.isSearching = false;
        this.cd.detectChanges();
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
    this.input$.next(value);
  }

  selectSuggestion(s: any) 
  {
    this.searchCity = s.full_address || s.main_text;
    this.suggestions = [];
    this.cd.detectChanges();
  }

  onSearch(): void {
    this.suggestions = [];
    this.search.emit(this.searchCity);
  }

  onFilterClick() {
    this.toggleFilters.emit();
  }
}