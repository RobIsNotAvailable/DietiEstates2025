import { Component, Input, OnInit, ChangeDetectorRef } from '@angular/core'; 
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup } from '@angular/forms';
import { debounceTime, distinctUntilChanged, switchMap, filter, tap, catchError, delay } from 'rxjs/operators';
import { of } from 'rxjs';

import { LocationService, GeoapifyProperties } from '../../../../services/location';

@Component
({
  selector: 'app-step-location',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './step-location.html',
  styleUrls: ['./step-location.scss']
})

export class StepLocationComponent implements OnInit 
{
  @Input() parentForm!: FormGroup;
  suggestions: any[] = [];
  isSearching = false;

  constructor(private locationService: LocationService, private cd: ChangeDetectorRef) {}

  get locationGroup() 
  {
    return this.parentForm.get('location') as FormGroup;
  }

  ngOnInit() 
  {
    this.locationGroup.get('address')?.valueChanges.pipe
    (
      tap(value => 
      {
        if (!value || value.length <= 5) 
        {
          this.suggestions = [];
          this.isSearching = false;
        }
      }),
      filter(value => typeof value === 'string' && value.length > 5),
      debounceTime(500),
      distinctUntilChanged(),
      tap(() => 
      {
        this.isSearching = true;
        this.suggestions = []; 
      }),
      switchMap(value => this.locationService.normalizeAddress(value).pipe
      (
        delay(300), 
        catchError(() => of([])) 
      ))
    )
    .subscribe
    ({
      next: (results: any) => 
      {
        if (results && results.length > 0) 
        {
          this.suggestions = results.map((res: any) => 
          ({
            ...res,
            main_text: (res.street || '') + (res.houseNumber ? ' ' + res.houseNumber : ''),
            secondary_text: `${res.city || ''}, ${res.province || ''}`, 
            full_address: res.formattedAddress 
          }));
        } 
        else 
        {
          this.suggestions = [];
        }
        this.isSearching = false;
        this.cd.detectChanges(); 
      },
      error: () => 
      {
        this.suggestions = [];
        this.isSearching = false;
        this.cd.detectChanges(); 
      }
    });
  }

  selectAddress(s: any) 
  {
      const selectedAddress = s.full_address || s.formatted || s.main_text;

      this.locationGroup.patchValue
      ({
          address: selectedAddress, 
          city: s.city,
          zipCode: s.postcode,
          latitude: s.lat,
          longitude: s.lon,
          province: s.province || ''
      }, { emitEvent: false }); 

      this.suggestions = []; 
      this.cd.detectChanges();
  }
}