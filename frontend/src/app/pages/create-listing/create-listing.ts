import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

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

  setStep(step: number) 
  {
    if (step >= 1 && step <= 3) 
    {
      this.currentStep = step;
    }
  }
}