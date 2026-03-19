import { CommonModule } from '@angular/common';
import { Component, Input, ChangeDetectorRef } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

import { ListingSliderComponent } from '../../../../components/listing-slider/listing-slider';

@Component({
  selector: 'app-step-general-info',
  standalone: true, 
  imports: [CommonModule, ReactiveFormsModule], 
  templateUrl: './step-general-info.html',
  styleUrls: ['./step-general-info.scss']
})
export class StepGeneralInfoComponent 
{
  @Input() parentForm!: FormGroup;
  @Input() showErrors: boolean = false;
   
  formattedPrice: string = ''; 

  constructor(private cd: ChangeDetectorRef) {}

  get generalGroup() 
  {
    return this.parentForm.get('generalInfo') as FormGroup;
  }

  formatPrice(event: any) 
  {
    const input = event.target as HTMLInputElement;
    
    let rawValue = input.value.replace(/\D/g, '');

    if (rawValue) 
    {
      if (rawValue.length > 10) rawValue = rawValue.substring(0, 10);

      const numericValue = parseInt(rawValue, 10);

      this.formattedPrice = new Intl.NumberFormat('de-DE').format(numericValue);
      
      this.generalGroup.patchValue({ price: numericValue }, { emitEvent: true });
    } 
    else 
    {
      this.formattedPrice = '';
      this.generalGroup.patchValue({ price: null }, { emitEvent: true });
    }

    input.value = this.formattedPrice;
  }
}