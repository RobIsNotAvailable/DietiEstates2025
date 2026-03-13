import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';


import { StepGeneralInfoComponent } from './steps/step-general-info/step-general-info';
import { StepLocationComponent } from './steps/step-location/step-location';
import { StepDetailsComponent } from './steps/step-details/step-details';
import { PhotosComponent } from './steps/photos/photos';

@Component({
  selector: 'app-create-listing',
  standalone: true,
  imports: 
  [
    CommonModule,
    StepGeneralInfoComponent, 
    StepLocationComponent,   
    StepDetailsComponent,
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

  constructor(private fb: FormBuilder) 
  {
    this.listingForm = new FormGroup({
      generalInfo: new FormGroup({
        name: new FormControl('', [Validators.required]),
        type: new FormControl('', [Validators.required]),
        price: new FormControl(null, [Validators.required, Validators.min(0)]),
        description: new FormControl('', [Validators.required]),
        surface: new FormControl(null), 
        rooms: new FormControl(1)       
      }),
      location: new FormGroup({
        address: new FormControl('', [Validators.required])
      }),
      details: new FormGroup({ 
        floor: new FormControl(''),
        inner: new FormControl(''),
        hasElevator: new FormControl(false),
        extraDetails: new FormControl('')
      }),
      photos: new FormGroup({ 
        images: new FormControl([]),
        planimetry: new FormControl(null)
      })
    });
  }
  
  setStep(step: number) 
  {
    if (step >= 1 && step <= 4) 
    {
      this.currentStep = step;
    }
  }
}