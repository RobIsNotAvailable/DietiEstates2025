import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { switchMap } from 'rxjs/operators';

import { StepGeneralInfoComponent } from './steps/step-general-info/step-general-info';
import { StepLocationComponent } from './steps/step-location/step-location';
import { StepExtraDetailsComponent } from './steps/step-extra-details/step-extra-details';
import { PhotosComponent } from './steps/photos/photos';

import { ListingService } from '../../services/listing';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-listing',
  standalone: true,
  imports: 
  [
    CommonModule,
    StepGeneralInfoComponent, 
    StepLocationComponent,   
    StepExtraDetailsComponent,
    PhotosComponent,
    RouterModule  
  ],
  templateUrl: './create-listing.html',
  styleUrls: ['./create-listing.scss']
})
export class CreateListingComponent 
{
  currentStep: number = 1;
  listingForm: FormGroup;
  isSubmitting: boolean = false;
  stepsAttempted: Set<number> = new Set();

  private stepGroups: { [key: number]: string } = 
  {
    1: 'generalInfo',
    2: 'location',
    3: 'extraDetails',
    4: 'photos'
  };

  constructor(private listingService: ListingService, private cd: ChangeDetectorRef, private router: Router) 
  {
    this.listingForm = new FormGroup({
      generalInfo: new FormGroup({
        name: new FormControl('', [Validators.required]),
        listingType: new FormControl('', [Validators.required]),
        price: new FormControl(null, [Validators.required, Validators.min(0)]),
        description: new FormControl('', [Validators.required]),
        squareMeters: new FormControl(null, [Validators.required, Validators.min(1)]), 
        numberOfRooms: new FormControl(1, [Validators.required, Validators.min(1)])       
      }),
        location: new FormGroup({
          address: new FormControl('', [
            Validators.required,
            (control) => {
              const lat = (control.parent as FormGroup)?.get('latitude')?.value;
              return (control.value && !lat) ? { invalidAddress: true } : null;
            }
          ]),
          city: new FormControl(''),
          zipCode: new FormControl(''),
          latitude: new FormControl(null),
          longitude: new FormControl(null),
          province: new FormControl(''),
          nearSchools: new FormControl(false),
          nearStops: new FormControl(false),
          nearParks: new FormControl(false)
      }),
        extraDetails: new FormGroup({ 
        floor: new FormControl('', [Validators.required]),        
        intern: new FormControl(''),                               
        hasElevator: new FormControl(false, [Validators.required]),
        extraDetails: new FormControl(''),
        energyClass: new FormControl('')                         
      }),
      photos: new FormGroup({ 
        images: new FormControl([], [Validators.required, Validators.minLength(1)]), 
        descriptions: new FormControl([]),
        planimetry: new FormControl(null)                                           
      })
    });

    this.listingForm.valueChanges.subscribe(() => 
    {
      this.listingForm.updateValueAndValidity({ emitEvent: false });
    });
  }

  onSubmit() 
  {
    if (this.listingForm.invalid) 
    {
      this.stepsAttempted.add(this.currentStep);
      console.error("Form non valido", this.listingForm.errors);
      return;
    }

    this.isSubmitting = true;
    const formValue = this.listingForm.value;

    const createRequest = 
    {
      name: formValue.generalInfo.name,
      description: formValue.generalInfo.description,
      price: formValue.generalInfo.price,
      listingType: formValue.generalInfo.listingType,
      squareMeters: formValue.generalInfo.squareMeters,
      numberOfRooms: formValue.generalInfo.numberOfRooms,
      rawAddress: formValue.location.address, 
      
      floor: Number(formValue.extraDetails.floor),
      intern: Number(formValue.extraDetails.intern),
      hasElevator: formValue.extraDetails.hasElevator,
      energyClass: formValue.extraDetails.energyClass,
      otherServices: formValue.extraDetails.extraDetails
    };

    this.listingService.createListing(createRequest).pipe
    (
      switchMap((listingId: number) => 
      {
        const photoEntries: string[] = formValue.photos?.images || []; 
        const descriptionsFromForm: string[] = formValue.photos?.descriptions || [];

        const files = photoEntries.map((base64: string, index: number) => 
        {
          return this.base64ToFile(base64, `photo_${listingId}_${index}.jpg`);
        });

        const descriptions = files.map((_, index: number) => 
        {
          return descriptionsFromForm[index] || ""; 
        });

        return this.listingService.uploadPhotos(listingId, files, descriptions);
      })
    )
    .subscribe
    ({
        next: () => 
        {
          this.isSubmitting = false;
          alert("Annuncio creato con successo!");
          this.router.navigate(['/home']); 
        },
        error: (err) => 
        {
          this.isSubmitting = false;
          this.cd.detectChanges();

          const errorMessage = typeof err.error === 'string' ? err.error : (err.error?.message || "An error occurred");
          alert(errorMessage);
        }
    });
  }

  base64ToFile(base64String: string, fileName: string): File 
  {
    const arr = base64String.split(',');
    const mime = arr[0].match(/:(.*?);/)![1];
    const bstr = atob(arr[1]);
    let n = bstr.length;
    const u8arr = new Uint8Array(n);
    
    while (n--) 
    {
      u8arr[n] = bstr.charCodeAt(n);
    }
    
    return new File([u8arr], fileName, { type: mime });
  }

  setStep(step: number) 
  {
    if (step >= 1 && step <= 4) 
    {
      if (step > this.currentStep) 
      {
        this.stepsAttempted.add(this.currentStep);
      }

      const currentGroupName = this.stepGroups[this.currentStep];
      this.listingForm.get(currentGroupName)?.markAsTouched();

      if (step === 2) 
      {
        const addressControl = this.listingForm.get('location.address');
        const lat = this.listingForm.get('location.latitude')?.value;
        if (lat && addressControl) 
        {
          addressControl.markAsUntouched();
          addressControl.markAsPristine();
        }
      }

      this.currentStep = step;
      this.cd.detectChanges();
      this.listingForm.updateValueAndValidity();
    }
  }

  getStepStatus(step: number): 'active' | 'valid' | 'error' | 'pending' 
  {
    if (this.currentStep === step) return 'active';

    const groupName = this.stepGroups[step];
    const group = this.listingForm.get(groupName);

    if (group) 
    {
      if (group.valid) return 'valid';
      if (group.invalid && this.stepsAttempted.has(step)) return 'error';
    }

    return 'pending';
  }

  hasVisibleErrors(): boolean 
  {
    for (let i = 1; i <= 4; i++) 
    {
      const groupName = this.stepGroups[i];
      const group = this.listingForm.get(groupName);
      if (group && group.invalid && this.stepsAttempted.has(i)) 
      {
        return true;
      }
    }
    return false;
  }

  nextStep() { this.setStep(this.currentStep + 1); }
  prevStep() { this.setStep(this.currentStep - 1); }

}