import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

import { StepGeneralInfoComponent } from './steps/step-general-info/step-general-info';
import { StepLocationComponent } from './steps/step-location/step-location';
import { StepDetailsComponent } from './steps/step-details/step-details';

@Component({
  selector: 'app-create-listing',
  standalone: true,
  imports: 
  [
    CommonModule,
    StepGeneralInfoComponent, 
    StepLocationComponent,   
    StepDetailsComponent      
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
    this.listingForm = new FormGroup(
    {
      generalInfo: new FormGroup(
      {
        name: new FormControl('', [Validators.required]),
        type: new FormControl('', [Validators.required]),
        price: new FormControl(null, [Validators.required, Validators.min(0)]),
        description: new FormControl('', [Validators.required])
      }),
      location: new FormGroup({}),
      details: new FormGroup({})
    });
  }
  
  setStep(step: number) 
  {
    if (step >= 1 && step <= 3) 
    {
      this.currentStep = step;
    }
  }
}