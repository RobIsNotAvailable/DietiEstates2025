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
}