import { Component, Input, OnInit, ChangeDetectorRef, ViewChild} from '@angular/core'; 
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup } from '@angular/forms';
import { debounceTime, distinctUntilChanged, switchMap, filter, tap, catchError, delay } from 'rxjs/operators';
import { of } from 'rxjs';

import { LocationService } from '../../../../services/location';
import { MapComponent } from '../../../../components/map/map';
import { LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-step-location',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MapComponent, LucideAngularModule], 
  templateUrl: './step-location.html',
  styleUrls: ['./step-location.scss']
})
export class StepLocationComponent implements OnInit 
{
  @Input() parentForm!: FormGroup;
  @Input() showErrors: boolean = false;
  @ViewChild(MapComponent) mapComponent!: MapComponent;

  suggestions: any[] = [];
  isSearching = false;
  isPoiLoading = false;
  showSuggestions = false;

  currentLat: number | null = null;
  currentLon: number | null = null;

  poiStatus = { hasBus: false, hasPark: false, hasSchool: false };

  constructor(private locationService: LocationService, private cd: ChangeDetectorRef) {}

  get locationGroup() 
  {
    return this.parentForm.get('location') as FormGroup;
  }

  ngOnInit() 
  {
    const existingLat = this.locationGroup.get('latitude')?.value;
    const existingLon = this.locationGroup.get('longitude')?.value;

    if (existingLat && existingLon) 
    {
      this.currentLat = existingLat;
      this.currentLon = existingLon;
    }

    this.poiStatus = {
        hasBus: this.locationGroup.get('nearStops')?.value || false,
        hasPark: this.locationGroup.get('nearParks')?.value || false,
        hasSchool: this.locationGroup.get('nearSchools')?.value || false,
    };
    
    this.locationGroup.get('address')?.valueChanges.pipe
    (
      tap(value => 
      {
        if (!value || value.length <= 5) 
        {
          this.suggestions = [];
          this.isSearching = false;
          this.locationGroup.patchValue(
            { latitude: null, longitude: null }, 
            { emitEvent: false }
          );
          this.currentLat = null;
          this.currentLon = null;
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
    .subscribe({
      next: (results: any) => 
      {
        this.suggestions = results?.length > 0
          ? results.map((res: any) => ({
              ...res,
              main_text: (res.street || '') + (res.houseNumber ? ' ' + res.houseNumber : ''),
              secondary_text: `${res.city || ''}, ${res.province || ''}`,
              full_address: res.formattedAddress
            }))
          : [];

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
    
    this.currentLat = s.lat;
    this.currentLon = s.lon;
    this.isPoiLoading = true;


    this.locationGroup.patchValue({
      address: selectedAddress, 
      city: s.city,
      zipCode: s.postcode,
      latitude: s.lat,
      longitude: s.lon,
      province: s.province || ''
    }, { emitEvent: false });

    this.locationGroup.get('address')?.updateValueAndValidity({ emitEvent: false });

    this.locationService.getSurroundings(s.lat, s.lon).subscribe({
      next: (res) => 
      {
        this.poiStatus = { hasBus: res.hasBus, hasPark: res.hasPark, hasSchool: res.hasSchool };
        this.locationGroup.patchValue({
            nearStops: res.hasBus,
            nearParks: res.hasPark,
            nearSchools: res.hasSchool
        }, { emitEvent: false });
        this.isPoiLoading = false;
        this.cd.detectChanges();
      },
      error: () => 
      {
        this.poiStatus = { hasBus: false, hasPark: false, hasSchool: false };
        this.isPoiLoading = false;
      }
    });
    
    this.suggestions = []; 
    this.cd.detectChanges();
  }

  recenterMap() 
  {
    const lat = this.mapComponent.lat;
    const lon = this.mapComponent.lon;

    if (lat && lon) 
    {
      this.currentLat = null;
      this.currentLon = null;
      this.cd.detectChanges();

      setTimeout(() => 
      {
        this.currentLat = lat;
        this.currentLon = lon;
        this.cd.detectChanges();
      }, 50);
    }
  }

  onAddressInput() 
  {
    this.locationGroup.patchValue(
      { latitude: null, longitude: null }, 
      { emitEvent: false }
    );
    this.currentLat = null;
    this.currentLon = null;
    this.suggestions = [];
    this.locationGroup.get('address')?.updateValueAndValidity({ emitEvent: false });
  }
}