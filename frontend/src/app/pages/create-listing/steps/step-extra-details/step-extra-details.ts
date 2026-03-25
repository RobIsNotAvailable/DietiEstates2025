import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-step-extra-details',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './step-extra-details.html',
  styleUrls: ['./step-extra-details.scss']
})
export class StepExtraDetailsComponent 
{
  @Input() parentForm!: FormGroup;
  @Input() showErrors: boolean = false;
  get extraGroup() 
  {
    return this.parentForm.get('extraDetails') as FormGroup;
  }

  blockInvalidChars(event: KeyboardEvent): void 
  {
    const invalidChars = ['e', 'E', '+', '-', ',', '.'];
    if (invalidChars.includes(event.key)) 
    {
      event.preventDefault();
    }
  }

  validateNumericInput(event: any, controlName: string): void {
    const input = event.target as HTMLInputElement;
    let value = input.value.replace(/\D/g, ''); 
    
    const numericValue = value ? parseInt(value, 10) : null;
    
    this.extraGroup.get(controlName)?.patchValue(numericValue, { emitEvent: true });
    
    input.value = value;
  }


  blockSpecialChars(event: KeyboardEvent): void 
  {
    const allowedKeys = /^[a-zA-Z0-9]$/;
    const navigationKeys = ['Backspace', 'Tab', 'ArrowLeft', 'ArrowRight', 'Delete'];

    if (!allowedKeys.test(event.key) && !navigationKeys.includes(event.key)) 
    {
      event.preventDefault();
    }
  }

  validateAlphanumeric(event: any, controlName: string): void 
  {
    const input = event.target as HTMLInputElement;
    const cleanedValue = input.value.replace(/[^a-zA-Z0-9]/g, ''); 
    
    this.extraGroup.get(controlName)?.patchValue(cleanedValue, { emitEvent: true });
    input.value = cleanedValue;
  }
}